package model;

import dao.Entity;

/**
 * Резюме
 */
public class Resume implements Entity {
    static int counter = 0;
    public String name;
    private int id;

    public Resume() {
    }

    public Resume(String name) {
        id = ++counter;
        this.name = name;
    }

    /**
     * @return идентификатор данного объекта
     */
    @Override
    public int getId() {
        return id;
    }

    public void setId(int value) {
        id = value;
    }

    @Override
    public String toString() {
        return "[Resume: " + id + " " + name + "]";
    }
}
