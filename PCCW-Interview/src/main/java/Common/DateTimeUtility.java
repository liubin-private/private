package Common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtility {

    public static String getSystemCurrentDateTime(String formateString) {
        String dateTime="";
        DateFormat df=new SimpleDateFormat(formateString);
        dateTime=df.format(new Date());
        return dateTime;
    }
    public static double getSystemCurrentTimestamp(){
        return System.currentTimeMillis();
    }
}
