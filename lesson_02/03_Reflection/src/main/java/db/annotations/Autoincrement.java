package db.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Автоматически увеличивающееся поле
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Autoincrement {
}
