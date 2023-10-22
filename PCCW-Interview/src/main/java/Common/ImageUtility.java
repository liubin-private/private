package Common;

import org.opencv.core.*;

import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import org.opencv.features2d.DescriptorMatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class ImageUtility {

    public static boolean isImagesMatching(String baseImage, String imageTest){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat baseSource = Imgcodecs.imread(baseImage);
        Mat testSource = Imgcodecs.imread(imageTest);

        Mat grayBase = new Mat();
        Mat grayTest = new Mat();
        Imgproc.cvtColor(baseSource, grayBase, Imgproc.COLOR_RGB2GRAY);
        Imgproc.cvtColor(testSource, grayTest, Imgproc.COLOR_RGB2GRAY);

        //convert it to Gray
        Imgproc.cvtColor(baseSource, grayBase, Imgproc.COLOR_RGB2GRAY);
        Imgproc.cvtColor(testSource, grayTest, Imgproc.COLOR_RGB2GRAY);

        //convert to ORB description
        FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.ORB);
        DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.ORB);
        //Keypoints with its description
        MatOfKeyPoint srouceKeyPoint = new MatOfKeyPoint();
        MatOfKeyPoint testKeyPoint = new MatOfKeyPoint();
        Mat descriptorSource = new Mat();
        Mat descriptorTest = new Mat();
        //calc keypoints
        featureDetector.detect(grayBase, srouceKeyPoint);
        featureDetector.detect(grayTest, testKeyPoint);
        //calc matrix for description of keypoints
        descriptorExtractor.compute(grayBase, srouceKeyPoint, descriptorSource);
        descriptorExtractor.compute(grayTest, testKeyPoint, descriptorTest);
        if(srouceKeyPoint.size().empty()){
            return false;
        }
        if(testKeyPoint.size().empty()){
            return false;
        }
        float result=0;
        double minDistance=100;
        DescriptorMatcher descMatcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_L1);
        MatOfDMatch matches = new MatOfDMatch();
        descMatcher.match(descriptorSource, descriptorTest, matches);
        DMatch[] dMatchs = matches.toArray();
        int numbersOfMatching = 0;
        for(DMatch match:dMatchs){
            float distance = match.distance;
            if (distance<=2*minDistance){
                result=result+distance*distance;
                numbersOfMatching++;
            }
        }
        result=result/numbersOfMatching;
        if(Float.isNaN(result)){
            return false;
        }
        return true;
    }
    public static void saveScreenshotOnWeb(WebDriver driver, File screenshotPath) {
        File screenshotOnWeb=new File(screenshotPath.toPath()
                +File.separator+DateTimeUtility.getSystemCurrentDateTime("yyyy-MM-dd HHMMss")+".png");
        File temp=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        temp.renameTo(screenshotOnWeb);
    }

}
