/**
 *
 */
public class Person {
    private String name;
    private int age;

    /**
     * Getter
     *
     * @return получаем имя
     */
    public String getName() {
        return name;
    }

    /**
     * Setter
     *
     * @param s новое имя
     */
    public void setName(String s) {
        name = s;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}