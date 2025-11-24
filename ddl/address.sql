
drop table address;

CREATE TABLE addresses (
    id        CHAR(36) NOT NULL PRIMARY KEY,
    street    VARCHAR(255) NOT NULL,
    zip_code  VARCHAR(50) NOT NULL,
    city      VARCHAR(255) NOT NULL,
    country   VARCHAR(255) NOT NULL
);