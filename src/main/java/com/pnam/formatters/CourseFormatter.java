/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pnam.formatters;

import com.pnam.pojo.Course;
import com.pnam.services.CourseService;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

/**
 *
 * @author pnam
 */
@Component
public class CourseFormatter implements Formatter<Course> {

    @Autowired
    private CourseService courseService;

    @Override
    public Course parse(String text, Locale locale) throws ParseException {
        if (text == null || text.isBlank()) {
            return null;
        }
        return courseService.getCourseById(Long.valueOf(text));
    }

    @Override
    public String print(Course course, Locale locale) {
        return (course != null ? String.valueOf(course.getId()) : "");
    }
}
