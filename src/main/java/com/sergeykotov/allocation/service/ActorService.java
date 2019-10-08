package com.sergeykotov.allocation.service;

import com.sergeykotov.allocation.dao.ActorDao;
import com.sergeykotov.allocation.domain.Actor;
import com.sergeykotov.allocation.exception.DataModificationException;
import com.sergeykotov.allocation.exception.ExtractionException;
import com.sergeykotov.allocation.exception.InvalidDataException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class ActorService {
    private static final Logger log = Logger.getLogger(ActorService.class);
    private final ActorDao actorDao;
    private final GraphService graphService;

    @Autowired
    public ActorService(ActorDao actorDao, GraphService graphService) {
        this.actorDao = actorDao;
        this.graphService = graphService;
    }

    public void create(Actor actor) {
        log.info("creating actor... " + actor);
        try {
            actorDao.create(actor);
        } catch (SQLException e) {
            log.error("failed to create actor " + actor, e);
            throw new InvalidDataException();
        }
        log.info("actor has been created " + actor);
    }

    public List<Actor> getAll() {
        try {
            return actorDao.getAll();
        } catch (SQLException e) {
            log.error("failed to extract actors", e);
            throw new ExtractionException();
        }
    }

    public Actor getById(long id) {
        try {
            return actorDao.getAll().stream()
                    .filter(a -> a.getId() == id)
                    .findAny().orElseThrow(InvalidDataException::new);
        } catch (SQLException e) {
            log.error("failed to extract actor by id " + id, e);
            throw new InvalidDataException();
        }
    }

    public void update(Actor actor) {
        if (graphService.isGenerating()) {
            throw new DataModificationException();
        }
        log.info("updating actor... " + actor);
        try {
            actorDao.update(actor);
        } catch (SQLException e) {
            log.error("failed to update actor " + actor, e);
            throw new InvalidDataException();
        }
        log.info("actor has been updated " + actor);
    }

    public void deleteById(long id) {
        log.info("deleting actor by id " + id);
        try {
            actorDao.deleteById(id);
        } catch (SQLException e) {
            log.error("failed to delete actor by id " + id, e);
            throw new InvalidDataException();
        }
        log.info("actor has been deleted by id " + id);
    }
}