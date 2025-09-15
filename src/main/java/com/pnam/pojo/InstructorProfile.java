/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pnam.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author pnam
 */
@Entity
@Table(name = "instructor_profile")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InstructorProfile.findAll", query = "SELECT i FROM InstructorProfile i"),
    @NamedQuery(name = "InstructorProfile.findByUserId", query = "SELECT i FROM InstructorProfile i WHERE i.userId = :userId"),
    @NamedQuery(name = "InstructorProfile.findByCertifications", query = "SELECT i FROM InstructorProfile i WHERE i.certifications = :certifications"),
    @NamedQuery(name = "InstructorProfile.findByVerifiedByAdmin", query = "SELECT i FROM InstructorProfile i WHERE i.verifiedByAdmin = :verifiedByAdmin"),
    @NamedQuery(name = "InstructorProfile.findByVerifiedAt", query = "SELECT i FROM InstructorProfile i WHERE i.verifiedAt = :verifiedAt")})
public class InstructorProfile implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "user_id")
    private Long userId;
    @Lob
    @Size(max = 65535)
    @Column(name = "bio")
    private String bio;
    @Size(max = 255)
    @Column(name = "certifications")
    private String certifications;
    @Basic(optional = false)
    @NotNull
    @Column(name = "verified_by_admin")
    private boolean verifiedByAdmin;
    @Column(name = "verified_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date verifiedAt;
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    @JsonIgnore
    private User user;

    public InstructorProfile() {
    }

    public InstructorProfile(Long userId) {
        this.userId = userId;
    }

    public InstructorProfile(Long userId, boolean verifiedByAdmin) {
        this.userId = userId;
        this.verifiedByAdmin = verifiedByAdmin;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getCertifications() {
        return certifications;
    }

    public void setCertifications(String certifications) {
        this.certifications = certifications;
    }

    public boolean getVerifiedByAdmin() {
        return verifiedByAdmin;
    }

    public void setVerifiedByAdmin(boolean verifiedByAdmin) {
        this.verifiedByAdmin = verifiedByAdmin;
    }

    public Date getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(Date verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InstructorProfile)) {
            return false;
        }
        InstructorProfile other = (InstructorProfile) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pnam.pojo.InstructorProfile[ userId=" + userId + " ]";
    }
    
}
