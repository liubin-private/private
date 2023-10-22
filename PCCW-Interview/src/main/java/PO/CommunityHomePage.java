package PO;

import PO.Component.PostView;
import PO.Component.PostEditor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class CommunityHomePage extends BasePage{

    @FindBy(name="msg")
    private WebElement btnCreatePost;
    @FindBy(className = "Posts")
    //@CacheLookup
    private  WebElement divPosts;
    @FindBy(className = "Post")
    private List<WebElement> divsPost;
    private PostEditor postEditor;
    private String textEditorClassName="TextEditor";
    private String emptyPostsClassName="EmptyState";
    private String postClassName="Post";
    private List<PostView> postViews;
    public CommunityHomePage(String url){
        super(url);
    }

    public void showEditor(WebDriver driver, WebDriverWait wait) throws Exception{
        this.btnCreatePost.click();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.className(this.textEditorClassName))));
        this.postEditor =new PostEditor(driver);
    }
    public void publishTextPost(String content){
        this.postEditor.inputContent(content);
        this.postEditor.publish();
    }
    public void publishPost(String textContent,String imageFile,WebDriver driver,WebDriverWait wait){
        if(!textContent.equals("")){
            this.postEditor.inputContent(textContent);
        }
        if(!imageFile.equals("")){
            this.postEditor.showImageUploader(driver,wait);
            this.postEditor.uploadImage(imageFile);
            this.postEditor.waitImageUploading(driver,wait);
        }
        this.postEditor.publish(wait);
    }
    public void inputPostContentInEditor(String content){
        this.postEditor.inputContent(content);
    }
    public boolean isEmptyStatusInPostList(WebDriverWait wait){
        try {
            wait.until(ExpectedConditions.visibilityOf(
                    this.divPosts.findElement(By.className(this.emptyPostsClassName))));
            return true;
        }
        catch (Exception e){
            return false;
        }
    }


    public boolean isNotEmptyStatusInPostList(WebDriverWait wait){
        try {
            wait.until(ExpectedConditions.visibilityOf(
                    this.divPosts.findElement(By.className(this.postClassName))));
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public void iniPosts(WebDriverWait wait){
        this.postViews =new ArrayList<>();
        System.out.println("total: \t"+this.divsPost.size()+"\t posts");
        for(WebElement postDiv:this.divsPost){
            PostView postView =new PostView(postDiv);
            postView.onLoad(wait);
           this.postViews.add(postView);
        }
    }
    public List<PostView> getPosts(){
        return this.postViews;
    }

    public boolean isEmptyInPostEditor(WebDriver driver,WebDriverWait wait){
        try{
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(
                    By.className(this.textEditorClassName))));
           // this.postEditor.refresh(driver);
            if(this.postEditor.isPublishingButtonDisabled()
                    &&this.postEditor.isEmptyContentInputted()){
                return true;
            }
            return false;

        }
        catch (Exception e){
            return false;
        }
    }
    public void closePostEditorWithoutPublishing(){
        this.postEditor.close();
    }
    public void onLoad(WebDriver driver){
        PageFactory.initElements(new AjaxElementLocatorFactory(driver,10),this);
    }




}
