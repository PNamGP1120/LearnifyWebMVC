package com.pnam.services;

import com.pnam.pojo.CourseRating;
import java.util.List;
import java.util.Map;

public interface CourseRatingService {

    List<CourseRating> getRatings(Map<String, String> params);

    long countRatings(Map<String, String> params);

    CourseRating getRatingById(Long id);

    void saveRating(CourseRating rating);

    void deleteRating(Long id);
}
