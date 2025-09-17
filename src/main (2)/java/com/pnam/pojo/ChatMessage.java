package com.pnam.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "chat_message")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ChatMessage.findAll", query = "SELECT c FROM ChatMessage c"),
    @NamedQuery(name = "ChatMessage.findById", query = "SELECT c FROM ChatMessage c WHERE c.id = :id"),
    @NamedQuery(name = "ChatMessage.findByContent", query = "SELECT c FROM ChatMessage c WHERE c.content = :content"),
    @NamedQuery(name = "ChatMessage.findBySentAt", query = "SELECT c FROM ChatMessage c WHERE c.sentAt = :sentAt")
})
public class ChatMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{chatMessage.content.notBlank}")
    @Size(max = 1000, message = "{chatMessage.content.size}")
    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @NotNull(message = "{chatMessage.sentAt.notNull}")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sent_at", nullable = false)
    private Date sentAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "thread_id", referencedColumnName = "id")
    @JsonIgnore
    private ChatThread threadId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    @JsonIgnore
    private User senderId;

    public ChatMessage() {
    }

    public ChatMessage(Long id) {
        this.id = id;
    }

    public ChatMessage(Long id, String content, Date sentAt) {
        this.id = id;
        this.content = content;
        this.sentAt = sentAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getSentAt() {
        return sentAt;
    }

    public void setSentAt(Date sentAt) {
        this.sentAt = sentAt;
    }

    public ChatThread getThreadId() {
        return threadId;
    }

    public void setThreadId(ChatThread threadId) {
        this.threadId = threadId;
    }

    public User getSenderId() {
        return senderId;
    }

    public void setSenderId(User senderId) {
        this.senderId = senderId;
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
        if (!(object instanceof ChatMessage)) {
            return false;
        }
        ChatMessage other = (ChatMessage) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pnam.pojo.ChatMessage[ id=" + id + " ]";
    }

}
