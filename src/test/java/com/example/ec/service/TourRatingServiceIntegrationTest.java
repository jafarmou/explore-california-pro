package com.example.ec.service;

import com.example.ec.domain.TourRating;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class TourRatingServiceIntegrationTest {
    private static final int CUSTOMER_ID = 456;
    private static final int TOUR_ID = 1;
    private static final int NOT_A_TOUR_ID = 123;

    @Autowired
    private TourRatingService service;

    //Happy Path delete existing TourRating.
    @Test
    public void delete() {
        List<TourRating> tourRatings = service.lookupAll();
        service.delete(tourRatings.get(0).getTour().getId(), tourRatings.get(0).getCustomerId());
        Assertions.assertEquals(tourRatings.size() - 1, service.lookupAll().size());
    }

    //UnHappy Path, Tour NOT_A_TOUR_ID does not exist
    @Test
    public void deleteException() {
        Assertions.assertThrows(NoSuchElementException.class, () -> service.delete(NOT_A_TOUR_ID, CUSTOMER_ID));
    }

    //Happy Path to Create a new Tour Rating
    @Test
    public void createNew() {
        //would throw NoSuchElementException if TourRating for TOUR_ID by CUSTOMER_ID already exists
        service.createNew(TOUR_ID, CUSTOMER_ID, 2, "it was fair");

        //Verify New Tour Rating created.
        TourRating newTourRating = service.verifyTourRating(TOUR_ID, CUSTOMER_ID);
        Assertions.assertEquals(TOUR_ID, newTourRating.getTour().getId());
        Assertions.assertEquals(CUSTOMER_ID, newTourRating.getCustomerId());
        Assertions.assertEquals(2, newTourRating.getScore());
        Assertions.assertEquals("it was fair", newTourRating.getComment());
    }

    //UnHappy Path, Tour NOT_A_TOUR_ID does not exist
    @Test
    public void createNewException() {
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            service.createNew(NOT_A_TOUR_ID, CUSTOMER_ID, 2, "it was fair");
        });
    }

    //Happy Path many customers Rate one tour
    @Test
    public void rateMany() {
        int ratings = service.lookupAll().size();
        service.rateMany(TOUR_ID, 5, new Integer[]{100, 101, 102});
        Assertions.assertEquals(ratings + 3, service.lookupAll().size());
    }

    //Unhappy Path, 2nd Invocation would create duplicates in the database, DataIntegrityViolationException thrown
    @Test
    public void rateManyProveRollback() {
        int ratings = service.lookupAll().size();
        Integer customers[] = {100, 101, 102};
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            service.rateMany(TOUR_ID, 3, customers);
            service.rateMany(TOUR_ID, 3, customers);
        });
    }

    //Happy Path, Update a Tour Rating already in the database
    @Test
    public void update() {
        createNew();
        TourRating tourRating = service.update(TOUR_ID, CUSTOMER_ID, 1, "one");
        Assertions.assertEquals(TOUR_ID, tourRating.getTour().getId());
        Assertions.assertEquals(CUSTOMER_ID, tourRating.getCustomerId());
        Assertions.assertEquals(1, tourRating.getScore());
        Assertions.assertEquals("one", tourRating.getComment());
    }

    //Unhappy path, no Tour Rating exists for tourId=1 and customer=1
    @Test
    public void updateException() throws Exception {
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            service.update(1, 1, 1, "one");
        });
    }

    //Happy Path, Update a Tour Rating already in the database
    @Test
    public void updateSome() {
        createNew();
        TourRating tourRating = service.update(TOUR_ID, CUSTOMER_ID, 1, "one");
        Assertions.assertEquals(TOUR_ID, tourRating.getTour().getId());
        Assertions.assertEquals(CUSTOMER_ID, tourRating.getCustomerId());
        Assertions.assertEquals(1, tourRating.getScore());
        Assertions.assertEquals("one", tourRating.getComment());
    }

    //Unhappy path, no Tour Rating exists for tourId=1 and customer=1
    @Test
    public void updateSomeException() throws Exception {
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            service.update(1, 1, 1, "one");
        });
    }

    //Happy Path get average score of a Tour.
    @Test
    public void getAverageScore() {
        Assertions.assertTrue(service.getAverageScore(TOUR_ID) == 4.0);
    }

    //UnHappy Path, Tour NOT_A_TOUR_ID does not exist
    @Test
    public void getAverageScoreException() {
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            service.getAverageScore(NOT_A_TOUR_ID); //That tour does not exist
        });
    }
}
