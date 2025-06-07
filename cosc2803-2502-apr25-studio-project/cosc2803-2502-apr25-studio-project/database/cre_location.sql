--
-- File generated with SQLiteStudio v3.4.17 on Mon Apr 7 11:03:37 2025
--
-- Text encoding used: System
--
PRAGMA foreign_keys = off;
BEGIN TRANSACTION;

-- Table: Location
DROP TABLE IF EXISTS Location;

CREATE TABLE Location (
    Site,
    Name,
    Lat,
    Long,
    State,
    Region
);


COMMIT TRANSACTION;
PRAGMA foreign_keys = on;
