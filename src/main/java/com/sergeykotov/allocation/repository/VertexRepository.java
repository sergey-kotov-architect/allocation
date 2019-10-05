package com.sergeykotov.allocation.repository;

import com.sergeykotov.allocation.domain.Vertex;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VertexRepository extends CrudRepository<Vertex, Long> {
}