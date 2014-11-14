package webapp.model;

import webapp.util.Util;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

/**
 * User: gkislin
 * Date: 20.06.2014
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Resume implements Comparable<Resume>, Serializable {
    static final long serialVersionUID = 1L;

    public static final Resume EMPTY;

    static {
        EMPTY = new Resume();
        for (SectionType type : SectionType.values()) {
            EMPTY.addSection(type, type.getSectionClass().getEmptySection());
        }
    }

    private String uuid;
    private String fullName;
    private String location;
    private Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
    private Map<SectionType, Section> sections = new EnumMap<>(SectionType.class);

    public Resume() {
    }

    public Resume(String fullName, String location) {
        this(UUID.randomUUID().toString(), fullName, location);
    }

    public Resume(String uuid, String fullName, String location) {
        this.uuid = uuid;
        this.fullName = fullName;
        setLocation(location);
    }

    public String getContact(ContactType type) {
        return contacts.get(type);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = Util.mask(location);
    }

    public void addSection(SectionType type, String... values) {
        addSection(type, new TextSection(values));
    }

    public void addSection(SectionType type, Organization... values) {
        addSection(type, new OrganizationSection(values));
    }

    public void addSection(SectionType type, Section s) {
        sections.put(type, s);
    }

    public Section getSection(SectionType type) {
        return sections.get(type);
    }

    public void addContact(ContactType type, String value) {
        contacts.put(type, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resume resume = (Resume) o;

        if (!contacts.equals(resume.contacts)) return false;
        if (!fullName.equals(resume.fullName)) return false;
        if (location != null ? !location.equals(resume.location) : resume.location != null) return false;
        if (!sections.equals(resume.sections)) return false;
        if (!uuid.equals(resume.uuid)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = uuid.hashCode();
        result = 31 * result + fullName.hashCode();
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + contacts.hashCode();
        result = 31 * result + sections.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Resume{" +
                "uuid='" + uuid + '\'' +
                ", fullName='" + fullName + '\'' +
                ", location='" + location + '\'' +
                ", contacts=" + contacts +
                ", sections=" + sections +
                '}';
    }

    @Override
    public int compareTo(Resume o) {
        int cmp = fullName.compareTo(o.fullName);
        return cmp == 0 ? uuid.compareTo(o.uuid) : cmp;
    }

    public Map<ContactType, String> getContacts() {
        return contacts;
    }

    public Map<SectionType, Section> getSections() {
        return sections;
    }

    public void createUuid() {
        this.uuid = UUID.randomUUID().toString();
    }
}
