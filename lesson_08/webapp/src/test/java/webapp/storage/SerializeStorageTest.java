package webapp.storage;

/**
 * User: gkislin
 * Date: 18.04.2014
 */
public class SerializeStorageTest extends StorageTest {
    static {
        storage = new SerializeStorage("D:\\doc\\gkislin\\project\\webapp4\\file_storage");
    }
}
