package com.sergeykotov.allocation.service;

import com.sergeykotov.allocation.dao.ActorDao;
import com.sergeykotov.allocation.domain.Actor;
import com.sergeykotov.allocation.exception.DataModificationException;
import com.sergeykotov.allocation.exception.ExtractionException;
import com.sergeykotov.allocation.exception.InvalidDataException;
import com.sergeykotov.allocation.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class ActorService {
    private static final Logger log = LoggerFactory.getLogger(ActorService.class);

    private final ActorDao actorDao;
    private final GraphService graphService;

    @Autowired
    public ActorService(ActorDao actorDao, GraphService graphService) {
        this.actorDao = actorDao;
        this.graphService = graphService;
    }

    public void create(Actor actor) {
        log.info("creating actor " + actor + "...");
        try {
            actorDao.create(actor);
        } catch (SQLException e) {
            log.error("failed to create actor " + actor + ", error code: " + e.getErrorCode(), e);
            throw new InvalidDataException();
        }
        log.info("actor " + actor + " has been created");
    }

    public List<Actor> getAll() {
        log.info("extracting actors...");
        List<Actor> actors;
        try {
            actors = actorDao.getAll();
        } catch (SQLException e) {
            log.error("failed to extract actors, error code: " + e.getErrorCode(), e);
            throw new ExtractionException();
        }
        log.info(actors.size() + " actors have been extracted");
        return actors;
    }

    public Actor getById(long id) {
        try {
            return actorDao.getAll().stream()
                    .filter(a -> a.getId() == id)
                    .findAny().orElseThrow(NotFoundException::new);
        } catch (SQLException e) {
            log.error("failed to extract actor by ID " + id + ", error code: " + e.getErrorCode(), e);
            throw new InvalidDataException();
        }
    }

    public void updateById(long id, Actor actor) {
        if (graphService.isGenerating()) {
            throw new DataModificationException();
        }
        log.info("updating actor by ID " + id + "...");
        try {
            actorDao.updateById(id, actor);
        } catch (SQLException e) {
            log.error("failed to update actor by ID " + id + ", error code: " + e.getErrorCode(), e);
            throw new InvalidDataException();
        }
        log.info("actor has been updated by ID " + id);
    }

    public void deleteById(long id) {
        log.info("deleting actor by ID " + id);
        try {
            actorDao.deleteById(id);
        } catch (SQLException e) {
            log.error("failed to delete actor by ID " + id + ", error code: " + e.getErrorCode(), e);
            throw new InvalidDataException();
        }
        log.info("actor has been deleted by ID " + id);
    }
}