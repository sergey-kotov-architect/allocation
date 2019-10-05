package com.sergeykotov.allocation.repository;

import com.sergeykotov.allocation.domain.Edge;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EdgeRepository extends CrudRepository<Edge, Long> {
}