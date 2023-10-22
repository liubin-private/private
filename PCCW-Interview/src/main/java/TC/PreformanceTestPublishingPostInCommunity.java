package TC;

import Common.DateTimeUtility;
import Common.FileUtility;
import Common.MyWebDriver;
import PO.CommunityHomePage;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PreformanceTestPublishingPostInCommunity {
    private double testStartTimeStamp=0L;
    private String testStart;
    private String testEnd;
    private double testEndTimeStamp=0L;
    private int testDurationInSec=1;
    private double avgTimeInEachThread=0;
    private float tps=0F;
    private float qps=0F;
    private int passed=0;
    private int failed=0;
    private int total=1;
    private float errorRatePercentage=0;

    private List<MyWebDriver> myBrowsers =new ArrayList<>();

    private List<PublishPostThread> thread=new ArrayList<>();

    private CommunityHomePage communityHomePage;

    private String communityServerDomain;

    private String urlCommunityLoginPage;

    private String urlCommunityHomePage;

    private String urlCommunityLogoutPath;

    private File testDataFolder;

    private List<Map<String,?>> data;
    private int getTestDuration(){
        return (int)((this.testEndTimeStamp-this.testStartTimeStamp)/1000);
    }
    private double getAvgTimeInEachThread(double totalAvgTimeInEachThread){
        return totalAvgTimeInEachThread/this.total;
    }
    private float getTps(){
        return this.total/this.testDurationInSec;
    }

    private float getQps(){
        return this.passed/this.testDurationInSec;
    }

    private float getErrorRatePercentage(){
        return 100* this.failed/this.total;
    }

    private void counterAvgTime(double durationInThread){
        this.avgTimeInEachThread+=durationInThread;
    }
    private void setupBrowsers(){
      for(int i=0; i<this.data.size(); i++){
          MyWebDriver myWebDriver=new MyWebDriver();
          myWebDriver.setupEdgeBrowser();
          this.myBrowsers.add(myWebDriver);
      }
    }
    private void counterPassed(boolean res){
        if(res){
            this.passed++;
        }
    }
    private void counterFailed(boolean res){
        if(!res){
            this.failed++;
        }
    }


    @BeforeTest
    public void init(ITestContext context) throws Exception{
        this.communityServerDomain=context.getCurrentXmlTest().
                getParameter("COMMUNITY_SERVER_DOMAIN");
        this.urlCommunityHomePage=this.communityServerDomain
                +context.getCurrentXmlTest().getParameter("COMMUNITY_HOME_PATH");
        this.urlCommunityLoginPage=this.communityServerDomain
                +context.getCurrentXmlTest().getParameter("COMMUNITY_LOGIN_PATH");
        this.urlCommunityLogoutPath=context.getCurrentXmlTest().getParameter("COMMUNITY_LOGOUT_PATH");
        this.testDataFolder =new File(System.getProperty("user.dir")+File.separator
                +context.getCurrentXmlTest().getParameter("TEST_DATA_PATH"));
        this.data= FileUtility.getDataFromCSV(this.testDataFolder.getCanonicalPath()+File.separator
                +context.getCurrentXmlTest().getParameter("PERF_TEST_DATA_FILE"));
        this.setupBrowsers();
    }





    @Test
    public void verifyParallelingUserPublishingPost() throws Exception{
        double totalAVGTimeInAllThreads=0L;
        this.testStart=DateTimeUtility.getSystemCurrentDateTime("yyyy-MM-dd HH:mm:ss");
        this.testStartTimeStamp=DateTimeUtility.getSystemCurrentTimestamp();
        for(int i=0;i<this.data.size();i++){
            PublishPostThread thread= new PublishPostThread(this.data.get(i),
                    this.myBrowsers.get(i));
            thread.setCommunityServerDomain(this.communityServerDomain);
            thread.setUrlCommunityHomePage(this.urlCommunityHomePage);
            thread.setUrlCommunityLoginPage(this.urlCommunityLoginPage);
            thread.setUrlCommunityLogoutPath(this.urlCommunityLogoutPath);
            this.thread.add(thread);
            thread.run();

        }
        for(PublishPostThread t:this.thread){
            t.join();
            this.counterFailed(t.getResult());
            this.counterPassed(t.getResult());
            totalAVGTimeInAllThreads+=t.getTestThreadDuration();
        }
        this.testEnd=DateTimeUtility.getSystemCurrentDateTime("yyyy-MM-dd HH:mm:ss");
        this.total=this.thread.size();
        this.testEndTimeStamp=DateTimeUtility.getSystemCurrentTimestamp();
        this.testDurationInSec=this.getTestDuration();
        this.tps=this.getTps();
        this.qps=this.getQps();
        this.errorRatePercentage=this.getErrorRatePercentage();
        this.avgTimeInEachThread=this.getAvgTimeInEachThread(totalAVGTimeInAllThreads);
    }

    @AfterMethod
    public void printTestSummary(){
        System.out.println("============Perform Test Summary===========");
        System.out.println("Test Start At: "+this.testStart);
        System.out.println("Test End At: "+this.testEnd);
        System.out.println("Total Time Duration in Test: "+ this.testDurationInSec +" seconds");
        System.out.println("Total: "+this.total);
        System.out.println("Passed: " +this.passed);
        System.out.println("Failed: "+this.failed);
        System.out.println("Error Rate: "+this.errorRatePercentage+"%");
        System.out.println("Qps: "+this.qps);
        System.out.println("Tps: "+this.tps);
        System.out.println("AVG Time in Each Thread: "+this.avgTimeInEachThread+" ms");

    }
    @AfterTest
    public void tearDown(){
        for(MyWebDriver driver:this.myBrowsers){
           driver.tearDown();
        }
    }

}
