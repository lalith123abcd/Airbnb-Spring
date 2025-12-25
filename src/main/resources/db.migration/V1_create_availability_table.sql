
CREATE TABLE availability (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    airbnb_id BIGINT NOT NULL,
    date VARCHAR(255) NOT NULL,

    booking_id BIGINT NULL
);
