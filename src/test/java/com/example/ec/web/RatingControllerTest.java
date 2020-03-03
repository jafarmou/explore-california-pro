package com.example.ec.web;

import com.example.ec.domain.Tour;
import com.example.ec.domain.TourRating;
import com.example.ec.service.TourRatingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RatingControllerTest {
    private static final String RATINGS_URL = "/ratings";

    private static final int CUSTOMER_ID = 1000;
    private static final int TOUR_ID = 999;
    private static final int RATING_ID = 555;
    private static final int SCORE = 3;
    private static final String COMMENT = "comment";

    @MockBean
    private TourRatingService tourRatingServiceMock;

    @Mock
    private TourRating tourRatingMock;

    @Mock
    private Tour tourMock;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setupReturnValuesOfMockMethods() {
        Mockito.lenient().when(tourRatingMock.getTour()).thenReturn(tourMock);
        Mockito.lenient().when(tourMock.getId()).thenReturn(TOUR_ID);
        Mockito.lenient().when(tourRatingMock.getComment()).thenReturn(COMMENT);
        Mockito.lenient().when(tourRatingMock.getScore()).thenReturn(SCORE);
        Mockito.lenient().when(tourRatingMock.getCustomerId()).thenReturn(CUSTOMER_ID);
    }

    /**************************************************************************************
     *
     * HTTP GET  /ratings
     *
     **************************************************************************************/

    @Test
    public void getRatings() {

        when(tourRatingServiceMock.lookupAll()).thenReturn(Arrays.asList(tourRatingMock, tourRatingMock, tourRatingMock));

        ResponseEntity<List<RatingDto>> response = restTemplate.exchange(RATINGS_URL, HttpMethod.GET,null,
                new ParameterizedTypeReference<List<RatingDto>>() {});

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(3, response.getBody().size());
    }

    /**************************************************************************************
     *
     * HTTP GET  /ratings/{id}
     *
     **************************************************************************************/

    @Test
    public void getOne() {
        when(tourRatingServiceMock.lookupRatingById(RATING_ID)).thenReturn(Optional.of(tourRatingMock));

//        ResponseEntity<RatingDto> response = restTemplate.exchange(RATINGS_URL + "/" + RATING_ID, HttpMethod.GET,
//                null, RatingDto.class);
        ResponseEntity<RatingDto> response = restTemplate.getForEntity(RATINGS_URL + "/" + RATING_ID, RatingDto.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(COMMENT, response.getBody().getComment());
        Assertions.assertEquals(CUSTOMER_ID, response.getBody().getCustomerId());
        Assertions.assertEquals(SCORE, response.getBody().getScore());
    }
}
