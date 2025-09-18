package com.pnam.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "lesson")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Lesson.findAll", query = "SELECT l FROM Lesson l"),
    @NamedQuery(name = "Lesson.findById", query = "SELECT l FROM Lesson l WHERE l.id = :id"),
    @NamedQuery(name = "Lesson.findByTitle", query = "SELECT l FROM Lesson l WHERE l.title = :title"),
    @NamedQuery(name = "Lesson.findByContentUrl", query = "SELECT l FROM Lesson l WHERE l.contentUrl = :contentUrl"),
    @NamedQuery(name = "Lesson.findByContentType", query = "SELECT l FROM Lesson l WHERE l.contentType = :contentType"),
    @NamedQuery(name = "Lesson.findByDurationMin", query = "SELECT l FROM Lesson l WHERE l.durationMin = :durationMin"),
    @NamedQuery(name = "Lesson.findByOrderIndex", query = "SELECT l FROM Lesson l WHERE l.orderIndex = :orderIndex"),
    @NamedQuery(name = "Lesson.findByPreviewable", query = "SELECT l FROM Lesson l WHERE l.previewable = :previewable")
})
public class Lesson implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{lesson.title.notBlank}")
    @Size(max = 200, message = "{lesson.title.size}")
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @NotBlank(message = "{lesson.contentUrl.notBlank}")
    @Size(max = 255, message = "{lesson.contentUrl.size}")
    @Column(name = "content_url", nullable = false, length = 255)
    private String contentUrl;

    @NotBlank(message = "{lesson.contentType.notBlank}")
    @Size(max = 5, message = "{lesson.contentType.size}")
    @Column(name = "content_type", nullable = false, length = 5)
    private String contentType;

    @NotNull(message = "{lesson.durationMin.notNull}")
    @Positive(message = "{lesson.durationMin.positive}")
    @Column(name = "duration_min", nullable = false)
    private int durationMin;

    @NotNull(message = "{lesson.orderIndex.notNull}")
    @Positive(message = "{lesson.orderIndex.positive}")
    @Column(name = "order_index", nullable = false)
    private int orderIndex;

    @Column(name = "previewable", nullable = false)
    private boolean previewable;

    @ManyToOne(optional = false)
    @JoinColumn(name = "section_id", referencedColumnName = "id")
    @JsonIgnore
    private CourseSection sectionId;

    @OneToMany(mappedBy = "lessonId", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Progress> progressSet;

    public Lesson() {
    }

    public Lesson(Long id) {
        this.id = id;
    }

    public Lesson(Long id, String title, String contentUrl, String contentType, int durationMin, int orderIndex, boolean previewable) {
        this.id = id;
        this.title = title;
        this.contentUrl = contentUrl;
        this.contentType = contentType;
        this.durationMin = durationMin;
        this.orderIndex = orderIndex;
        this.previewable = previewable;
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

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getDurationMin() {
        return durationMin;
    }

    public void setDurationMin(int durationMin) {
        this.durationMin = durationMin;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public boolean getPreviewable() {
        return previewable;
    }

    public void setPreviewable(boolean previewable) {
        this.previewable = previewable;
    }

    public CourseSection getSectionId() {
        return sectionId;
    }

    public void setSectionId(CourseSection sectionId) {
        this.sectionId = sectionId;
    }

    @XmlTransient
    public Set<Progress> getProgressSet() {
        return progressSet;
    }

    public void setProgressSet(Set<Progress> progressSet) {
        this.progressSet = progressSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Lesson)) {
            return false;
        }
        Lesson other = (Lesson) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pnam.pojo.Lesson[ id=" + id + " ]";
    }

}
