# Homework #8

```Sammy El-Sherif``` ```Java``` ```SQL```

## Classes
### Store
Is the class representing the store which data is stored in the database.
The store has a list of products and list of orders. 

### Product
Products sold by the store. Has a code, description and price. 

### Order
Custormer's orders.
Each order has an order number (string), date and a list of order lines. 

### Order Line
Each order line shows the details about the order. 
In particular, which product was order and how many. 


## Database Connection Configuration File. 
The database is a MySQL server as we exemplified in the lecture recordings.
**Remember you require VPN access to connect to the server**

The file `db.cfg` stored in the `src` directory has the connection configuration. 
The file has exactly 5 lines; do not add extra spaces or blank lines.  

```
HOSTNAME:cse-mllab-pastorino.ucdenver.pvt
PORT:3399
USERNAME:guest
PASSWORD:guest
DATABASE:store
``` 

## DATABASE SCHEMA
The database schema is `store`. To avoid issues, it's recommended to prefix the table name with the schema.
E.g. `store.product`

### `product` table
This is the table that stores the product information. 

| Attribute   | Type            |
|-------------|-----------------|
| code        | varchar(255)    |
| description | varchar(255)    |
| price       | decimal(10,2) * |
 \* this mean is a double with 2 decimals. 

### `purchase_order` table
This is the table storing the Order information. Requires the secondary access to `order_line` table to get the details of the order.

| Attribute | Type |
|---|----|
|order_number|varchar(255)|
|order_date|date|

### `order_line` table 
This table stores the details of the order. I.e. the lines (products and quantities) ordered.

| Attribute    | Type         |
|--------------|--------------|
| order_number | varchar(255) |
| product_code | varchar(255) |
| quantity     | int          |


## SQL QUERIES 
* `SELECT code, description, price FROM store.product WHERE code = ?`
* `SELECT code, description, price FROM store.product`
* `SELECT order_number, order_date FROM store.purchase_order WHERE order_number = ?`
* `SELECT order_number, order_date FROM store.purchase_order`
* `SELECT order_number, order_date FROM store.purchase_order WHERE order_date BETWEEN ? and ?`
* `SELECT PO.order_number FROM store.purchase_order as PO, store.order_line L WHERE PO.order_number = L.order_number and L.product_code = ?`
* `SELECT order_number, product_code, quantity FROM store.order_line WHERE order_number = ?`

