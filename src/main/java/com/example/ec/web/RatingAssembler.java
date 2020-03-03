package com.example.ec.web;

import com.example.ec.domain.TourRating;
import com.example.ec.repo.TourRepository;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RatingAssembler implements RepresentationModelAssembler<TourRating, RatingDto> {

    private RepositoryEntityLinks entityLinks;

    public RatingAssembler(RepositoryEntityLinks entityLinks) {
        this.entityLinks = entityLinks;
    }

    @Override
    public RatingDto toModel(TourRating entity) {
        var rating = new RatingDto(entity.getScore(), entity.getComment(), entity.getCustomerId());

        // "self" : ".../ratings/{ratingId}"
        WebMvcLinkBuilder ratingLink = WebMvcLinkBuilder.linkTo(methodOn(RatingController.class).getRating(entity.getId()));

        //"tour" : ".../tours/{tourId}"
        Link tourLink = entityLinks.linkToItemResource(TourRepository.class, entity.getTour().getId());

        rating.add(ratingLink.withSelfRel(), tourLink.withRel("tour"));

        return rating;
    }

    @Override
    public CollectionModel<RatingDto> toCollectionModel(Iterable<? extends TourRating> entities) {
        List<RatingDto> ratingDtos = new ArrayList<>();
        List<Link> tourLinks = new ArrayList<>();
        entities.forEach(entity -> ratingDtos.add(toModel(entity)));

        return new CollectionModel(ratingDtos, tourLinks);
    }
}
