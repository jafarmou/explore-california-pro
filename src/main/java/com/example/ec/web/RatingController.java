package com.example.ec.web;

import com.example.ec.service.TourRatingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/ratings")
public class RatingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RatingController.class);
    private TourRatingService tourRatingService;
    private RatingAssembler ratingAssembler;

    @Autowired
    public RatingController(TourRatingService tourRatingService, RatingAssembler ratingAssembler) {
        this.tourRatingService = tourRatingService;
        this.ratingAssembler = ratingAssembler;
    }

    @GetMapping
    public List<RatingDto> getAll() {
        LOGGER.info("GET /ratings");
        List<RatingDto> ratingDtos = new ArrayList<>();
        ratingAssembler.toCollectionModel(tourRatingService.lookupAll()).forEach(ratingDtos::add);
        return ratingDtos;
    }

    @GetMapping("/{id}")
    public RatingDto getRating(@PathVariable("id") Integer id) {
        LOGGER.info("GET /ratings/{}", id);
        return ratingAssembler.toModel(tourRatingService.lookupRatingById(id)
                .orElseThrow(() -> new NoSuchElementException("Rating " + id + " not found")));
    }
}
