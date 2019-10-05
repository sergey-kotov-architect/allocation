CREATE TABLE actor (
  id    INTEGER PRIMARY KEY,
  name  TEXT    NOT NULL UNIQUE,
  note  TEXT,
  speed REAL    NOT NULL CHECK (speed > 0)
);

CREATE TABLE vertex (
  id   INTEGER PRIMARY KEY,
  name TEXT    NOT NULL UNIQUE,
  note TEXT,
  rank REAL    NOT NULL CHECK (rank > 0)
);

CREATE TABLE edge (
  id             INTEGER PRIMARY KEY,
  name           TEXT    NOT NULL UNIQUE,
  note           TEXT,
  speed_limit    REAL    NOT NULL CHECK (speed_limit > 0),
  distance       REAL    NOT NULL CHECK (distance > 0),
  source_id      INTEGER NOT NULL REFERENCES vertex (id) ON DELETE RESTRICT,
  destination_id INTEGER NOT NULL REFERENCES vertex (id) ON DELETE RESTRICT
);

CREATE TABLE allocation (
  id         INTEGER PRIMARY KEY,
  actor_id   INTEGER NOT NULL REFERENCES actor (id) ON DELETE RESTRICT,
  vertex_id  INTEGER NOT NULL REFERENCES vertex (id) ON DELETE RESTRICT,
  actor_rank REAL    NOT NULL CHECK (actor_rank > 0),
  active     BOOLEAN,
  note       TEXT,
  UNIQUE (actor_id, vertex_id)
);

CREATE TRIGGER verify_allocation_insert BEFORE INSERT ON allocation WHEN NEW.active = 1
BEGIN
  SELECT CASE WHEN EXISTS (SELECT 1 FROM allocation a WHERE a.active = 1 AND a.actor_id = NEW.actor_id)
  THEN RAISE(ABORT, "invalid allocation")
  END;
END;

CREATE TRIGGER verify_allocation_update BEFORE UPDATE ON allocation WHEN NEW.active = 1
BEGIN
  SELECT CASE WHEN EXISTS
      (SELECT 1 FROM allocation a WHERE a.active = 1 AND a.actor_id = NEW.actor_id AND a.id <> NEW.id)
  THEN RAISE(ABORT, "invalid allocation")
  END;
END;