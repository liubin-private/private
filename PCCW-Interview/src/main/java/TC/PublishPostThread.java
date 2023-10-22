package TC;

import Common.DateTimeUtility;
import Common.MyWebDriver;
import PO.CommunityHomePage;
import PO.CommunityLoginPage;

import java.util.Map;

public class PublishPostThread extends Thread{
    private double testStartTimeStamp=0L;
    private double testEndTimeStamp=0L;
    private double testThreadDurationInSec=0;

    private MyWebDriver browser;
    private String communityServerDomain;

    private String urlCommunityLoginPage;

    private String urlCommunityHomePage;

    private String urlCommunityLogoutPath;

    private CommunityHomePage communityHomePage;

    private boolean res=false;

    //private Map<String,?> data;
    private String emailAddress="";
    private String password="";

    private String postContent="";

    public PublishPostThread(Map<String,?> data,MyWebDriver browser){
        super();
        this.emailAddress=String.valueOf(data.get("emailAddress"));
        this.password=String.valueOf(data.get("password"));
        this.postContent=String.valueOf(data.get("content"));
        this.browser = browser;
    }
    public void setUrlCommunityLoginPage(String urlCommunityLoginPage){
        this.urlCommunityLoginPage=urlCommunityLoginPage;
    }
    public void setUrlCommunityHomePage(String urlCommunityHomePage){
        this.urlCommunityHomePage=urlCommunityHomePage;
    }
    public void setCommunityServerDomain(String serverDomain){
        this.communityServerDomain=serverDomain;
    }
    public void setUrlCommunityLogoutPath(String logoutPath){
        this.urlCommunityLogoutPath=logoutPath;
    }
    @Override
    public void run() {

        System.out.println("Thread Started at:"+
                DateTimeUtility.getSystemCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
        this.testStartTimeStamp=DateTimeUtility.getSystemCurrentTimestamp();
        try{
            this.userLoginToCommunity();
            this.publishPost();

            this.res=true;

        }
        catch (Exception e){
            e.printStackTrace();
            this.res=false;
        }
        this.testEndTimeStamp=DateTimeUtility.getSystemCurrentTimestamp();
        this.testThreadDurationInSec=this.testEndTimeStamp-this.testStartTimeStamp;
        System.out.println("Thread Started End:"+
                DateTimeUtility.getSystemCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
    }
    public boolean getResult(){
        return this.res;
    }
    public double getTestThreadDuration(){
        return this.testThreadDurationInSec;
    }
    public void setBrowser(MyWebDriver browser){
        this.browser =browser;
    }


    private void userLoginToCommunity(){
        this.communityHomePage=this.getCommunityHomePageByUserLogin(this.emailAddress,this.password);
    }
    private CommunityHomePage getCommunityHomePageByUserLogin(String emailAddress, String password){
        CommunityHomePage communityHomePage = null;
        CommunityLoginPage userLoginPage=null;
        this.browser.getWebDriver().get(this.urlCommunityHomePage);
        if(this.browser.isPageRedirected(this.urlCommunityLoginPage)){
            userLoginPage=new CommunityLoginPage(this.urlCommunityLoginPage);
            userLoginPage.onLoad(this.browser.getWebDriver());
            userLoginPage.userLogin(emailAddress,password);
        }
        if(this.browser.isPageRedirected("home")){
            this.browser.getWebDriver().get(this.urlCommunityHomePage);
        }
        communityHomePage=new CommunityHomePage(this.urlCommunityHomePage);
        communityHomePage.onLoad(this.browser.getWebDriver());
        return communityHomePage;
    }
    private void publishPost() throws Exception{
        System.out.println("Creating a Post Start");
        this.communityHomePage.showEditor(this.browser.getWebDriver(),
                this.browser.getWebDriverWait());
        this.communityHomePage.publishPost(this.postContent,"",this.browser.getWebDriver(),
                this.browser.getWebDriverWait());
        this.communityHomePage.onLoad(this.browser.getWebDriver());
        System.out.println("Creating a Post End");
    }


}

