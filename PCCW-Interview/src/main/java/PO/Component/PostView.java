package PO.Component;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;

import java.util.List;

public class PostView {


    private String clsDivTextContent="text";
    private String clsImageViewer="ImageViewer";
    private WebElement divTextContent;
    private WebElement divPost;


    private WebElement divImageViewer;

    private WebElement image;

    public PostView(WebElement divPost){
        this.divPost=divPost;
    }

    public void onLoad(WebDriverWait wait){
        List<WebElement> divTags=this.divPost.findElements(By.tagName("div"));
        for(WebElement divTag:divTags){
            if(divTag.getAttribute("class").contains(this.clsDivTextContent)){
                this.divTextContent=divTag;
            }
            if(divTag.getAttribute("class").equals(this.clsImageViewer)){
                this.divImageViewer=divTag;
                this.image=this.divImageViewer.findElement(By.tagName("img"));
            }
        }
    }

    public String getText(){
       return this.divTextContent!=null?this.divTextContent.getAttribute("innerText").toString():"";
    }

    public WebElement getImage() {
        return this.image;
    }
}
