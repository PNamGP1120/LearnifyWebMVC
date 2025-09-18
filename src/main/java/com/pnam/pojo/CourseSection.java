package com.pnam.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "course_section")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CourseSection.findAll", query = "SELECT c FROM CourseSection c"),
    @NamedQuery(name = "CourseSection.findById", query = "SELECT c FROM CourseSection c WHERE c.id = :id"),
    @NamedQuery(name = "CourseSection.findByTitle", query = "SELECT c FROM CourseSection c WHERE c.title = :title"),
    @NamedQuery(name = "CourseSection.findByOrderIndex", query = "SELECT c FROM CourseSection c WHERE c.orderIndex = :orderIndex")
})
public class CourseSection implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{courseSection.title.notBlank}")
    @Size(max = 200, message = "{courseSection.title.size}")
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @NotNull(message = "{courseSection.orderIndex.notNull}")
    @Min(value = 1, message = "{courseSection.orderIndex.min}")
    @Column(name = "order_index", nullable = false)
    private int orderIndex;

    @OneToMany(mappedBy = "sectionId", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Lesson> lessonSet;

    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    @JsonIgnore
    private Course courseId;

    public CourseSection() {
    }

    public CourseSection(Long id) {
        this.id = id;
    }

    public CourseSection(Long id, String title, int orderIndex) {
        this.id = id;
        this.title = title;
        this.orderIndex = orderIndex;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    @XmlTransient
    public Set<Lesson> getLessonSet() {
        return lessonSet;
    }

    public void setLessonSet(Set<Lesson> lessonSet) {
        this.lessonSet = lessonSet;
    }

    public Course getCourseId() {
        return courseId;
    }

    public void setCourseId(Course courseId) {
        this.courseId = courseId;
    }

    @Override
    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CourseSection)) {
            return false;
        }
        CourseSection other = (CourseSection) o;
        return id != null && id.equals(other.id);
    }

    @Override
    public String toString() {
        return "com.pnam.pojo.CourseSection[ id=" + id + " ]";
    }
}
