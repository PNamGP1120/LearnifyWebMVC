/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pnam.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author pnam
 */
@Entity
@Table(name = "course")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Course.findAll", query = "SELECT c FROM Course c"),
    @NamedQuery(name = "Course.findById", query = "SELECT c FROM Course c WHERE c.id = :id"),
    @NamedQuery(name = "Course.findByTitle", query = "SELECT c FROM Course c WHERE c.title = :title"),
    @NamedQuery(name = "Course.findBySlug", query = "SELECT c FROM Course c WHERE c.slug = :slug"),
    @NamedQuery(name = "Course.findByCoverImage", query = "SELECT c FROM Course c WHERE c.coverImage = :coverImage"),
    @NamedQuery(name = "Course.findByIntroVideoUrl", query = "SELECT c FROM Course c WHERE c.introVideoUrl = :introVideoUrl"),
    @NamedQuery(name = "Course.findByPrice", query = "SELECT c FROM Course c WHERE c.price = :price"),
    @NamedQuery(name = "Course.findByCurrency", query = "SELECT c FROM Course c WHERE c.currency = :currency"),
    @NamedQuery(name = "Course.findByDurationHours", query = "SELECT c FROM Course c WHERE c.durationHours = :durationHours"),
    @NamedQuery(name = "Course.findByStatus", query = "SELECT c FROM Course c WHERE c.status = :status"),
    @NamedQuery(name = "Course.findByCreatedAt", query = "SELECT c FROM Course c WHERE c.createdAt = :createdAt"),
    @NamedQuery(name = "Course.findByUpdatedAt", query = "SELECT c FROM Course c WHERE c.updatedAt = :updatedAt")})
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{course.title.notBlank}")
    @Size(max = 200, message = "{course.title.size}")
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @NotBlank(message = "{course.slug.notBlank}")
    @Size(max = 220, message = "{course.slug.size}")
    @Column(name = "slug", nullable = false, length = 220)
    private String slug;

    @Size(max = 65535, message = "{course.description.size}")
    @Lob
    private String description;

    @Size(max = 255, message = "{course.coverImage.size}")
    @Column(name = "cover_image")
    private String coverImage;

    @Transient
    @JsonIgnore
    @jakarta.xml.bind.annotation.XmlTransient
    private MultipartFile coverFile;

    @Size(max = 255, message = "{course.introVideoUrl.size}")
    @Column(name = "intro_video_url")
    private String introVideoUrl;

    @DecimalMin(value = "0.0", inclusive = true, message = "{course.price.min}")
    private BigDecimal price;

    @Size(max = 3, message = "{course.currency.size}")
    private String currency;

    @NotNull(message = "{course.duration.notNull}")
    @Positive(message = "{course.duration.positive}")
    @Column(name = "duration_hours", nullable = false)
    private BigDecimal durationHours;

    @NotBlank(message = "{course.status.notBlank}")
    @Size(max = 11, message = "{course.status.size}")
    private String status;

    @NotNull(message = "{course.createdAt.notNull}")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt = new Date();

    @NotNull(message = "{course.updatedAt.notNull}")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt = new Date();

    @OneToMany(mappedBy = "courseId", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Wishlist> wishlistSet;

    @OneToMany(mappedBy = "courseId", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<CourseRating> courseRatingSet;

    @OneToMany(mappedBy = "courseId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Enrollment> enrollmentSet;

    @OneToMany(mappedBy = "courseId", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<CourseSection> courseSectionSet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonIgnore
    private Category categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    @JsonIgnore
    private User instructorId;

    @OneToMany(mappedBy = "courseId", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<CartItem> cartItemSet;

    @OneToMany(mappedBy = "courseId")
    @JsonIgnore
    private Set<ChatThread> chatThreadSet;

    public Course() {
    }

    public Course(Long id) {
        this.id = id;
    }

    public Course(Long id, String title, String slug, BigDecimal durationHours, String status, Date createdAt, Date updatedAt) {
        this.id = id;
        this.title = title;
        this.slug = slug;
        this.durationHours = durationHours;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getIntroVideoUrl() {
        return introVideoUrl;
    }

    public void setIntroVideoUrl(String introVideoUrl) {
        this.introVideoUrl = introVideoUrl;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(BigDecimal durationHours) {
        this.durationHours = durationHours;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @XmlTransient
    public Set<Wishlist> getWishlistSet() {
        return wishlistSet;
    }

    public void setWishlistSet(Set<Wishlist> wishlistSet) {
        this.wishlistSet = wishlistSet;
    }

    @XmlTransient
    public Set<CourseRating> getCourseRatingSet() {
        return courseRatingSet;
    }

    public void setCourseRatingSet(Set<CourseRating> courseRatingSet) {
        this.courseRatingSet = courseRatingSet;
    }

    @XmlTransient
    public Set<Enrollment> getEnrollmentSet() {
        return enrollmentSet;
    }

    public void setEnrollmentSet(Set<Enrollment> enrollmentSet) {
        this.enrollmentSet = enrollmentSet;
    }

    @XmlTransient
    public Set<CourseSection> getCourseSectionSet() {
        return courseSectionSet;
    }

    public void setCourseSectionSet(Set<CourseSection> courseSectionSet) {
        this.courseSectionSet = courseSectionSet;
    }

    public Category getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Category categoryId) {
        this.categoryId = categoryId;
    }

    public User getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(User instructorId) {
        this.instructorId = instructorId;
    }

    @XmlTransient
    public Set<CartItem> getCartItemSet() {
        return cartItemSet;
    }

    public void setCartItemSet(Set<CartItem> cartItemSet) {
        this.cartItemSet = cartItemSet;
    }

    @XmlTransient
    public Set<ChatThread> getChatThreadSet() {
        return chatThreadSet;
    }

    public void setChatThreadSet(Set<ChatThread> chatThreadSet) {
        this.chatThreadSet = chatThreadSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Course)) {
            return false;
        }
        Course other = (Course) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.pnam.pojo.Course[ id=" + id + " ]";
    }

    public MultipartFile getCoverFile() {
        return coverFile;
    }

    public void setCoverFile(MultipartFile coverFile) {
        this.coverFile = coverFile;
    }

}
