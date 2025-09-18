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
@Table(name = "cart")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cart.findAll", query = "SELECT c FROM Cart c"),
    @NamedQuery(name = "Cart.findById", query = "SELECT c FROM Cart c WHERE c.id = :id"),
    @NamedQuery(name = "Cart.findByCreatedAt", query = "SELECT c FROM Cart c WHERE c.createdAt = :createdAt"),
    @NamedQuery(name = "Cart.findByStatus", query = "SELECT c FROM Cart c WHERE c.status = :status")
})
public class Cart implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "{cart.createdAt.notNull}")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @NotBlank(message = "{cart.status.notBlank}")
    @Size(max = 11, message = "{cart.status.size}")
    @Column(name = "status", nullable = false, length = 11)
    private String status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    @JsonIgnore
    private User studentId;

    @OneToMany(mappedBy = "cartId", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<CartItem> cartItemSet;

    public Cart() {
    }

    public Cart(Long id) {
        this.id = id;
    }

    public Cart(Long id, Date createdAt, String status) {
        this.id = id;
        this.createdAt = createdAt;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getStudentId() {
        return studentId;
    }

    public void setStudentId(User studentId) {
        this.studentId = studentId;
    }

    @XmlTransient
    public Set<CartItem> getCartItemSet() {
        return cartItemSet;
    }

    public void setCartItemSet(Set<CartItem> cartItemSet) {
        this.cartItemSet = cartItemSet;
    }

    @Override
    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Cart)) {
            return false;
        }
        Cart other = (Cart) obj;
        return this.id != null && this.id.equals(other.id);
    }

    @Override
    public String toString() {
        return "com.pnam.pojo.Cart[ id=" + id + " ]";
    }
}
