CREATE TABLE if NOT EXISTS car_detail(
    id SERIAL PRIMARY KEY,
    car_id INT NOT NULL REFERENCES cars(id),
    detail_id INT NOT NULL REFERENCES details(id)
);