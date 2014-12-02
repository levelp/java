package webapp.storage;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import webapp.WebAppException;
import webapp.model.ContactType;
import webapp.model.Resume;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * User: gkislin
 * Date: 18.04.2014
 */
public class StorageTest {

    static IStorage storage;
    public static final String STORAGE_DIR = ".\\file_storage";

    private static Resume R1, R2, R3;

    @BeforeClass
    public static void beforeClass() {
        R1 = new Resume("Полное Имя1", "location1");
        R1.addContact(ContactType.MAIL, "mail1@ya.ru");
        R1.addContact(ContactType.PHONE, "11111");

/*

        R1.addSection(SectionType.ACHIEVEMENT, "Achivment11", "Achivment12");
        R1.addSection(SectionType.OBJECTIVE, "Objective1");
        R1.addSection(SectionType.EXPERIENCE,
                new Organization("Organization11", null,
                        new Period(2005, Calendar.JANUARY, 2008, Calendar.DECEMBER, "position1", "content1"),
                        new Period(2001, Calendar.MARCH, 2005, Calendar.JANUARY,"position2", "content2")),
                new Organization("Organization12", "Url11"));
*/

        R2 = new Resume("Полное Имя2", null);
        R2.addContact(ContactType.SKYPE, "skype2");
        R2.addContact(ContactType.PHONE, "22222");
        R3 = new Resume("Полное Имя3", null);
    }

    @Before
    public void before() {
        // Execute before every test
        // Tests order is random
        storage.clear();
        storage.save(R1);
        storage.save(R2);
        storage.save(R3);
    }

    @Test
    public void testClear() throws Exception {
        storage.clear();
        assertEquals(0, storage.size());
    }

    @Test
    public void testLoad() throws Exception {
        assertEquals(R1, storage.load(R1.getUuid()));
        assertEquals(R2, storage.load(R2.getUuid()));
        assertEquals(R3, storage.load(R3.getUuid()));
    }

    @Test
    public void testDelete() throws Exception {
        storage.delete(R2.getUuid());
        assertEquals(2, storage.size());
        storage.delete(R1.getUuid());
        assertEquals(1, storage.size());
        storage.delete(R3.getUuid());
        assertEquals(0, storage.size());
    }

    @Test(expected = WebAppException.class)
    public void testDeleteMissed() throws Exception {
        storage.delete("dummy");
    }

    @Test(expected = WebAppException.class)
    public void testSavePresented() throws Exception {
        storage.save(R1);
    }

    @Test(expected = WebAppException.class)
    public void testUpdateMissed() throws Exception {
        Resume resume = new Resume("dummy", "fullName_U1", "location_U1");
        storage.update(resume);
    }

    @Test
    public void testUpdate() throws Exception {
        Resume resume = new Resume(R2.getUuid(), "fullName_U2", "location_U2");
        storage.update(resume);
        assertEquals(resume, storage.load(R2.getUuid()));
    }

    @Test
    public void testGetAll() throws Exception {
        Object[] src = new Object[]{R1, R2, R3};
        Arrays.sort(src);
        assertArrayEquals(src, storage.getAllSorted().toArray());
    }
}
