import dao.FileStorage;
import dao.Storage;
import model.Resume;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 *
 */
public class FileStorageTest {

    @Test
    public void testResume() throws Exception {
        Storage<Resume> storage = new FileStorage<Resume>(Resume.class);
        Resume resume = new Resume("Имя");
        storage.save(resume);

        Resume r = storage.load(resume.getId());
        assertEquals(resume.name, r.name);

        // Обновляем
        String newName = "Новое имя";
        r.name = newName;
        storage.update(r);

        Resume r1 = storage.load(resume.getId());
        assertEquals(newName, r1.name);

        Resume r2 = new Resume("Второе");
        storage.save(r2);

        assertNull(storage.load(123456789));

        assertEquals(r2.name, storage.load(r2.getId()).name);

        storage.delete(r2);
        assertNull(storage.load(r2.getId()));

    }
}
