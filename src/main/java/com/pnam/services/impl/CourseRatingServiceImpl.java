package com.pnam.services.impl;

import com.pnam.pojo.CourseRating;
import com.pnam.repositories.CourseRatingRepository;
import com.pnam.services.CourseRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CourseRatingServiceImpl implements CourseRatingService {

    @Autowired
    private CourseRatingRepository ratingRepo;

    @Override
    public List<CourseRating> getRatings(Map<String, String> params) {
        return ratingRepo.getRatings(params);
    }

    @Override
    public long countRatings(Map<String, String> params) {
        return ratingRepo.countRatings(params);
    }

    @Override
    public CourseRating getRatingById(Long id) {
        return ratingRepo.findById(id);
    }

    @Override
    public void saveRating(CourseRating rating) {
        ratingRepo.save(rating);
    }

    @Override
    public void deleteRating(Long id) {
        ratingRepo.delete(id);
    }
}
