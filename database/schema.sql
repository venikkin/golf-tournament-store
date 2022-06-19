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
    UNIQUE KEY idx_provider_externalId (provider, externalId)
)
