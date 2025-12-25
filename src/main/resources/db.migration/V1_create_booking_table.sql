CREATE TABLE booking(
  id BIGINT AUTO_INCREMENT PRIMARY KEY,

    user_id BIGINT NOT NULL,
    airbnb_id BIGINT NOT NULL,

    total_price DECIMAL(10,2),

    booking_status VARCHAR(20) NOT NULL,
    idempotency_key VARCHAR(255) UNIQUE,

       CONSTRAINT fk_booking_user
            FOREIGN KEY (user_id)
            REFERENCES users(id),

        CONSTRAINT fk_booking_airbnb
            FOREIGN KEY (airbnb_id)
            REFERENCES airbnb(id)


);