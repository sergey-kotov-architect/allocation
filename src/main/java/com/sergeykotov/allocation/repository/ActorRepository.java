package com.sergeykotov.allocation.repository;

import com.sergeykotov.allocation.domain.Actor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepository extends CrudRepository<Actor, Long> {
}