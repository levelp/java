package webapp.storage;

import webapp.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: gkislin
 * Date: 24.06.2014
 */
public class MapStorage extends AbstractStorage<String> {

    private Map<String, Resume> MAP = new HashMap<>();

    @Override
    protected String getCtx(String uuid) {
        return uuid;
    }

    @Override
    protected boolean exist(String uuid) {
        return MAP.get(uuid) != null;
    }

    @Override
    protected void doClear() {
        MAP.clear();
    }

    @Override
    protected void doSave(String uuid, Resume r) {
        MAP.put(uuid, r);
    }

    @Override
    protected void doUpdate(String uuid, Resume r) {
        MAP.put(uuid, r);
    }

    @Override
    protected Resume doLoad(String uuid) {
        return MAP.get(uuid);
    }

    @Override
    protected void doDelete(String uuid) {
        MAP.remove(uuid);
    }

    @Override
    protected List<Resume> doGetAll() {
        return new ArrayList<>(MAP.values());
    }

    @Override
    public int size() {
        return MAP.size();
    }
}