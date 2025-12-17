CREATE TABLE IF NOT EXISTS customers_audit (
     revision_id INTEGER NOT NULL,
     revision_type SMALLINT NOT NULL,

    id UUID NOT NULL,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(200) NOT NULL,
    user_id UUID NOT NULL,
    active BOOLEAN NOT NULL,
    created_by VARCHAR(50),
    created_date TIMESTAMP WITH TIME ZONE,
    last_modified_by VARCHAR(50),
    last_modified_date TIMESTAMP WITH TIME ZONE,

    PRIMARY KEY (revision_id, id),
    CONSTRAINT fk_customers_audit_revision_info FOREIGN KEY (revision_id) REFERENCES revision_info(revision_id)
);

