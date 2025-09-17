package com.pnam.pojo;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id"),
    @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email")
})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    // ===== ID =====
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===== USERNAME =====
    @NotBlank(message = "{user.username.notBlank}")
    @Size(min = 4, max = 100, message = "{user.username.size}")
    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;

    // ===== EMAIL =====
    @NotBlank(message = "{user.email.notBlank}")
    @Email(message = "{user.email.invalid}")
    @Size(max = 190, message = "{user.email.size}")
    @Column(name = "email", nullable = false, unique = true, length = 190)
    private String email;

    // ===== PASSWORD =====
    @NotBlank(message = "{user.password.notBlank}")
    @Size(min = 6, max = 255, message = "{user.password.size}")
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    // ===== FULL NAME =====
    @NotBlank(message = "{user.fullName.notBlank}")
    @Size(max = 150, message = "{user.fullName.size}")
    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    // ===== AVATAR =====
    @Size(max = 255, message = "{user.avatarUrl.size}")
    @Column(name = "avatar_url")
    private String avatarUrl;

    // ===== ROLE =====
    @NotBlank(message = "{user.role.notBlank}")
    @Pattern(regexp = "STUDENT|INSTRUCTOR|ADMIN", message = "{user.role.pattern}")
    @Column(name = "role", nullable = false, length = 10)
    private String role;

    // ===== STATUS =====
    @NotBlank(message = "{user.status.notBlank}")
    @Pattern(regexp = "ACTIVE|LOCKED|PENDING", message = "{user.status.pattern}")
    @Column(name = "status", nullable = false, length = 7)
    private String status;

    // ===== OAUTH =====
    @Size(max = 8)
    @Column(name = "oauth_provider")
    private String oauthProvider;

    @Size(max = 190)
    @Column(name = "oauth_sub")
    private String oauthSub;

    // ===== TIMESTAMP =====
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt = new Date();

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt = new Date();

    // ===== Constructors =====
    public User() {}

    public User(Long id) {
        this.id = id;
    }

    public User(Long id, String username, String email, String password,
                String fullName, String role, String status,
                Date createdAt, Date updatedAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ===== Getters & Setters =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getOauthProvider() { return oauthProvider; }
    public void setOauthProvider(String oauthProvider) { this.oauthProvider = oauthProvider; }

    public String getOauthSub() { return oauthSub; }
    public void setOauthSub(String oauthSub) { this.oauthSub = oauthSub; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    // ===== equals & hashCode =====
    @Override
    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof User)) return false;
        User other = (User) object;
        return (this.id != null && this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "com.pnam.pojo.User[ id=" + id + " ]";
    }
}
