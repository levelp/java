package webapp.storage;

/**
 * User: gkislin
 * Date: 18.04.2014
 */
public class DataStreamStorageTest extends StorageTest {
    static {
        storage = new DataStreamStorage(STORAGE_DIR);
    }
}
