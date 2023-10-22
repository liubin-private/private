package PO;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CommunityLoginPage extends BasePage{

    @FindBy(name="email")
    WebElement inputEmail;
    @FindBy(name="password")
    WebElement inputPassword;
    @FindBy(tagName = "button")
    WebElement btnLogin;

    public CommunityLoginPage(String url){
        super(url);
    }

    public void userLogin(String emailAddress, String password){
        this.inputEmail.clear();
        this.inputEmail.sendKeys(emailAddress);
        this.inputPassword.clear();
        this.inputPassword.sendKeys(password);
        this.btnLogin.click();
    }

}
