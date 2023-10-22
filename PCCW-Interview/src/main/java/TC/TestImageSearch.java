package TC;

import Common.*;
import PO.BaiduImageSearchPage;
import PO.BaiduImageSearchResultPage;
import PO.BaiduImageSearchedResultDetailsPage;
import PO.Component.BaiduImageThumbnail;
import org.testng.ITestContext;
import org.testng.annotations.*;
import org.testng.Assert;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.Map;


public class TestImageSearch {
    private MyWebDriver myDriver;

    private BaiduImageSearchPage baiduImageSearchPage;
    private BaiduImageSearchResultPage baiduImageSearchResultPage;

    private BaiduImageSearchedResultDetailsPage baiduImageDetailsPage;
    private String imageUploadDomain="";
    private String imageSearchedDomain="";

    private String imageUpload="";

    private File imageDownloadPath;
    private File screenshotFolder;
    private File passImageFolder;
    private File failedImageFolder;

    private File testDataFolder;

    @BeforeTest
    public void init(ITestContext context) throws Exception{
        this.myDriver=new MyWebDriver();
        this.myDriver.setupEdgeBrowser();
        this.myDriver.setupWait(4000);
        this.myDriver.setDriverProperties();
        this.imageSearchedDomain=context.getCurrentXmlTest().getParameter("IMAGE_SEARCHED_DOMAIN");
        this.imageUploadDomain=context.getCurrentXmlTest().getParameter("IMAGE_UPLOAD_DOMAIN");

        this.imageDownloadPath=new File(System.getProperty("user.dir")
                +context.getCurrentXmlTest().getParameter("DOWNLOAD_DIR")+
                DateTimeUtility.getSystemCurrentDateTime("yyyy-MM-dd HHMMss"));
        this.testDataFolder =new File(System.getProperty("user.dir")+File.separator
                +context.getCurrentXmlTest().getParameter("TEST_DATA_PATH"));
        this.screenshotFolder =new File(System.getProperty("user.dir")
                +context.getCurrentXmlTest().getParameter("SCREENSHOT_DIR")+
                DateTimeUtility.getSystemCurrentDateTime("yyyy-MM-dd HHMMss"));
        this.imageUpload=this.testDataFolder.getCanonicalPath()+File.separator
                +context.getCurrentXmlTest().getParameter("IMAGE_UPLOAD");
        this.passImageFolder=new File(this.imageDownloadPath.getCanonicalPath(),"PASS");
        this.failedImageFolder=new File(this.imageDownloadPath.getCanonicalPath(),"FAILED");
        FileUtility.makeDir(this.imageDownloadPath);
        FileUtility.makeDir(this.screenshotFolder);
        FileUtility.makeDir(this.passImageFolder);
        FileUtility.makeDir(this.failedImageFolder);



    }
    @BeforeMethod
    public void preparePages() throws Exception {
        this.baiduImageSearchPage=new BaiduImageSearchPage(this.imageUploadDomain);
        this.baiduImageSearchPage.onLoad(this.myDriver.getWebDriver());
        this.baiduImageSearchPage.uploadImage(this.imageUpload,this.myDriver.getWebDriver());
        if(myDriver.isPageRedirected(this.imageSearchedDomain)){
            this.baiduImageSearchResultPage=new BaiduImageSearchResultPage();
            this.baiduImageSearchResultPage.onLoad(this.myDriver.getWebDriver());
        }
        Assert.assertFalse(
                this.baiduImageSearchResultPage.isEmptyResultSearched(this.myDriver.getWebDriverWait()));
        this.myDriver.lazyLoading();
        this.baiduImageSearchResultPage.initiateSimilarImageSearchedThumbnails(this.myDriver.getWebDriver(),
                this.myDriver.getWebDriverWait());
    }



    @Test
    public void verifyImageSimilarSearched()throws Exception{
        List<String> similarImageSearchedUrls=this.baiduImageSearchResultPage.getSimilarImageUrls();
        List<File> similarImagesDownloaded=this.imagesDownloaded(similarImageSearchedUrls);
        int passed=0, failed=0;
        ImageUtility.saveScreenshotOnWeb(this.myDriver.getWebDriver(),this.screenshotFolder);
        for(File testImage:similarImagesDownloaded){
            if(ImageUtility.isImagesMatching(this.imageUpload,
                    testImage.getAbsolutePath()+File.pathSeparator+testImage.getName())){
                passed++;
                FileUtility.copyFile(testImage,this.passImageFolder.toPath().toString());
                continue;
            }
            failed++;
            FileUtility.copyFile(testImage,this.failedImageFolder.toPath().toString());
        }
        System.out.println("Image Related Passed:"+passed);
        System.out.println("image Related Failed:"+failed);
    }


    @Test
    public void verifyVisitSimilarImagePage(ITestContext context){

        String strVisitIndex=context.getCurrentXmlTest().getParameter("VISIT_RESULT");
        Map<String,Integer> mVisitIndex=this.getImageVisitIndexes(strVisitIndex);
        BaiduImageThumbnail imageThumbnail=this.baiduImageSearchResultPage.getThumbnailByIndex(
                mVisitIndex.get("rowIndex"),mVisitIndex.get("colIndex"));
        String thumbnailImageUrl=imageThumbnail.getImageUrl();
        imageThumbnail.onClick(this.myDriver.getWebDriver());
        if(this.myDriver.isPageRedirected("https://graph.baidu.com/pcpage/similar")){
            this.baiduImageDetailsPage=new BaiduImageSearchedResultDetailsPage();
            this.baiduImageDetailsPage.onLoad(this.myDriver.getWebDriver());
        }
        ImageUtility.saveScreenshotOnWeb(this.myDriver.getWebDriver(),this.screenshotFolder);
        Assert.assertEquals(this.baiduImageDetailsPage.getImageUrl(),thumbnailImageUrl);

    }
    @AfterTest
    public void testingDone(){
        this.myDriver.tearDown();
    }
    private Map<String,Integer> getImageVisitIndexes(String visitIndex){
        Map<String,Integer> visitIndexes= new TreeMap<>();
        int row=0,col=0;
        if(visitIndex.contains("(") && visitIndex.contains(")")  && visitIndex.contains(",")){
            row=Integer.valueOf(visitIndex.substring(visitIndex.indexOf("(")+1,visitIndex.indexOf(",")));
            col=Integer.valueOf(visitIndex.substring(visitIndex.indexOf(",")+1,visitIndex.indexOf(")")));
        }
        else{
            row=0;
            col="".equals(visitIndex)?0:Integer.valueOf(visitIndex);
        }
        visitIndexes.put("rowIndex",row);
        visitIndexes.put("colIndex",col);
        return visitIndexes;

    }


    private List<File> imagesDownloaded(List<String> imageUrls) throws Exception{
        List<File> images=new ArrayList<>();
        List<FileDownloadThread> imageDownloadThreads= new ArrayList<FileDownloadThread>();
        for(int i=0;i<imageUrls.size();i++){
            FileDownloadThread imageDownloadThread=new FileDownloadThread();
            imageDownloadThreads.add(imageDownloadThread);
            imageDownloadThread.setFileUrl(imageUrls.get(i));
            imageDownloadThread.setDesDirectory(this.imageDownloadPath.getPath());
            imageDownloadThread.setFileName(String.valueOf(i)+".jpg");
            imageDownloadThread.start();
        }
        for(int j=0;j<imageDownloadThreads.size();j++){
            File image=imageDownloadThreads.get(j).getFileDownloaded();
            imageDownloadThreads.get(j).join();
            images.add(image);
        }



        return images;
    }
}
