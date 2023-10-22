package PO;

import PO.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

public class BaiduImageSearchedResultDetailsPage extends BasePage {
    @FindBys({
            @FindBy(className = "page-similar-big-cont"),
            @FindBy(tagName = "img")
    })
    private WebElement image;

    public WebElement getImage() {
        return image;
    }
    public String getImageUrl(){
        return this.image.getAttribute("src");
    }
}
