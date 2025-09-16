package com.pnam.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "payment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Payment.findAll", query = "SELECT p FROM Payment p"),
    @NamedQuery(name = "Payment.findById", query = "SELECT p FROM Payment p WHERE p.id = :id"),
    @NamedQuery(name = "Payment.findByMethod", query = "SELECT p FROM Payment p WHERE p.method = :method"),
    @NamedQuery(name = "Payment.findByAmount", query = "SELECT p FROM Payment p WHERE p.amount = :amount"),
    @NamedQuery(name = "Payment.findByCurrency", query = "SELECT p FROM Payment p WHERE p.currency = :currency"),
    @NamedQuery(name = "Payment.findByGatewayTxnId", query = "SELECT p FROM Payment p WHERE p.gatewayTxnId = :gatewayTxnId"),
    @NamedQuery(name = "Payment.findByStatus", query = "SELECT p FROM Payment p WHERE p.status = :status"),
    @NamedQuery(name = "Payment.findByCreatedAt", query = "SELECT p FROM Payment p WHERE p.createdAt = :createdAt")
})
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{payment.method.notBlank}")
    @Size(max = 7, message = "{payment.method.size}")
    @Column(name = "method", nullable = false, length = 7)
    private String method;

    @NotNull(message = "{payment.amount.notNull}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{payment.amount.min}")
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @NotBlank(message = "{payment.currency.notBlank}")
    @Size(max = 3, message = "{payment.currency.size}")
    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Size(max = 190, message = "{payment.gatewayTxnId.size}")
    @Column(name = "gateway_txn_id", length = 190)
    private String gatewayTxnId;

    @NotBlank(message = "{payment.status.notBlank}")
    @Size(max = 9, message = "{payment.status.size}")
    @Column(name = "status", nullable = false, length = 9)
    private String status;

    @Size(max = 65535, message = "{payment.rawWebhook.size}")
    @Lob
    @Column(name = "raw_webhook")
    private String rawWebhook;

    @NotNull(message = "{payment.createdAt.notNull}")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "enrollment_id", referencedColumnName = "id")
    @JsonIgnore
    private Enrollment enrollmentId;

    public Payment() {
    }

    public Payment(Long id) {
        this.id = id;
    }

    public Payment(Long id, String method, BigDecimal amount, String currency, String status, Date createdAt) {
        this.id = id;
        this.method = method;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getGatewayTxnId() {
        return gatewayTxnId;
    }

    public void setGatewayTxnId(String gatewayTxnId) {
        this.gatewayTxnId = gatewayTxnId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRawWebhook() {
        return rawWebhook;
    }

    public void setRawWebhook(String rawWebhook) {
        this.rawWebhook = rawWebhook;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Enrollment getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Enrollment enrollmentId) {
        this.enrollmentId = enrollmentId;
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
        if (!(object instanceof Payment)) {
            return false;
        }
        Payment other = (Payment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pnam.pojo.Payment[ id=" + id + " ]";
    }

}
