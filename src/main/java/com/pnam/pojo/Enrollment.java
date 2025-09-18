package com.pnam.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "enrollment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Enrollment.findAll", query = "SELECT e FROM Enrollment e"),
    @NamedQuery(name = "Enrollment.findById", query = "SELECT e FROM Enrollment e WHERE e.id = :id"),
    @NamedQuery(name = "Enrollment.findByEnrolledAt", query = "SELECT e FROM Enrollment e WHERE e.enrolledAt = :enrolledAt"),
    @NamedQuery(name = "Enrollment.findByAccessStatus", query = "SELECT e FROM Enrollment e WHERE e.accessStatus = :accessStatus")
})
public class Enrollment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "{enrollment.enrolledAt.notNull}")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    @Column(name = "enrolled_at", nullable = false)
    private Date enrolledAt;

    @NotBlank(message = "{enrollment.accessStatus.notBlank}")
    @Size(max = 8, message = "{enrollment.accessStatus.size}")
    @Column(name = "access_status", nullable = false, length = 8)
    private String accessStatus;

    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    @JsonIgnore
    private Course courseId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    @JsonIgnore
    private User studentId;

    @OneToMany(mappedBy = "enrollmentId", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Progress> progressSet;

    @OneToMany(mappedBy = "enrollmentId", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Payment> paymentSet;

    public Enrollment() {
    }

    public Enrollment(Long id) {
        this.id = id;
    }

    public Enrollment(Long id, Date enrolledAt, String accessStatus) {
        this.id = id;
        this.enrolledAt = enrolledAt;
        this.accessStatus = accessStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getEnrolledAt() {
        return enrolledAt;
    }

    public void setEnrolledAt(Date enrolledAt) {
        this.enrolledAt = enrolledAt;
    }

    public String getAccessStatus() {
        return accessStatus;
    }

    public void setAccessStatus(String accessStatus) {
        this.accessStatus = accessStatus;
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

    @XmlTransient
    public Set<Progress> getProgressSet() {
        return progressSet;
    }

    public void setProgressSet(Set<Progress> progressSet) {
        this.progressSet = progressSet;
    }

    @XmlTransient
    public Set<Payment> getPaymentSet() {
        return paymentSet;
    }

    public void setPaymentSet(Set<Payment> paymentSet) {
        this.paymentSet = paymentSet;
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
        if (!(object instanceof Enrollment)) {
            return false;
        }
        Enrollment other = (Enrollment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pnam.pojo.Enrollment[ id=" + id + " ]";
    }

}
