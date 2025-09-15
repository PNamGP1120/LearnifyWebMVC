import com.pnam.pojo.User;
import com.pnam.services.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class UserServiceTestMain {
    public static void main(String[] args) {
        // Load Spring context
        AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext();
        ctx.scan("com.pnam"); // package gốc chứa @Service, @Repository, @Configuration
        ctx.refresh();

        UserService userService = ctx.getBean(UserService.class);

        // Test getAllUsers
        List<User> users = userService.getAllUsers();
        System.out.println("===== Danh sách Users =====");
        for (User u : users) {
            System.out.printf("%d | %s | %s | %s%n",
                    u.getId(), u.getUsername(), u.getEmail(), u.getRole());
        }

        // Test getUserById
        User u = userService.getUserById(9L); // ví dụ admin1 có id=9
        if (u != null) {
            System.out.println("===== User by ID =====");
            System.out.printf("%d | %s | %s | %s%n",
                    u.getId(), u.getUsername(), u.getEmail(), u.getRole());
        } else {
            System.out.println("Không tìm thấy user với ID=9");
        }

        ctx.close();
    }
}
