package com.learn.repository;

import com.learn.model.PageClazz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageClazzRepository extends JpaRepository<PageClazz, Long> {
}
