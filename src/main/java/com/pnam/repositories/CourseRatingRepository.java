package com.pnam.repositories;

import com.pnam.pojo.CourseRating;
import java.util.List;
import java.util.Map;

public interface CourseRatingRepository {
    List<CourseRating> getRatings(Map<String, String> params);
    long countRatings(Map<String, String> params);
    CourseRating findById(Long id);
    void save(CourseRating rating);
    void delete(Long id);
}
