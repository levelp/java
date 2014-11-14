package webapp.storage;

import webapp.WebAppException;
import webapp.model.Resume;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * User: gkislin
 * Date: 24.06.2014
 */
public class ArrayStorage extends AbstractStorage<Integer> {

    private static final int NUMBER = 100;
    private final Resume[] ARRAY = new Resume[NUMBER];

    @Override
    protected Integer getCtx(String uuid) {
        for (int i = 0; i < NUMBER; i++) {
            if (ARRAY[i] != null) {
                if (ARRAY[i].getUuid().equals(uuid)) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    protected boolean exist(Integer index) {
        return index != -1;
    }

    @Override
    public void doClear() {
        Arrays.fill(ARRAY, null);
    }

    @Override
    public void doSave(Integer index, Resume r) {
        for (int i = 0; i < NUMBER; i++) {
            if (ARRAY[i] == null) {
                ARRAY[i] = r;
                return;
            }
        }
        throw new WebAppException("Array is full");
    }

    @Override
    public void doUpdate(Integer index, Resume r) {
        ARRAY[index] = r;
    }

    @Override
    public Resume doLoad(Integer index) {
        return ARRAY[index];
    }

    @Override
    public void doDelete(Integer index) {
        ARRAY[index] = null;
    }

    @Override
    // return all not null elements
    public List<Resume> doGetAll() {
        List<Resume> list = new LinkedList<>();
        for (Resume r : ARRAY) if (r != null) list.add(r);
        return list;
    }

    @Override
    public int size() {
        int size = 0;
        for (Resume r : ARRAY) if (r != null) size++;
        return size;
    }
}