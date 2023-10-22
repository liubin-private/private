package PO;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class BasePage {
    protected String url="";




    public BasePage(String url){
        this.url=url;

    }
    public BasePage(){

    }

    public void onLoad(WebDriver driver){
        if(!"".equals(this.url)) {
            driver.get(this.url);
        }
        PageFactory.initElements(new AjaxElementLocatorFactory(driver,2),this);
    }


}
