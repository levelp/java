/**
 *
 */
public class Resume {
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Устаревшее резюме
     */
    public void expired(){
        // ...
        user.notifyExpired();
    }
}
