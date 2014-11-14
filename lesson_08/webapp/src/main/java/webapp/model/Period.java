package webapp.model;

import webapp.util.DateUtil;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;
import java.util.Date;

/**
 * User: gkislin
 * Date: 31.01.14
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Period implements Serializable {
    static final long serialVersionUID = 1L;

    public static final Period EMPTY = new Period();

    private Date startDate;
    private Date endDate;
    private String position;
    private String content;

    public Period() {
    }

    public Period(Date startDate, Date endDate, String position, String content) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.position = position;
        this.content = content;
    }

    public Period(int startYear, int startMonth,
                  int endYear, int endMonth, String position, String content) {
        this(DateUtil.getDate(startYear, startMonth),
                DateUtil.getDate(endYear, endMonth),
                position, content);
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getPosition() {
        return position;
    }

    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Period period = (Period) o;

        if (content != null ? !content.equals(period.content) : period.content != null) return false;
        if (endDate != null ? !endDate.equals(period.endDate) : period.endDate != null) return false;
        if (position != null ? !position.equals(period.position) : period.position != null) return false;
        if (startDate != null ? !startDate.equals(period.startDate) : period.startDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = startDate != null ? startDate.hashCode() : 0;
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Period{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", position='" + position + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
