package com.sergeykotov.allocation.service;

import com.sergeykotov.allocation.domain.Actor;
import com.sergeykotov.allocation.exception.ExtractionException;
import com.sergeykotov.allocation.exception.InvalidDataException;
import com.sergeykotov.allocation.repository.ActorRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActorService {
    private static final Logger log = Logger.getLogger(ActorService.class);
    private final ActorRepository actorRepository;

    @Autowired
    public ActorService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public void create(Actor actor) {
        log.info("creating actor... " + actor);
        try {
            actorRepository.save(actor);
        } catch (Exception e) {
            log.error("failed to create actor " + actor, e);
            throw new InvalidDataException();
        }
        log.info("actor has been created " + actor);
    }

    public List<Actor> getAll() {
        List<Actor> actors = new ArrayList<>();
        try {
            actorRepository.findAll().forEach(actors::add);
        } catch (Exception e) {
            log.error("failed to extract actors", e);
            throw new ExtractionException();
        }
        return actors;
    }

    public Actor getById(long id) {
        try {
            return actorRepository.findById(id).orElseThrow(InvalidDataException::new);
        } catch (Exception e) {
            log.error("failed to extract actor by id " + id, e);
            throw new InvalidDataException();
        }
    }

    public void update(Actor actor) {
        log.info("updating actor... " + actor);
        try {
            actorRepository.save(actor);
        } catch (Exception e) {
            log.error("failed to update actor " + actor, e);
            throw new InvalidDataException();
        }
        log.info("actor has been updated " + actor);
    }

    public void deleteById(long id) {
        log.info("deleting actor by id " + id);
        try {
            actorRepository.deleteById(id);
        } catch (Exception e) {
            log.error("failed to delete actor by id " + id, e);
            throw new InvalidDataException();
        }
        log.info("actor has been deleted by id " + id);
    }
}