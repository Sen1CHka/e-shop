# E-shop

A web page for an online store. A user (<b>User</b>) can create orders (<b>Order</b>) containing a list of products (<b>Product</b>). Each product can appear in multiple orders, and each order can contain multiple products.

## Schema

![Schema](databaseschema.jpg)

## Query

### Formulation
Select all orders up to a specified date.
### Usage
Order output filter.

### Formulation
Select all products that are cheaper than a specified price.
### Usage
Filter to remove products above the price limit.

## Setup and Running
Swagger : localhost:8090/swagger-ui.html
To run the application, both repositories and all required tools must be installed.

### Requirements

 - JVM
 - Gradle
 - Node.js

## Running the Application

### Client: 
In the directory `eshop/selling-frontend`

```bash
npm start
