package TC;

import Common.FileUtility;
import Common.MyWebDriver;
import PO.CommunityHomePage;
import PO.CommunityLoginPage;
import PO.Component.PostView;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.io.File;
import java.time.Duration;
import java.util.*;

public class TestPublishingPostInCommunity {

    private MyWebDriver myDriver;
    private CommunityHomePage communityHomePage;

    private String communityServerDomain;

    private String urlCommunityLoginPage;

    private String urlCommunityHomePage;

    private String urlCommunityLogoutPath;

    private File testDataFolder;

    private List<Map<String,?>> data;

    private Map<String,String> user;


    @BeforeClass
    public void init(ITestContext context) throws Exception {
        this.myDriver = new MyWebDriver();
        this.myDriver.setupEdgeBrowser();
        this.myDriver.setupWait(2000);
        this.myDriver.setDriverProperties();
        this.communityServerDomain=context.getCurrentXmlTest().
                getParameter("COMMUNITY_SERVER_DOMAIN");
        this.urlCommunityHomePage=this.communityServerDomain
                +context.getCurrentXmlTest().getParameter("COMMUNITY_HOME_PATH");
        this.urlCommunityLoginPage=this.communityServerDomain
                +context.getCurrentXmlTest().getParameter("COMMUNITY_LOGIN_PATH");
        this.urlCommunityLogoutPath=context.getCurrentXmlTest().getParameter("COMMUNITY_LOGOUT_PATH");
        this.testDataFolder =new File(System.getProperty("user.dir")+File.separator
                +context.getCurrentXmlTest().getParameter("TEST_DATA_PATH"));
        this.data= (List<Map<String, ?>>) FileUtility.getDataFromYaml(
                this.testDataFolder.getCanonicalPath()+File.separator
                + context.getCurrentXmlTest().getParameter("TEST_DATA_FILE"));
        this.user=(Map<String,String>) FileUtility.getDataFromYaml(
                this.testDataFolder.getCanonicalPath()+File.separator
                        + context.getCurrentXmlTest().getParameter("TEST_USER"));
        this.communityHomePage=this.getCommunityHomePageByUserLogin(
                this.user.get("emailAddress").toString(),this.user.get("password").toString());

    }

    @DataProvider(name="Posts")
    public Iterator<Object[]> getTestData() throws Exception{
        List<Object[]> dataList = new Vector<Object[]>();
            for(Map<String,?> tempMap:this.data){
                dataList.add(new Object[]{tempMap});
            }
        return dataList.iterator();
    }

    private CommunityHomePage getCommunityHomePageByUserLogin(String emailAddress, String password){
        CommunityHomePage communityHomePage = null;
        CommunityLoginPage userLoginPage=null;
           this.myDriver.getWebDriver().get(this.urlCommunityHomePage);
           if(this.myDriver.isPageRedirected(this.urlCommunityLoginPage)){
                userLoginPage=new CommunityLoginPage(this.urlCommunityLoginPage);
                userLoginPage.onLoad(this.myDriver.getWebDriver());
                userLoginPage.userLogin(emailAddress,password);
            }
           if(this.myDriver.isPageRedirected("home")){
               this.myDriver.getWebDriver().get(this.urlCommunityHomePage);
           }
            communityHomePage=new CommunityHomePage(this.urlCommunityHomePage);
           communityHomePage.onLoad(this.myDriver.getWebDriver());

       return communityHomePage;
    }

    @Test(dataProvider = "Posts")
    public void verifyPublishingPost(Map<String,?> postData) throws Exception{
        boolean res=false;
        String description = String.valueOf(postData.get("description"));
        String contentText = "";
        String fileName="";

        if(postData.containsKey("text-content")){
            contentText=String.valueOf(postData.get("text-content"));
        }
        if(postData.containsKey("image")){
            fileName=String.valueOf(postData.get("image"));
        }
        System.out.println(description+" Start");
        this.publishPost(postData);
        myDriver.refresh();
        this.communityHomePage.onLoad(this.myDriver.getWebDriver());
        if(description.toLowerCase().contains("security test")
                && description.toLowerCase().contains("xss")
                && this.myDriver.isShowingUpAlert()){
            res=false;
            Assert.assertTrue(res);
            this.myDriver.acceptInAlert();
        }
        if(!this.communityHomePage.isNotEmptyStatusInPostList(
                new WebDriverWait(this.myDriver.getWebDriver(),
                        Duration.ofSeconds(3)))){
            System.out.println("No Posts Published");
            Assert.assertTrue(false);
        }
        this.communityHomePage.iniPosts(new WebDriverWait(this.myDriver.getWebDriver(),
                Duration.ofSeconds(2)));
        if(this.communityHomePage.getPosts().size()==0){
            System.out.println("Did not Find any Post published");
            Assert.assertTrue(false);
            return;
        }
        for(PostView postView :this.communityHomePage.getPosts()){
            String postContentText=postView.getText();
            if(!contentText.equals("")){
                if(postContentText.equals(contentText)){
                    System.out.println("Find the content published in post");
                    System.out.println("Input Content:" +contentText);
                    System.out.println("Get Content in Post:"+postContentText);
                    res=true;
                }
                else if(postContentText.length()==4000
                        && contentText.contains(postContentText)){
                        System.out.println("Find the long text content published in post");
                        System.out.println("Input Content:" +contentText);
                        System.out.println("Get Content in Post:"+postContentText);
                        res=true;
                }
                else{
                    continue;
                }
            }
            if(!fileName.trim().equals("")){
               res = postView.getImage()==null?false:true;
               if(res){
                   System.out.println("find the image in the post");
                   System.out.println(postView.getImage().getAttribute("src"));
                   break;
               }
            }
            if(res){
                break;
            }
        }
        System.out.println(description+" End");
        Assert.assertTrue(res);
    }
    private void publishPost(Map<String,?> postData) throws Exception{
        String contentText = "";
        String imageFile="";
        if(postData.containsKey("text-content")){
            contentText= String.valueOf(postData.get("text-content"));
        }
        if(postData.containsKey("image")){
           imageFile=postData.get("image").toString();
        }
        if(!FileUtility.isFileExisted(imageFile)){
            imageFile=this.testDataFolder.getAbsolutePath()+File.separator+imageFile;
        }
        if(!FileUtility.isFileExisted(imageFile)){
            System.out.println("Pls check the test data config file:");
            return;
        }
        System.out.println("Creating a Post Start");
        this.communityHomePage.showEditor(this.myDriver.getWebDriver(),
                this.myDriver.getWebDriverWait());
        this.communityHomePage.publishPost(contentText,imageFile,this.myDriver.getWebDriver(),
              this.myDriver.getWebDriverWait());
        System.out.println("Creating a Post End");
    }

    @Test
    public void verifyPublishPostWithEmptyContent()throws Exception{
        boolean res=false;
        this.communityHomePage.showEditor(this.myDriver.getWebDriver(),
                this.myDriver.getWebDriverWait());
        this.communityHomePage.inputPostContentInEditor("");
        res=this.communityHomePage.isEmptyInPostEditor(this.myDriver.getWebDriver(),
                this.myDriver.getWebDriverWait());
        this.communityHomePage.closePostEditorWithoutPublishing();
        Assert.assertTrue(res);

    }
    @AfterClass
    public void userLogout(){
        System.out.println("user logout");
        this.myDriver.getWebDriver().get(this.communityServerDomain+this.urlCommunityLogoutPath);
    }
    @AfterTest
    public void tearDown(){
        this.myDriver.tearDown();
    }

}
