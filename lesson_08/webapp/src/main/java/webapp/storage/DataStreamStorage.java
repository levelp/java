package webapp.storage;

import webapp.model.*;

import java.io.*;
import java.util.*;

/**
 * User: gkislin
 * Date: 04.07.2014
 */
public class DataStreamStorage extends FileStorage {

    private static final String NULL = "null";

    public DataStreamStorage(String path) {
        super(path);
    }

    @Override
    protected void doWrite(OutputStream fos, Resume resume) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(fos)) {
            writeStr(dos, resume.getFullName());
            writeStr(dos, resume.getLocation());

            Map<ContactType, String> contacts = resume.getContacts();
            dos.writeInt(contacts.size());

            for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
                writeStr(dos, entry.getKey().name());
                writeStr(dos, entry.getValue());
            }

            Map<SectionType, Section> sections = resume.getSections();
            dos.writeInt(sections.size());

            for (Map.Entry<SectionType, Section> entry : sections.entrySet()) {
                Section section = entry.getValue();
                SectionType type = entry.getKey();
                writeStr(dos, type.name());
                Collection sectionValues = section.getValues();
                dos.writeInt(sectionValues.size());
                if (type.getSectionClass() == SectionClass.TEXT) {
                    for (String val : (Collection<String>) sectionValues) {
                        writeStr(dos, val);
                    }
                } else {
                    for (Organization val : (Collection<Organization>) sectionValues) {
                        writeStr(dos, val.getLink().getName());
                        writeStr(dos, val.getLink().getUrl());
                        Collection<Period> periods = val.getPeriods();
                        dos.writeInt(periods.size());
                        for (Period p : periods) {
                            dos.writeLong(p.getStartDate().getTime());
                            dos.writeLong(p.getEndDate().getTime());
                            writeStr(dos, p.getPosition());
                            writeStr(dos, p.getContent());
                        }
                    }
                }
            }
        }
    }

    @Override
    protected Resume doRead(InputStream fis) throws IOException {
        Resume r = new Resume();
        try (DataInputStream dis = new DataInputStream(fis)) {
            r.setFullName(readStr(dis));
            r.setLocation(readStr(dis));

            final int contactsSize = dis.readInt();
            for (int i = 0; i < contactsSize; i++) {
                r.addContact(ContactType.valueOf(readStr(dis)), readStr(dis));
            }

            final int sectionsSize = dis.readInt();
            for (int i = 0; i < sectionsSize; i++) {

                SectionType sectionType = SectionType.valueOf(readStr(dis));
                Section section = sectionType.getSectionClass().create();

                r.addSection(sectionType, section);
                int sectionValuesSize = dis.readInt();

                for (int j = 0; j < sectionValuesSize; j++) {
                    if (sectionType.getSectionClass() == SectionClass.TEXT) {
                        section.add(readStr(dis));
                    } else {
                        String name = readStr(dis);
                        String url = readStr(dis);
                        int periodsSize = dis.readInt();
                        LinkedList<Period> periods = new LinkedList<>();
                        for (int k = 0; k < periodsSize; k++) {
                            periods.add(
                                    new Period(new Date(dis.readLong()), new Date(dis.readLong()), readStr(dis), readStr(dis)));
                        }
                        section.add(new Organization(name, url, periods));
                    }
                }
            }
        }
        return r;
    }

    private void writeStr(DataOutputStream dos, String str) throws IOException {
        dos.writeUTF(str == null ? NULL : str);
    }

    private String readStr(DataInputStream dis) throws IOException {
        String str = dis.readUTF();
        return str.equals(NULL) ? null : str;
    }
}