package com.example.ec.repo;

import com.example.ec.domain.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;


public interface TourRepository extends PagingAndSortingRepository<Tour, Integer> {
    @RestResource(exported = false)
    Page<Tour> findByTourPackageCode(String code, Pageable pageable);
}
