package PO;

import PO.Component.BaiduImageThumbnail;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class BaiduImageSearchResultPage extends BasePage{





    private List<BaiduImageThumbnail> imageThumbnails=new ArrayList<>();
    @FindBy(className="graph-container")
    private WebElement graghContainer;


    public BaiduImageSearchResultPage(String url){
        super(url);
    }
    public BaiduImageSearchResultPage(){
        super();
    }



    public boolean isEmptyResultSearched(WebDriverWait wait){
       // System.out.println(this.graghContainer.getAttribute("class"));
    try{

        if(this.graghContainer.getAttribute("class").contains("noresult")){
            return true;
        }
        else{
            wait.until(ExpectedConditions.visibilityOf(
                    this.graghContainer.findElement(By.className("graph-similar-list"))));
        }
    }
    catch (Exception e){

    }

       return false;
    }


    public List<String> getSimilarImageUrls(){
       List<String> similarImageUrls=new ArrayList<>();
        for(BaiduImageThumbnail imageThumbnail:this.imageThumbnails){
            String imageUrl=imageThumbnail.getImage().getAttribute("src");
            similarImageUrls.add(imageUrl);
        }
        return similarImageUrls;
    }
    public void initiateSimilarImageSearchedThumbnails(WebDriver driver, WebDriverWait wait){
        wait.until(ExpectedConditions.visibilityOf(
                driver.findElement(By.className("general-waterfall"))));
        WebElement divTag=driver.findElement(
                By.className("general-waterfall"));
        wait.until(ExpectedConditions.visibilityOf(
                divTag.findElement(By.className("general-imgcol"))));
        List<WebElement> imageCols=divTag.findElements(By.className("general-imgcol"));
        System.out.println("total columns:"+imageCols.size());
        for(int c=0; c<imageCols.size();c++){

          WebElement imageCol=imageCols.get(c);
          List<WebElement> imageAnchorLinks=imageCol.findElements(By.tagName("a"));
          System.out.println("Column "+c+" total images:"+imageAnchorLinks.size());
          for(int r=0;r<imageAnchorLinks.size();r++){
             WebElement imageAnchorLink=imageAnchorLinks.get(r);
             WebElement image=imageAnchorLink.findElement(By.tagName("img"));
             BaiduImageThumbnail thumbnail=new BaiduImageThumbnail(image,imageAnchorLink,r,c);
             this.imageThumbnails.add(thumbnail);
          }

        }
    }



    public String getImageDetailsPageLink(int i){
       return this.imageThumbnails.get(i).
               getImageDetailsPageAnchorLink().getAttribute("href");
    }

    public String getImageDetailsPageLink(int r,int c){
        for(BaiduImageThumbnail thumbnail:this.imageThumbnails){
            System.out.println(thumbnail.getImageDetailsPageAnchorLink().getAttribute("href").toString());
            if(thumbnail.getColIndex()==c
                    && thumbnail.getRowIndex()==r){
                return thumbnail.getImageDetailsPageAnchorLink().getAttribute("href").toString();
            }
        }
        return this.url;

    }


    public BaiduImageThumbnail getThumbnailByIndex(int r,int c){

        for(BaiduImageThumbnail thumbnail:this.imageThumbnails){
            if(thumbnail.getColIndex()==c
                    && thumbnail.getRowIndex()==r){
                return thumbnail;
            }
        }
        return null;

    }


}
