import com.pnam.pojo.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class UserRepositoryTestMain {
    public static void main(String[] args) {
        // Tạo sessionFactory từ hibernate.cfg.xml
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(User.class)
                .buildSessionFactory();

        Session session = factory.openSession();

        try {
            session.beginTransaction();

            // Lấy tất cả user
            List<User> users = session.createQuery("FROM User", User.class).getResultList();
            System.out.println("===== Danh sách Users =====");
            for (User u : users) {
                System.out.printf("%d | %s | %s | %s | %s%n",
                        u.getId(), u.getUsername(), u.getEmail(), u.getRole(), u.getStatus());
            }

            // Lấy user theo ID
            Long testId = 9L; // ví dụ admin1
            User user = session.createQuery("FROM User u WHERE u.id = :id", User.class)
                    .setParameter("id", testId)
                    .uniqueResult();
            System.out.println("===== User theo ID =====");
            System.out.println(user != null ? user.getEmail() : "Không tìm thấy user id=" + testId);

            session.getTransaction().commit();
        } finally {
            session.close();
            factory.close();
        }
    }
}
