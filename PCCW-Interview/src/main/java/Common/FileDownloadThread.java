package Common;

import java.io.File;

public class FileDownloadThread extends Thread{
    private String fileUrl;
    private String desDirectory;
    private File file;

    private String fileName;
    public void setFileUrl(String fileUrl){
        this.fileUrl=fileUrl;
    }
    public void setFileName(String fileName){
        this.fileName=fileName;

    }
    public void setDesDirectory(String desDirectory){
        this.desDirectory=desDirectory;
    }
    public File getFileDownloaded(){
        return this.file;
    }
    public void run() {
        try{
            this.file=FileUtility.downloadFile(this.fileUrl,this.desDirectory,this.fileName);
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }
}
