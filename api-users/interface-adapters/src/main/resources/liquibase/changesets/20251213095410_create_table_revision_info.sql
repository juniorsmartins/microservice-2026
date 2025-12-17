CREATE SEQUENCE revinfo_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE IF NOT EXISTS revision_info (
    revision_id BIGINT PRIMARY KEY,
    revision_timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
    usuario VARCHAR(50)
);

ALTER TABLE revision_info ALTER COLUMN revision_id SET DEFAULT NEXTVAL('revinfo_seq');
