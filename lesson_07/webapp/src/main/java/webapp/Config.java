package webapp;

import webapp.storage.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.LogManager;


/**
 * User: gkislin
 * Date: 26.02.14
 */
public class Config {
    public static final String DIR_STORAGE;
    public static final IStorage DS_STORAGE = new DataStreamStorage(DIR_STORAGE);
    public static final IStorage SER_STORAGE = new SerializeStorage(DIR_STORAGE);
    public static final IStorage XML_STORAGE = new XmlStorage(DIR_STORAGE);
    public static final String DB_URL, DB_USER, DB_PASSWORD;
    public static final IStorage SQL_STORAGE = new SqlStorage();

    static {
        String webappRoot = null; //System.getenv("WEBAPP_ROOT");
        try {
            webappRoot = new File("C:\\Users\\gtee\\Desktop\\java\\lesson_09\\webapp").getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (webappRoot == null) {
            throw new IllegalStateException("Define environment variable WEBAPP_ROOT");
        }
        File webappRootDir = new File(webappRoot);
        Properties props = new Properties();
        try (FileInputStream webappProps = new FileInputStream(new File(webappRootDir, "config/webapp.properties"));
             FileInputStream logProps = new FileInputStream(new File(webappRootDir, "config/logging.properties"))) {

            LogManager.getLogManager().readConfiguration(logProps);

            props.load(webappProps);
            DIR_STORAGE = props.getProperty("dir.storage");
            DB_URL = props.getProperty("db.url");
            DB_USER = props.getProperty("db.user");
            DB_PASSWORD = props.getProperty("db.password");
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static IStorage getStorage() {
        return SQL_STORAGE;
    }
}
