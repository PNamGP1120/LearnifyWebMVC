package com.pnam.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "course_rating")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CourseRating.findAll", query = "SELECT c FROM CourseRating c"),
    @NamedQuery(name = "CourseRating.findById", query = "SELECT c FROM CourseRating c WHERE c.id = :id"),
    @NamedQuery(name = "CourseRating.findByRating", query = "SELECT c FROM CourseRating c WHERE c.rating = :rating"),
    @NamedQuery(name = "CourseRating.findByComment", query = "SELECT c FROM CourseRating c WHERE c.comment = :comment"),
    @NamedQuery(name = "CourseRating.findByCreatedAt", query = "SELECT c FROM CourseRating c WHERE c.createdAt = :createdAt")
})
public class CourseRating implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "{courseRating.rating.notNull}")
    @Min(value = 1, message = "{courseRating.rating.min}")
    @Max(value = 5, message = "{courseRating.rating.max}")
    @Column(name = "rating", nullable = false)
    private short rating;

    @Size(max = 500, message = "{courseRating.comment.size}")
    @Column(name = "comment", length = 500)
    private String comment;

    @NotNull(message = "{courseRating.createdAt.notNull}")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    @JsonIgnore
    private Course courseId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    @JsonIgnore
    private User studentId;

    public CourseRating() {
    }

    public CourseRating(Long id) {
        this.id = id;
    }

    public CourseRating(Long id, short rating, Date createdAt) {
        this.id = id;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public short getRating() {
        return rating;
    }

    public void setRating(short rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Course getCourseId() {
        return courseId;
    }

    public void setCourseId(Course courseId) {
        this.courseId = courseId;
    }

    public User getStudentId() {
        return studentId;
    }

    public void setStudentId(User studentId) {
        this.studentId = studentId;
    }

    @Override
    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CourseRating)) {
            return false;
        }
        CourseRating other = (CourseRating) o;
        return id != null && id.equals(other.id);
    }

    @Override
    public String toString() {
        return "com.pnam.pojo.CourseRating[ id=" + id + " ]";
    }
}
