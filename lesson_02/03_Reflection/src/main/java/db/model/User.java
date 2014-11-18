package db.model;

import db.annotations.Autoincrement;
import db.annotations.Field;
import db.annotations.PrimaryKey;

/**
 * Пользователь
 */
public class User {

    @PrimaryKey
    @Autoincrement
    @Field("ID")
    public int id;

    @Field("ИМЯ")
    public String name;

    //@Field("PASSWORD")
    public String password;

    public int tempVar;

    @Field("Sdsds")
    @Deprecated
    public int m1() {
        return 1;
    }


}
