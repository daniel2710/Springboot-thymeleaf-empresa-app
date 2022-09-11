package com.empresa.aplicacion.entity;

import java.util.Date;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {

    @CreatedBy
    protected String createdBy;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdAt;

/*    @LastModifiedBy
    protected String ultimaActualizacionPor;*/

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    protected Date updatedAt;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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

    public void setUpdateAt(Date updateAt) {
        this.updatedAt = updateAt;
    }
}

