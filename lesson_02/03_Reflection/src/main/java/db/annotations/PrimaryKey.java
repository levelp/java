package db.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Первичный ключ
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface PrimaryKey {
}
