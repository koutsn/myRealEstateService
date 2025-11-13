CREATE TABLE objectImages (
    id CHAR(36) NOT NULL PRIMARY KEY,
    objectId CHAR(36),
    description CHAR(200),
	uri CHAR(200)
);
