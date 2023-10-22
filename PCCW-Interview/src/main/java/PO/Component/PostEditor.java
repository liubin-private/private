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

public class PostEditor {


   @FindBy(className="public-DraftEditor-content")
   private WebElement draftTextContent;

   @FindBys({
           @FindBy(className = "buttons-div"),
           @FindBy(className = "con-button")
   })
    private  WebElement btnPublish;
   @FindBy(xpath = "/html/body/div[2]/div[2]/div/div/div[3]/div[1]/span[2]")
   private WebElement btnUploadImage;
   @FindBy(className = "close-button")
   private WebElement btnClose;

   private WebElement divImagePreview;
   private WebElement imageUploader;

   public PostEditor(WebDriver driver){
       PageFactory.initElements(new AjaxElementLocatorFactory(driver,1),this);
   }

   public boolean isEmptyContentInputted(){
       return this.draftTextContent.getAttribute("innerText").trim().length()==0?true:false;
   }

   public void refresh(WebDriver driver){
       PageFactory.initElements(new AjaxElementLocatorFactory(driver,1),this);
   }
   public void inputContent(String content){
       this.draftTextContent.sendKeys(content);
   }
   public void publish(){
       if(!this.btnPublish.getAttribute("class").toString().trim().contains("disabled")){
           this.btnPublish.click();
       }

   }

   public void publish(WebDriverWait wait){
       wait.until(ExpectedConditions.not(ExpectedConditions.attributeContains(
               this.btnPublish,"class","disabled"
       )));
       this.btnPublish.click();
   }

   public void close(){
       this.btnClose.click();
   }

   public boolean isPublishingButtonDisabled(){
       //System.out.println(this.btnPublish.getAttribute("class"));
       return this.btnPublish.getAttribute("class").contains("disabled")?true:false;
   }

   public void showImageUploader(WebDriver driver,WebDriverWait wait){
       this.btnUploadImage.click();
       wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.className("ImageUpload"))));
      // wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.name("file-upload"))));
       this.divImagePreview= driver.findElement(By.className("ImageUpload"));
       wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(this.divImagePreview,By.name("file-upload")));
       //this.imageUploader=driver.findElement(By.name("file-upload"));
       this.imageUploader=this.divImagePreview.findElement(By.name("file-upload"));
   }
   public void uploadImage(String fileName){
       this.imageUploader.sendKeys(fileName);
   }
   public void waitImageUploading(WebDriver driver,WebDriverWait wait){

       try{
           wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.className("ImageUpload"))));
           this.divImagePreview= driver.findElement(By.className("ImageUpload"));
           wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(this.divImagePreview,By.tagName("img")));
       }
       catch (Exception e){

       }

   }
}
