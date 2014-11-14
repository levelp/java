package webapp.storage;

import webapp.WebAppException;
import webapp.model.Resume;

import java.io.*;

/**
 * User: gkislin
 * Date: 25.04.2014
 */
public class SerializeStorage extends FileStorage {
    public SerializeStorage(String path) {
        super(path);
    }

    @Override
    protected void doWrite(OutputStream os, Resume resume) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(resume);
        }
    }

    @Override
    protected Resume doRead(InputStream is) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(is)) {
            return (Resume) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new WebAppException("Error read resume", e);
        }
    }
}
