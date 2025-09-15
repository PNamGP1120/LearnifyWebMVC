/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pnam.repositories.impl;

import com.pnam.pojo.InstructorProfile;
import com.pnam.repositories.InstructorProfileRepository;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author pnam
 */
@Repository
@Transactional
public class InstructorProfileRepositoryImpl implements InstructorProfileRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<InstructorProfile> findAll() {
        Session s = factory.getObject().getCurrentSession();
        return s.createQuery("FROM InstructorProfile", InstructorProfile.class).list();
    }

    @Override
    public InstructorProfile findById(Long userId) {
        Session s = factory.getObject().getCurrentSession();
        return s.find(InstructorProfile.class, userId);
    }

    @Override
    public void save(InstructorProfile instructor) {
        Session s = factory.getObject().getCurrentSession();
        if (instructor.getUserId() == null) {
            s.persist(instructor);   // thêm mới
        } else {
            s.merge(instructor);     // cập nhật
        }
    }

    @Override
    public void delete(Long userId) {
        Session s = factory.getObject().getCurrentSession();
        InstructorProfile ip = s.find(InstructorProfile.class, userId);
        if (ip != null) {
            s.remove(ip);
        }
    }
}
