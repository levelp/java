package webapp.storage;

import webapp.WebAppException;
import webapp.model.Resume;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * User: gkislin
 * Date: 04.07.2014
 */
abstract public class AbstractStorage<C> implements IStorage {

    protected Logger LOGGER = Logger.getLogger(getClass().getName());

    protected abstract C getCtx(String uuid);
    protected abstract boolean exist(C ctx);
    protected abstract void doClear();
    protected abstract void doSave(C ctx, Resume r);
    protected abstract void doUpdate(C ctx, Resume r);
    protected abstract Resume doLoad(C ctx);
    protected abstract void doDelete(C ctx);
    protected abstract List<Resume> doGetAll();

    public abstract int size();

    @Override
    public void clear() {
        LOGGER.info("Delete all resumes.");
        doClear();
    }

    @Override
    public void save(Resume r) {
        LOGGER.info("Save resume with uuid=" + r.getUuid());
        C ctx = getCtx(r.getUuid());
        if (exist(ctx)) throw new WebAppException("Resume " + r.getUuid() + "already exist", r);
        doSave(ctx, r);
    }

    @Override
    public void update(Resume r) {
        LOGGER.info("Update resume with " + r.getUuid());
        C ctx = getCtx(r.getUuid());
        if (!exist(ctx)) throw new WebAppException("Resume " + r.getUuid() + "not exist", r);
        doUpdate(ctx, r);
    }

    @Override
    public Resume load(String uuid) {
        LOGGER.info("Load resume with uuid=" + uuid);
        C ctx = getCtx(uuid);
        if (!exist(ctx)) throw new WebAppException("Resume " + uuid + "not exist", uuid);
        return doLoad(ctx);
    }

    @Override
    public void delete(String uuid) {
        LOGGER.info("Delete resume with uuid=" + uuid);
        C ctx = getCtx(uuid);
        if (!exist(ctx)) throw new WebAppException("Resume " + uuid + "not exist", uuid);
        doDelete(ctx);
    }

    @Override
    public Collection<Resume> getAllSorted() {
        LOGGER.info("getAllSorted");
        List<Resume> list = doGetAll();
        Collections.sort(list);
        return list;
    }
}