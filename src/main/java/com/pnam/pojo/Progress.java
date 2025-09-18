package com.pnam.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "progress")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Progress.findAll", query = "SELECT p FROM Progress p"),
    @NamedQuery(name = "Progress.findById", query = "SELECT p FROM Progress p WHERE p.id = :id"),
    @NamedQuery(name = "Progress.findByCompleted", query = "SELECT p FROM Progress p WHERE p.completed = :completed"),
    @NamedQuery(name = "Progress.findByLastPosition", query = "SELECT p FROM Progress p WHERE p.lastPosition = :lastPosition"),
    @NamedQuery(name = "Progress.findByUpdatedAt", query = "SELECT p FROM Progress p WHERE p.updatedAt = :updatedAt")
})
public class Progress implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "{progress.completed.notNull}")
    @Column(name = "completed", nullable = false)
    private boolean completed;

    @NotNull(message = "{progress.lastPosition.notNull}")
    @PositiveOrZero(message = "{progress.lastPosition.positiveOrZero}")
    @Column(name = "last_position", nullable = false)
    private int lastPosition;

    @NotNull(message = "{progress.updatedAt.notNull}")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "enrollment_id", referencedColumnName = "id")
    @JsonIgnore
    private Enrollment enrollmentId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lesson_id", referencedColumnName = "id")
    @JsonIgnore
    private Lesson lessonId;

    public Progress() {
    }

    public Progress(Long id) {
        this.id = id;
    }

    public Progress(Long id, boolean completed, int lastPosition, Date updatedAt) {
        this.id = id;
        this.completed = completed;
        this.lastPosition = lastPosition;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(int lastPosition) {
        this.lastPosition = lastPosition;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Enrollment getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Enrollment enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public Lesson getLessonId() {
        return lessonId;
    }

    public void setLessonId(Lesson lessonId) {
        this.lessonId = lessonId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Progress)) {
            return false;
        }
        Progress other = (Progress) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.pnam.pojo.Progress[ id=" + id + " ]";
    }

}
