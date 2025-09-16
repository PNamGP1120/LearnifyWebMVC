package com.pnam.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "cart_item")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CartItem.findAll", query = "SELECT c FROM CartItem c"),
    @NamedQuery(name = "CartItem.findById", query = "SELECT c FROM CartItem c WHERE c.id = :id"),
    @NamedQuery(name = "CartItem.findByAddedAt", query = "SELECT c FROM CartItem c WHERE c.addedAt = :addedAt")
})
public class CartItem implements Serializable {

    private static final long serialVersionUID = 1L;

    // ===== ID =====
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===== ADDED_AT =====
    @NotNull(message = "{cartItem.addedAt.notNull}")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "added_at", nullable = false)
    private Date addedAt;

    // ===== RELATIONSHIPS =====
    @ManyToOne(optional = false)
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    @JsonIgnore
    private Cart cartId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    @JsonIgnore
    private Course courseId;

    // ===== Constructors =====
    public CartItem() {}
    public CartItem(Long id) { this.id = id; }
    public CartItem(Long id, Date addedAt) {
        this.id = id;
        this.addedAt = addedAt;
    }

    // ===== Getters & Setters =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Date getAddedAt() { return addedAt; }
    public void setAddedAt(Date addedAt) { this.addedAt = addedAt; }

    public Cart getCartId() { return cartId; }
    public void setCartId(Cart cartId) { this.cartId = cartId; }

    public Course getCourseId() { return courseId; }
    public void setCourseId(Course courseId) { this.courseId = courseId; }

    // ===== equals & hashCode =====
    @Override
    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CartItem)) return false;
        CartItem other = (CartItem) obj;
        return this.id != null && this.id.equals(other.id);
    }

    // ===== toString =====
    @Override
    public String toString() {
        return "com.pnam.pojo.CartItem[ id=" + id + " ]";
    }
}
