package com.pnam.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Table(name = "audit_log")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AuditLog.findAll", query = "SELECT a FROM AuditLog a"),
    @NamedQuery(name = "AuditLog.findById", query = "SELECT a FROM AuditLog a WHERE a.id = :id"),
    @NamedQuery(name = "AuditLog.findByAction", query = "SELECT a FROM AuditLog a WHERE a.action = :action"),
    @NamedQuery(name = "AuditLog.findByTableName", query = "SELECT a FROM AuditLog a WHERE a.tableName = :tableName"),
    @NamedQuery(name = "AuditLog.findByRecordId", query = "SELECT a FROM AuditLog a WHERE a.recordId = :recordId"),
    @NamedQuery(name = "AuditLog.findByCreatedAt", query = "SELECT a FROM AuditLog a WHERE a.createdAt = :createdAt")
})
public class AuditLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{auditLog.action.notBlank}")
    @Size(max = 100, message = "{auditLog.action.size}")
    @Column(name = "action", nullable = false, length = 100)
    private String action;

    @NotBlank(message = "{auditLog.tableName.notBlank}")
    @Size(max = 100, message = "{auditLog.tableName.size}")
    @Column(name = "table_name", nullable = false, length = 100)
    private String tableName;

    @Column(name = "record_id")
    private BigInteger recordId;

    @Size(max = 65535, message = "{auditLog.oldData.size}")
    @Lob
    @Column(name = "old_data")
    private String oldData;

    @Size(max = 65535, message = "{auditLog.newData.size}")
    @Lob
    @Column(name = "new_data")
    private String newData;

    @NotNull(message = "{auditLog.createdAt.notNull}")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User userId;

    public AuditLog() {
    }

    public AuditLog(Long id) {
        this.id = id;
    }

    public AuditLog(Long id, String action, String tableName, Date createdAt) {
        this.id = id;
        this.action = action;
        this.tableName = tableName;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public BigInteger getRecordId() {
        return recordId;
    }

    public void setRecordId(BigInteger recordId) {
        this.recordId = recordId;
    }

    public String getOldData() {
        return oldData;
    }

    public void setOldData(String oldData) {
        this.oldData = oldData;
    }

    public String getNewData() {
        return newData;
    }

    public void setNewData(String newData) {
        this.newData = newData;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AuditLog)) {
            return false;
        }
        AuditLog other = (AuditLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pnam.pojo.AuditLog[ id=" + id + " ]";
    }

}
