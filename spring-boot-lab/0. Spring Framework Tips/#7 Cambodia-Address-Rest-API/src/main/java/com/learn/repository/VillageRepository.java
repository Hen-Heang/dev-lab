package com.learn.repository;

import com.learn.model.Village;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VillageRepository extends PagingAndSortingRepository<Village, Long> {

}
