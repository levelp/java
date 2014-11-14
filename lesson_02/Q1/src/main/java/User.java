/**
 *
 */
public class User {
    /**
     * Имя пользователя
     */
    public String name;

    /**
     * Резюме
     */
    public Resume resume;

    /**
     * Создать резюме
     */
    public void createResume() {
        resume = new Resume();
        resume.setUser(this);
    }

    public void notifyExpired() {
        // sendEmail();
    }
}
