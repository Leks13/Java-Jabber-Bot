package jabberbot;

import java.io.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.Properties;

public class Log {

    public static void addToLog(String note) throws IOException {
        Properties prop = new Properties();
        String fileName = "config.cfg";
        InputStream is = new FileInputStream(fileName);
        prop.load(is);
        String logConf = prop.getProperty("log", "on");
        Boolean loggingOn = false;
        if (logConf.equals("on")) {
            loggingOn = true;
        }
        if (loggingOn) {
            try {
                FileWriter fstream = new FileWriter("log/main.log", true);
                BufferedWriter out = new BufferedWriter(fstream);
                out.write(java.util.Calendar.getInstance().getTime() + " - " + note + "\n");
                out.close();
            } catch (FileNotFoundException ex) {
                File f = new File("log/");
                f.mkdirs();
            }
        }
    }

    public static void addToLogMuc(String note, String muc) throws IOException {
        Properties prop = new Properties();
        String fileName = "config.cfg";
        InputStream is = new FileInputStream(fileName);
        prop.load(is);
        String logConf = prop.getProperty("logMUC", "on");
        Boolean loggingOn = false;
        if (logConf.equals("on")) {
            loggingOn = true;
        }
        if (loggingOn) {
            DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT);
            Date aDate = new Date();
            String formattedDate = dateFormatter.format(aDate);

            try {
                FileWriter fstream2 = new FileWriter("log/muc/" + formattedDate + "/" + muc + ".log", true);
                BufferedWriter out2 = new BufferedWriter(fstream2);
                Date now = new Date();
                note = note.replaceAll(muc + "/", "");
                String line = DateFormat.getTimeInstance().format(now) + " - " + note + "\n";
                out2.write(line);
                out2.close();
            } catch (FileNotFoundException ex1) {
                File f = new File("log/muc/" + formattedDate + "/");
                f.mkdirs();
            }
        }
    }
}
