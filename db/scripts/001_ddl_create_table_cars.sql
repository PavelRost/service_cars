CREATE TABLE if NOT EXISTS cars(
    id SERIAL PRIMARY KEY,
    vin VARCHAR(20) NOT NULL,
    reg_number VARCHAR(10) NOT NULL,
    manufacturer VARCHAR(50) NULL,
    brand VARCHAR(20) NULL,
    year_manufacture VARCHAR(4) NULL
);