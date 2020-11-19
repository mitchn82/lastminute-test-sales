Sales Taxes app
====================
This is Spring Boot Application that runs a RESTFul web server to manage a basic shopping basket. It uses an embedded H2 im memory database with an init script from from src/main/resources/data.sql. It exposes RESTFul services to create a basket, and to add and remote items. Every time an item is added or removed, sales taxes and total are recalculated on the fly and saved into db.  

Main entities are 
 - _Items_: it describe a good that can be buyed
 - _Tax_: a definition of sale tax
 - _ShoppingBasket_: it describe a basket where items can be added
 

Integration tests 
---------------------
[ShoppingBasketServiceTest.java](https://github.com/mitchn82/lastminute-test-sales/blob/main/src/test/java/com/lastiminute/test/sales/services/ShoppingBasketServiceTest.java) class uses an embedded H2 database with initial data loaded from src/test/resources/data.sql file via Spring Boot test lib.
It solves the main goal of application, to calculate exact amount of taxes and totals for three given baskets.

RESTFul usage
---------------------
 - GET http://localhost:28080/sales/items
 Returns all items in db
 - POST http://localhost:28080/sales/baskets
 Create an empty basket, it gives the id. On startup it is '1'
 - POST http://localhost:28080/sales/baskets/1/items/1
 Add item 1 (book: 12.49) to basket 1. Return updated basket
 - POST http://localhost:28080/sales/baskets/1/items/2
 Add item 2 (music CD: 16.49) to basket 1. Return updated basket
 - POST http://localhost:28080/sales/baskets/1/items/3
 Add item 3 (chocolate bar: 0.85) to basket 1. Return updated basket

Last basket update shows final output for input 1

Same could be tested with input 2 and input 3.
