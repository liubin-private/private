package PO;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BaiduImageSearchPage extends BasePage{

    @FindBy(id = "sttb")
    private WebElement swticher=null;
    public BaiduImageSearchPage(String url){
        super(url);
    }

    public void uploadImage(String imagePath,WebDriver driver){
        WebElement imageUploader;
        if(this.swticher==null){
            return;
        }
        this.swticher.click();
        imageUploader=driver.findElement(By.id("stfile"));
        imageUploader.sendKeys(imagePath);

    }

}
