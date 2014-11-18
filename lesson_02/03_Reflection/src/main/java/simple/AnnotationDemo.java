package simple;

import db.annotations.Field;
import db.model.User;

/**
 *
 */
public class AnnotationDemo {
    public static void main(String[] args) throws NoSuchFieldException {
        User user = new User();
        Field fieldAnnotation =
                user.getClass().getField("name").getAnnotation(Field.class);
        System.out.println(fieldAnnotation.value());
    }
}
