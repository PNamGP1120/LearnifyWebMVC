package com.pnam.repositories.base;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import java.io.Serializable;
import java.util.Map;

public abstract class BaseRepository<T, ID extends Serializable> {

    @Autowired
    private LocalSessionFactoryBean factory;

    protected static final int PAGE_SIZE = 10;

    protected Session getSession() {
        return factory.getObject().getCurrentSession();
    }

    protected abstract Class<T> getEntityClass();

    public T findById(ID id) {
        return getSession().find(getEntityClass(), id);
    }

    public T save(T entity, ID id) {
        if (id == null) {
            getSession().persist(entity);
            return entity;
        } else {
            return getSession().merge(entity);
        }
    }

    public void delete(ID id) {
        T entity = findById(id);
        if (entity != null) {
            getSession().remove(entity);
        }
    }

    /** 
     * Helper: lấy page và pageSize từ params 
     */
    protected int getPage(Map<String, String> params) {
        return params != null && params.containsKey("page") 
                ? Integer.parseInt(params.get("page")) : 1;
    }

    protected int getPageSize(Map<String, String> params) {
        return params != null && params.containsKey("pageSize") 
                ? Integer.parseInt(params.get("pageSize")) : PAGE_SIZE;
    }
}
