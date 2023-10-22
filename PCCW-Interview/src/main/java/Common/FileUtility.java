package Common;



import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;

import org.apache.commons.csv.CSVRecord;

public class FileUtility {

    public static void makeDir(File directory){

       if(!directory.exists()){
           directory.mkdir();
       }
       System.out.println(directory.getAbsolutePath());
    }


    public static File downloadFile(String sourceUrl, String desPath, String name) throws Exception {

        System.out.println("Downloading start:"+sourceUrl);
        URL url = new URL(sourceUrl);
        URLConnection connection = url.openConnection();
        File dir = new File(desPath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File download=new File(dir.getCanonicalPath(),name);
        download.createNewFile();
        InputStream input = connection.getInputStream();
        OutputStream output = new FileOutputStream(download);
        byte[] buf = new byte[4096];
        int n;
        while ((n = input.read(buf)) >= 0) {
            output.write(buf, 0, n);
        }
        System.out.println("Downloading end:"+sourceUrl);

        return download;
    }
    public static void copyFile(File sourceFile,String desPath) throws IOException {
        String sourceFileName=sourceFile.getName();
        File destFile=new File(desPath+File.pathSeparator+sourceFileName);
        Files.copy(sourceFile.toPath(),destFile.toPath());
    }

    public static Object getDataFromYaml(String yamlFile) throws Exception{
        Yaml yaml = new Yaml();
        FileInputStream fileInputStream = new FileInputStream(yamlFile);
        Object object=yaml.load(fileInputStream);
        return object;
    }

    public static List<Map<String,?>> getDataFromCSV(String csvFile) throws Exception{
        List<Map<String,?>> list=new ArrayList<>();

        CSVParser parser = CSVParser.parse(new File(csvFile),
                Charset.forName("UTF-8"),
                CSVFormat.DEFAULT.withHeader());
       for(CSVRecord record : parser.getRecords()){
           Map<String,String> map=new TreeMap<>();
           for(int k=0;k<record.size(); k++){
              map.put(parser.getHeaderNames().get(k).toString(),record.get(k));
           }
           list.add(map);
       }
        return list;
    }

    public static boolean isFileExisted(String filename){
        File file=new File(filename);
        if(file.exists()){
            return true;
        }
        else{
            return false;
        }
    }

    public static void main(String args[]) throws Exception{
        List<Map<String,?>> lst=FileUtility.getDataFromCSV(
                "/Users/binsmba/IdeaProjects/PCCW-Interview/data/Performance-Test.csv");
        System.out.println(lst.size());
    }




    }


