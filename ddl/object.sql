
drop table objects;

CREATE TABLE objects (
    id              CHAR(36) NOT NULL PRIMARY KEY,
    description     VARCHAR(255) NOT NULL,
    address_Id      INT NOT NULL,
    price           FLOAT NOT NULL,
    number_rooms    INT NOT NULL,
    number_bathrooms INT NOT NULL,
    has_garage      BOOLEAN DEFAULT FALSE,
    garage_size     INT,
    status          ENUM('ACTIVE', 'INACTIVE', 'PENDING', 'SOLD') NOT NULL,
    created_at      DATETIME(6) NOT NULL,
    updated_at      DATETIME(6) NOT NULL,
    thumbnail_url   VARCHAR(255)
);