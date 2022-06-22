/**
  This is possible schema. In the production environment it could be beneficial to use database migration utility (for example: https://www.liquibase.org/).
  This will allow incrementally develop and deploy database structure will much auditable way. Most of the migration tools allow to define schema via
  vendor-agnostic DSL.

  The list of indices could be extended as well. In this example, I assume there will be queries by tournament id. Provider in included into unique index as I
  assume external IDs may not be unique globally, but must be unique for each provider. Name field is indexed as an example, as searching by name is generally useful.

  If database if used as a provider store as well, provider column could be replaced with foreign key constraint to meet DB normal forms.
 */

CREATE TABLE IF NOT EXISTS tournament (
    id bigint NOT NULL AUTO_INCREMENT,
    `name` varchar(255) NOT NULL,
    courseName varchar(255) NOT NULL,
    countryCode varchar(16) NOT NULL,
    startDate date NOT NULL,
    endDate date NOT NULL,
    creationTimestamp timestamp(3) NOT NULL,
    rounds int NOT NULL,
    playerCount int,
    forecast varchar(16),
    externalId varchar(36) NOT NULL,
    provider varchar(36) NOT NULL,

    PRIMARY KEY (id),
    UNIQUE KEY idx_provider_externalId (provider, externalId),
    INDEX idx_name (name)
)
