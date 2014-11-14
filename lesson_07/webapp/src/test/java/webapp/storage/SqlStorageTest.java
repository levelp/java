package webapp.storage;

/**
 * User: gkislin
 * Date: 18.04.2014
 */
public class SqlStorageTest extends StorageTest {
    static {
        storage = new SqlStorage();
    }
}
