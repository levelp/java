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

    @Field("NAME")
    public String name;

    @Field("PASSWORD")
    public String password;

    public int tempVar;


}
