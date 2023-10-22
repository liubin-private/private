package PO.Component;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;


public class UserOnboardingModal {
    @FindBy(className = "skip-button")
    private WebElement btnSkip;
    public UserOnboardingModal(WebDriver driver){
        PageFactory.initElements(new AjaxElementLocatorFactory(driver,2),this);
    }
    public void skip(){
        this.btnSkip.click();
    }
}
