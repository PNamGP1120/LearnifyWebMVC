package com.pnam.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "chat_thread")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ChatThread.findAll", query = "SELECT c FROM ChatThread c"),
    @NamedQuery(name = "ChatThread.findById", query = "SELECT c FROM ChatThread c WHERE c.id = :id"),
    @NamedQuery(name = "ChatThread.findByType", query = "SELECT c FROM ChatThread c WHERE c.type = :type"),
    @NamedQuery(name = "ChatThread.findByCreatedAt", query = "SELECT c FROM ChatThread c WHERE c.createdAt = :createdAt")
})
public class ChatThread implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{chatThread.type.notBlank}")
    @Size(max = 6, message = "{chatThread.type.size}")
    @Column(name = "type", nullable = false, length = 6)
    private String type;

    @NotNull(message = "{chatThread.createdAt.notNull}")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @OneToMany(mappedBy = "threadId", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<ChatMessage> chatMessageSet;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    @JsonIgnore
    private Course courseId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_a", referencedColumnName = "id")
    @JsonIgnore
    private User userA;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_b", referencedColumnName = "id")
    @JsonIgnore
    private User userB;

    public ChatThread() {
    }

    public ChatThread(Long id) {
        this.id = id;
    }

    public ChatThread(Long id, String type, Date createdAt) {
        this.id = id;
        this.type = type;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @XmlTransient
    public Set<ChatMessage> getChatMessageSet() {
        return chatMessageSet;
    }

    public void setChatMessageSet(Set<ChatMessage> chatMessageSet) {
        this.chatMessageSet = chatMessageSet;
    }

    public Course getCourseId() {
        return courseId;
    }

    public void setCourseId(Course courseId) {
        this.courseId = courseId;
    }

    public User getUserA() {
        return userA;
    }

    public void setUserA(User userA) {
        this.userA = userA;
    }

    public User getUserB() {
        return userB;
    }

    public void setUserB(User userB) {
        this.userB = userB;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ChatThread)) {
            return false;
        }
        ChatThread other = (ChatThread) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.pnam.pojo.ChatThread[ id=" + id + " ]";
    }

}
