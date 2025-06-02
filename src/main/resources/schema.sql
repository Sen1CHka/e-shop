CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    realname VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(256) NOT NULL
);

CREATE TABLE IF NOT EXISTS orders (
    id_order SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    date DATE NOT NULL,
    state VARCHAR(20) NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS products (
    id_product SERIAL PRIMARY KEY,
    name VARCHAR(256) NOT NULL,
    description TEXT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    available_amount INTEGER NOT NULL
    );

CREATE TABLE IF NOT EXISTS product_in_orders (
    product_in_order INTEGER REFERENCES products,
    orders_with_product INTEGER REFERENCES orders
);

CREATE TABLE IF NOT EXISTS roles (
    id_role SERIAL PRIMARY KEY,
    role VARCHAR(256) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(username),
    FOREIGN KEY (role_id) REFERENCES roles(id_role)
);