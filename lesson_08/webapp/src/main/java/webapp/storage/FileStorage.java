package webapp.storage;

import webapp.WebAppException;
import webapp.model.Resume;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: gkislin
 * Date: 04.07.2014
 */
abstract public class FileStorage extends AbstractStorage<File> {

    private File dir;

    public FileStorage(String path) {
        this.dir = new File(path);
        if (!dir.isDirectory() || !dir.canWrite())
            throw new IllegalArgumentException("'" + path + "' is not directory or is not writable");
    }

    protected abstract void doWrite(OutputStream os, Resume resume) throws IOException;
    protected abstract Resume doRead(InputStream is) throws IOException;

    protected void write(File file, Resume resume) {
        try {
            doWrite(new FileOutputStream(file), resume);
        } catch (IOException e) {
            throw new WebAppException("Couldn't write file " + file.getAbsolutePath(), resume, e);
        }
    }

    protected Resume read(File file) {
        try {
            Resume r = doRead(new FileInputStream(file));
            r.setUuid(file.getName());
            return r;
        } catch (IOException e) {
            throw new WebAppException("Couldn't read file " + file.getAbsolutePath(), e);
        }
    }

    @Override
    protected File getCtx(String uuid) {
        return new File(dir, uuid);
    }

    @Override
    protected boolean exist(File file) {
        return file.exists();
    }

    @Override
    protected void doClear() {
        File[] files = dir.listFiles();
        if (files == null) return;
        for (File file : files) {
            if (!file.delete()) {
                throw new WebAppException("File " + file.getAbsolutePath() + " can not be deleted");
            }
        }
    }

    @Override
    protected void doSave(File file, Resume r) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new WebAppException("Couldn't create file " + file.getAbsolutePath(), r, e);
        }
        write(file, r);
    }

    @Override
    protected void doUpdate(File file, Resume r) {
        write(file, r);
    }

    @Override
    protected Resume doLoad(File file) {
        return read(file);
    }

    @Override
    protected void doDelete(File file) {
        if (!file.delete()) throw new WebAppException("File " + file.getAbsolutePath() + " can not be deleted");
    }

    @Override
    protected List<Resume> doGetAll() {
        File[] files = dir.listFiles();
        if (files == null) return Collections.emptyList();
        List<Resume> list = new ArrayList<>(files.length);
        for (File file : files) list.add(read(file));
        return list;
    }

    @Override
    public int size() {
        return dir.list().length;
    }
}