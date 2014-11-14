import java.util.ArrayList;
import java.util.List;

/**
 * Использование ссылки this
 */
public class ThisLink {

    public static void main(String[] args) {
        Journal journal = new Journal();

        User A = new User("Петя");
        A.subscribe(journal);

        User B = new User("Вася");
        B.subscribe(journal);

        journal.release("Сентябрь 2014");
        journal.release("Октябрь 2014");
    }

    static class Journal {
        List<User> users = new ArrayList<User>();

        public void release(String name) {
            for (User user : users) {
                user.send(name);
            }
        }

        public void add(User user) {
            users.add(user);
        }
    }

    static class User {
        private final String name;

        public User(String name) {
            this.name = name;
        }

        public void subscribe(Journal journal) {
            journal.add(this);
        }

        public void send(String name) {
            System.out.println("Пользователь " + this.name +
                    " получил журнал " + name);
        }
    }
}
