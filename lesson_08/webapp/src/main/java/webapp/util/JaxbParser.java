package webapp.util;


import webapp.WebAppException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.Reader;
import java.io.Writer;

/**
 * User: gkislin
 * Date: 30.10.13
 */
public class JaxbParser {
    private final Marshaller marshaller;
    private final Unmarshaller unmarshaller;

    public JaxbParser(Class... classesToBeBound) {
        try {
            JAXBContext ctx = JAXBContext.newInstance(classesToBeBound);

            marshaller = ctx.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
//            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

            unmarshaller = ctx.createUnmarshaller();

        } catch (JAXBException e) {
            throw new WebAppException("Jaxb init failed", e);
        }
    }

    public <T> T unmarshall(Reader reader)  {
        try {
            return (T) unmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            throw new WebAppException("Jaxb unmarshall failed", e);
        }
    }

    public void marshall(Object instance, Writer writer) {
        try {
            marshaller.marshal(instance, writer);
        } catch (JAXBException e) {
            throw new WebAppException("Jaxb marshal failed", e);
        }
    }
}
