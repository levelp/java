package webapp.storage;

import webapp.model.*;
import webapp.util.JaxbParser;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * User: gkislin
 * Date: 07.07.2014
 */
public class XmlStorage extends FileStorage {
    private JaxbParser xmlParser;

    public XmlStorage(String path) {
        super(path);
        xmlParser = new JaxbParser(Resume.class, Organization.class, Link.class,
                OrganizationSection.class, TextSection.class, Period.class);
    }

    @Override
    protected void doWrite(OutputStream os, Resume resume) throws IOException {
        try (Writer w = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
            xmlParser.marshall(resume, w);
        }
    }

    @Override
    protected Resume doRead(InputStream is) throws IOException {
        try (Reader r = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            return xmlParser.unmarshall(r);
        }
    }
}
