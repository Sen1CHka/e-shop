CREATE TABLE IF NOT EXISTS orders (
    id_order SERIAL NOT NULL,
    id_user INTEGER NOT NULL,
    date DATE NOT NULL,
    state VARCHAR(20) NOT NULL,
    PRIMARY KEY (id_order, id_user)
    );

CREATE TABLE IF NOT EXISTS products (
    id_product SERIAL NOT NULL,
    name VARCHAR(256) NOT NULL,
    description TEXT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    available_amount INTEGER NOT NULL,
    PRIMARY KEY (id_product)
    );

CREATE TABLE IF NOT EXISTS users (
    id_user SERIAL NOT NULL,
    username VARCHAR(50) NOT NULL,
    realname VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(256) NOT NULL,
    PRIMARY KEY (id_user)
    );

CREATE TABLE IF NOT EXISTS product_order (
    id_product INTEGER NOT NULL,
    id_order INTEGER NOT NULL,
    id_user INTEGER NOT NULL,
    PRIMARY KEY (id_product, id_order, id_user)
);