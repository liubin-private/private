package PO.Component;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class BaiduImageThumbnail {
      private  WebElement image;
      private WebElement imageDetailsPageAnchorLink;
      private int rowIndex;
      private int colIndex;
    public BaiduImageThumbnail(WebElement image, WebElement imageDetailsPageAnchorLink,int rowIndex, int colIndex){
        this.image =image;
        this.imageDetailsPageAnchorLink=imageDetailsPageAnchorLink;
        this.rowIndex=rowIndex;
        this.colIndex=colIndex;
    }
    public void setImage(WebElement image){
        this.image = image;
    }
    public void setImageDetailsPageAnchorLink(WebElement imageAnchorLink){
        this.imageDetailsPageAnchorLink=imageAnchorLink;
    }
    public void setRowIndex(int rowIndex){
        this.rowIndex=rowIndex;
    }
    public void setColIndex(int colIndex){
        this.colIndex=colIndex;
    }

    public WebElement getImage(){
        return this.image;
    }
    public WebElement getImageDetailsPageAnchorLink(){
        return this.imageDetailsPageAnchorLink;
    }
    public int getRowIndex(){
        return this.rowIndex;

    }
    public int getColIndex(){
        return this.colIndex;
    }

    public void onClick(WebDriver driver){
        driver.get(this.imageDetailsPageAnchorLink.getAttribute("href"));
    }
    public String getImageUrl(){
        return this.image.getAttribute("src");
    }

}
