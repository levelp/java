package webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

/**
 * User: gkislin
 * Date: 20.06.2014
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Section<T> implements Serializable {
    static final long serialVersionUID = 1L;

    private LinkedList<T> values;

    protected Section() {
        values = new LinkedList<>();
    }

    public Section(T[] values) {
        this.values = new LinkedList<>(Arrays.asList(values));
    }

    public void add(T value) {
        values.add(value);
    }

    public void addFirst(T value) {
        values.addFirst(value);
    }

    @Override
    public String toString() {
        return "Section( " + values + " )";
    }

    public Collection<T> getContent() {
        return values;
    }

    public Collection<T> getValues() {
        return values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Section section = (Section) o;

        if (!values.equals(section.values)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }
}
