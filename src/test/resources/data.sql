/**
 * Author:  mitch
 * Created: 18-nov-2020
 */

insert into item(id, name, price, imported, type) values (1, 'book', 12.49, false, 'BOOK');
insert into item(id, name, price, imported, type) values (2, 'music CD', 14.99, false, 'GENERIC');
insert into item(id, name, price, imported, type) values (3, 'chocolate bar', 0.85, false, 'FOOD');

insert into item(id, name, price, imported, type) values (4, 'box of chocolates 1', 10.00, true, 'FOOD');
insert into item(id, name, price, imported, type) values (5, 'bottle of perfume 1', 47.50, true, 'GENERIC');

insert into item(id, name, price, imported, type) values (6, 'bottle of perfume 2', 27.99, true, 'GENERIC');
insert into item(id, name, price, imported, type) values (7, 'bottle of perfume', 18.99, false, 'GENERIC');
insert into item(id, name, price, imported, type) values (8, 'packet of headache pills', 9.75, false, 'MEDICAL');
insert into item(id, name, price, imported, type) values (9, 'box of imported chocolates', 11.25, true, 'FOOD');

insert into tax(id, rate, type) values (1, 10, 'BASIC');
insert into tax(id, rate, type) values (2, 5, 'IMPORTED');

/* INPUT 1 */
insert into shopping_basket(id, taxes, total) values (1, 1.50, 29.83);

insert into shopping_basket_item(id, basket_id, item_id, quantity) values (1, 1, 1, 1);
insert into shopping_basket_item(id, basket_id, item_id, quantity) values (2, 1, 2, 1);
insert into shopping_basket_item(id, basket_id, item_id, quantity) values (3, 1, 3, 1);

/* INPUT 2 */
insert into shopping_basket(id, taxes, total) values (2, 7.65, 65.15);

insert into shopping_basket_item(id, basket_id, item_id, quantity) values (4, 2, 4, 1);
insert into shopping_basket_item(id, basket_id, item_id, quantity) values (5, 2, 5, 1);

/* INPUT 3 */
insert into shopping_basket(id, taxes, total) values (3, 6.70, 74.68);

insert into shopping_basket_item(id, basket_id, item_id, quantity) values (6, 3, 6, 1);
insert into shopping_basket_item(id, basket_id, item_id, quantity) values (7, 3, 7, 1);
insert into shopping_basket_item(id, basket_id, item_id, quantity) values (8, 3, 8, 1);
insert into shopping_basket_item(id, basket_id, item_id, quantity) values (9, 3, 9, 1);