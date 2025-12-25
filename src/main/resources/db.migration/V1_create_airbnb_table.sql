

CREATE TABLE airbnb (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) ,
    description VARCHAR(255),
    pricePerNight VARCHAR(255) NOT NULL,
    location VARCHAR(255)

);