package webapp.storage;

/**
 * User: gkislin
 * Date: 25.04.2014
 */
public class XmlStorageTest extends StorageTest {
    static {
        storage = new XmlStorage(STORAGE_DIR);
    }
}
