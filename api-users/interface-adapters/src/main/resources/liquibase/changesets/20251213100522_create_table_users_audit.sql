CREATE TABLE IF NOT EXISTS users_audit (
    revision_id INTEGER NOT NULL,
    revision_type SMALLINT NOT NULL,

    id UUID NOT NULL,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(200) NOT NULL,
    role_id UUID NOT NULL,
    active BOOLEAN NOT NULL,
    created_by VARCHAR(50),
    created_date TIMESTAMP WITH TIME ZONE,
    last_modified_by VARCHAR(50),
    last_modified_date TIMESTAMP WITH TIME ZONE,

    PRIMARY KEY (revision_id, id),
    CONSTRAINT fk_users_audit_revision_info FOREIGN KEY (revision_id) REFERENCES revision_info(revision_id)
);
