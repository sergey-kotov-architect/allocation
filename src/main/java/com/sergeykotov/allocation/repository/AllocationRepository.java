package com.sergeykotov.allocation.repository;

import com.sergeykotov.allocation.domain.Allocation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllocationRepository extends CrudRepository<Allocation, Long> {
}