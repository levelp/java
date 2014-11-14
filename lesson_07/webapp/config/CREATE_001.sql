CREATE TABLE resume (
  uuid      CHAR(36) PRIMARY KEY NOT NULL,
  full_name TEXT                 NOT NULL,
  location  TEXT
);

CREATE TABLE contact (
  id          SERIAL,
  resume_uuid CHAR(36) NOT NULL,
  type        TEXT     NOT NULL,
  value       TEXT     NOT NULL,
  CONSTRAINT contant_pkey PRIMARY KEY (id),
  CONSTRAINT contact_fk FOREIGN KEY (resume_uuid)
  REFERENCES resume (uuid)
  ON DELETE CASCADE
  ON UPDATE NO ACTION
  NOT DEFERRABLE
)
WITH (OIDS = FALSE);

CREATE UNIQUE INDEX contact_idx ON contact
USING BTREE (resume_uuid, type);

CREATE TABLE TEXT_SECTION
(
  id          SERIAL,
  resume_uuid CHAR(36) NOT NULL,
  type        VARCHAR  NOT NULL,
  values      VARCHAR  NOT NULL,
  CONSTRAINT text_section_pkey PRIMARY KEY (id),
  FOREIGN KEY (resume_uuid) REFERENCES resume (uuid) ON DELETE CASCADE
);
