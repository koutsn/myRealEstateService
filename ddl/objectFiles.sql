
drop table object_files;

CREATE TABLE object_files (
    id CHAR(36) NOT NULL PRIMARY KEY,
    object_id CHAR(36) NOT NULL,
    name CHAR(200),
	file_name CHAR(100) NOT NULL,
    original_filename CHAR(100) NOT NULL,
    url CHAR(100) NOT NULL
);
