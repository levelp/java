package annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Показывать ли поле
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Show {

    String name() default "";

    int spaces();
}
