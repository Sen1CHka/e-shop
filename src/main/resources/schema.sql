CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(50) PRIMARY KEY ,
    realname VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(256) NOT NULL
);

CREATE TABLE IF NOT EXISTS orders (
    id_order SERIAL PRIMARY KEY,
    id_user INTEGER NOT NULL REFERENCES users,
    date DATE NOT NULL,
    state VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS products (
    id_product SERIAL PRIMARY KEY,
    name VARCHAR(256) NOT NULL,
    description TEXT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    avaliable_amount INTEGER NOT NULL
    );

CREATE TABLE IF NOT EXISTS product_in_orders (
    product_in_order INTEGER REFERENCES products,
    orders_with_product INTEGER REFERENCES orders
);