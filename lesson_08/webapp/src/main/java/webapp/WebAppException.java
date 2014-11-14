package webapp;

import webapp.model.Resume;

/**
 * User: gkislin
 * Date: 18.04.2014
 */
public class WebAppException extends RuntimeException {
    private Resume resume = null;
    private String uuid = null;

    public WebAppException(String message) {
        super(message);
    }

    public WebAppException(String message, Throwable e) {
        super(message, e);
    }

    public WebAppException(String message, Resume resume) {
        super(message);
        this.resume = resume;
    }

    public WebAppException(String message, Resume resume, Throwable e) {
        super(message, e);
        this.resume = resume;
    }

    public WebAppException(String message, String uuid) {
        super(message);
        this.uuid = uuid;
    }

    public Resume getResume() {
        return resume;
    }

    public String getUuid() {
        return uuid;
    }
}
