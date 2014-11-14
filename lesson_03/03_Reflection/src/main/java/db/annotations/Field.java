package db.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Поле в базе данных
 */
@Retention(RetentionPolicy.RUNTIME) // Чтобы аннотация была видна в процессе работы
public @interface Field {
    /**
     * Имя поля в базе данных
     */
    String value();
}
