package webapp.model;

/**
 * User: gkislin
 * Date: 08.07.2014
 */
public enum SectionClass {
    TEXT(TextSection.EMPTY) {
        @Override
        public Section create() {
            return new TextSection();
        }

        @Override
        public void addEmptyValue(Section s) {
             s.addFirst("");
        }
    },
    ORGANIZATION(OrganizationSection.EMPTY) {
        @Override
        public Section create() {
            return new OrganizationSection();
        }

        @Override
        public void addEmptyValue(Section s) {
            for (Organization org : ((OrganizationSection) s).getValues()) {
                org.addFirstPeriod(Period.EMPTY);
            }
            s.addFirst(Organization.EMPTY);
        }
    };

    SectionClass(Section emptySection) {
        this.emptySection = emptySection;
    }

    private final Section emptySection;

    public abstract Section create();

    public Section getEmptySection() {
        return emptySection;
    }

    public abstract void addEmptyValue(Section s);
}
