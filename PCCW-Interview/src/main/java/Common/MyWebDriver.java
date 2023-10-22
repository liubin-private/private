package Common;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class MyWebDriver {
    private WebDriver driver;
    private WebDriverWait wait;

    public MyWebDriver(WebDriverWait wait){
        this.wait=wait;
    }
    public MyWebDriver(){

    }

    public void setupWait(int timeoutInMill){
        this.wait=new WebDriverWait(this.driver,Duration.ofMillis(timeoutInMill));
    }
    public WebDriverWait getWebDriverWait(){
        return this.wait;
    }


    public void setupEdgeBrowser(){
        EdgeOptions options=new EdgeOptions();
        options.addArguments("--guest");
        this.driver=new EdgeDriver(options);

    }

    public void setupEdgeBrowser(String args){
        EdgeOptions options=new EdgeOptions();
        options.addArguments(args);
        this.driver=new EdgeDriver((options));
    }
    public void setupChromeBrowser(){
        this.driver = new ChromeDriver();
    }
    public void setupSafariBrowser(){
        this.driver =new SafariDriver();
    }
    public WebDriver getWebDriver(){
        return this.driver;
    }
    public void setDriverProperties(){
       this.driver.manage().window().maximize();
       this.driver.manage().deleteAllCookies();
       

    }
    public void tearDown(){
        this.driver.close();
        this.driver.quit();
    }
    public String getCurrentUrl(){
        return this.driver.getCurrentUrl();
    }
    public boolean isPageRedirected(String newUrl){
        boolean isRedirected=false;
        if(null==this.wait){
            this.wait=new WebDriverWait(this.driver, Duration.ofSeconds(5));
            System.out.println("new wait");

        }
        try{
            System.out.println("Old page url:"+this.driver.getCurrentUrl());
            wait.until(
                    ExpectedConditions.urlContains(newUrl));
            System.out.println("new page url:"+this.driver.getCurrentUrl());
            isRedirected=true;
        }
        catch(Exception e){
            System.out.println("page is not redirected");
            System.out.println("current page url:"+this.driver.getCurrentUrl());

        }
        return isRedirected;



    }

    public void scrollToBottom(){
        JavascriptExecutor jsDriver=(JavascriptExecutor)this.driver;

        jsDriver.executeAsyncScript("window.scrollTo(0, document.body.scrollHeight);");
    }
    public void lazyLoading(){
        JavascriptExecutor jsDriver=(JavascriptExecutor)this.driver;
        this.wait=new WebDriverWait(this.driver,Duration.ofSeconds(10));


        while(true){
            long scrollbarLastPosition=(long) jsDriver.executeScript("return document.body.scrollHeight;");
            System.out.println("old scrollbar:"+scrollbarLastPosition);
            jsDriver.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            try{
                Thread.sleep(1000);
            }
            catch (Exception e){
            }
            long scrollbarNewPosition=(long) jsDriver.executeScript("return document.body.scrollHeight;");
            System.out.println("new scrollbar:"+scrollbarNewPosition);

            if(scrollbarLastPosition==scrollbarNewPosition){
                return;
            }
        }


    }

    public void refresh(){
        this.driver.navigate().refresh();
    }

    public void openInNewWindow(String url){
        this.driver.switchTo().newWindow(WindowType.WINDOW);
        this.driver.get(url);
    }
    public void openInNewTab(String url){
        this.driver.switchTo().newWindow(WindowType.TAB);
        this.driver.get(url);

    }
    public boolean isShowingUpAlert(){
        try{
            this.wait.until(ExpectedConditions.alertIsPresent());
            return true;
        }
        catch (Exception e){
            return false;
        }

    }
    public void acceptInAlert(){
       Alert alert= this.driver.switchTo().alert();
       System.out.println("Text in Alert:"+alert.getText() );
       alert.accept();
       System.out.println("Accept in Alert");
    }
    public void inputInAlert(String content){
        Alert alert= this.driver.switchTo().alert();
        System.out.println("Text in Alert:"+alert.getText() );
        alert.sendKeys(content);
        alert.accept();
    }
    public static void main(String args[]){
        MyWebDriver driver=new MyWebDriver();
        driver.setupEdgeBrowser("--headless");
        driver.getWebDriver().get("https://www.baidu.com");
        System.out.println(driver.getWebDriver().getCurrentUrl());
    }
}
