-- Toppings table
INSERT INTO toppings VALUES ("Pepperoni",1.25,0.2,100,2,2.75,3.5,4.5);
INSERT INTO toppings VALUES ("Sausage",1.25,0.15,100,2.5,3,3.5,4.25);
INSERT INTO toppings VALUES ("Ham",1.5,0.15,78,2,2.5,3.25,4);
INSERT INTO toppings VALUES ("Chicken",1.75,0.25,56,1.5,2,2.25,3);
INSERT INTO toppings VALUES ("Green Pepper",0.5,0.02,79,1,1.5,2,2.5);
INSERT INTO toppings VALUES ("Onion",0.5,0.02,85,1,1.5,2,2.75);
INSERT INTO toppings VALUES ("Roma tomato",0.75,0.03,86,2,3,3.5,4.5);
INSERT INTO toppings VALUES ("Mushrooms",0.75,0.1,52,1.5,2,2.5,3);
INSERT INTO toppings VALUES ("Black Olives",0.6,0.1,39,0.75,1,1.5,2);
INSERT INTO toppings VALUES ("Pineapple",1,0.25,15,1,1.25,1.75,2);
INSERT INTO toppings VALUES ("Jalapenos",0.5,0.05,64,0.5,0.75,1.25,1.75);
INSERT INTO toppings VALUES ("Banana Peppers",0.5,0.05,36,0.6,1,1.3,1.75);
INSERT INTO toppings VALUES ("Regular Cheese",1.5,0.12,250,2,3.5,5,7);
INSERT INTO toppings VALUES ("Four Cheese Blend",2,0.15,150,2,3.5,5,7);
INSERT INTO toppings VALUES ("Feta Cheese",2,0.18,75,1.75,3,4,5.5);
INSERT INTO toppings VALUES ("Goat Cheese",2,0.2,54,1.6,2.75,4,5.5);
INSERT INTO toppings VALUES ("Bacon",1.5,0.25,89,1,1.5,2,3);
--
--
-- Discounts table
INSERT INTO discounts VALUES ("employee", 15, NULL);
INSERT INTO discounts VALUES ("Lunch Special Medium", NULL, 1);
INSERT INTO discounts VALUES ("Lunch Special Large", NULL, 2);
INSERT INTO discounts VALUES ("Specialty Pizza", NULL, 1.5);
INSERT INTO discounts VALUES ("Gameday special", 20, NULL);
--
--
-- Baseprice table
INSERT INTO baseprice VALUES (1,"small","Thin",3,0.5);
INSERT INTO baseprice VALUES (2,"small","Original",3,0.75);
INSERT INTO baseprice VALUES (3,"small","Pan",3.5,1);
INSERT INTO baseprice VALUES (4,"small","Gluten-Free",4,2);
INSERT INTO baseprice VALUES (5,"medium","Thin",5,1);
INSERT INTO baseprice VALUES (6,"medium","Original",5,1.5);
INSERT INTO baseprice VALUES (7,"medium","Pan",6,2.25);
INSERT INTO baseprice VALUES (8,"medium","Gluten-Free",6.25,3);
INSERT INTO baseprice VALUES (9,"Large","Thin",8,1.25);
INSERT INTO baseprice VALUES (101,"Large","Original",8,2);
INSERT INTO baseprice VALUES (102,"Large","Pan",9,3);
INSERT INTO baseprice VALUES (103,"Large","Gluten-Free",9.5,4);
INSERT INTO baseprice VALUES (104,"X-Large","Thin",10,2);
INSERT INTO baseprice VALUES (105,"X-Large","Original",10,3);
INSERT INTO baseprice VALUES (106,"X-Large","Pan",11.5,4.5);
INSERT INTO baseprice VALUES (107,"X-Large","Gluten-Free",12.5,6);
--
--
--
-- Orders
-- 1
INSERT INTO orders VALUES (1, 13.50, 3.68); -- Create order
INSERT INTO pizza VALUES (1, "Completed", '2020-03-05 12:03:00', 13.50, 3.68, 9, 1);
INSERT INTO pizzatoppings VALUES (1, "Regular Cheese", true), (1, "Pepperoni", false), (1, "Sausage", false);
INSERT INTO orderdiscount VALUES (1, "Lunch Special Large");
INSERT INTO dinein VALUES (1, 14, 1);
INSERT INTO seat VALUES (1, 1), (2, 1), (3, 1);
--
--
-- 2
INSERT INTO orders VALUES (2, 10.60, 3.23);
INSERT INTO pizza VALUES (2, "Completed", '2020-03-03 12:05:00', 10.60, 3.23, 7, 2);
INSERT INTO pizzatoppings VALUES (2, "Feta Cheese", false), (2, "Black Olives", false), (2, "Roma tomato", false), (2, "Mushrooms", false), (2, "Banana Peppers", false);
INSERT INTO orderdiscount VALUES (2, "Lunch Special Medium");
INSERT INTO pizzadiscount VALUES (2, "Specialty Pizza");
INSERT INTO dinein VALUES (2, 4, 2);
INSERT INTO seat VALUES (1, 2);
-- 2.5
INSERT INTO orders VALUES (3, 6.75, 1.40);
INSERT INTO pizza VALUES (3, "Completed", '2020-03-03 12:05:00', 6.75, 1.40, 2, 3);
INSERT INTO pizzatoppings VALUES (3, "Regular Cheese", false), (3, "Chicken", false), (3, "Banana Peppers", false);
INSERT INTO dinein VALUES (3, 4, 3);
INSERT INTO seat VALUES (2, 3);
--
--
-- 3
INSERT INTO orders VALUES (4, 10.75 * 6, 3.30 * 6);
INSERT INTO pizza VALUES (4, "Completed", '2020-03-03 21:30:00', 10.75, 3.30, 101, 4),
(5, "Completed", '2020-03-03 21:30:00', 10.75, 3.30, 101, 4),
(6, "Completed", '2020-03-03 21:30:00', 10.75, 3.30, 101, 4),
(7, "Completed", '2020-03-03 21:30:00', 10.75, 3.30, 101, 4),
(8, "Completed", '2020-03-03 21:30:00', 10.75, 3.30, 101, 4),
(9, "Completed", '2020-03-03 21:30:00', 10.75, 3.30, 101, 4);
INSERT INTO pizzatoppings VALUES (4, "Regular Cheese", false), (4, "Pepperoni", false),
(5, "Regular Cheese", false), (5, "Pepperoni", false),
(6, "Regular Cheese", false), (6, "Pepperoni", false),
(7, "Regular Cheese", false), (7, "Pepperoni", false),
(8, "Regular Cheese", false), (8, "Pepperoni", false),
(9, "Regular Cheese", false), (9, "Pepperoni", false);
INSERT INTO customer VALUES (1, "Andrew Wilkes-Krier", "864-254-5861");
INSERT INTO pickup VALUES (4, 1);
--
--
-- 4
INSERT INTO orders VALUES (5, 14.50 + 17 + 14, 5.59 + 5.59 + 5.68);
INSERT INTO pizza VALUES (10, "Completed", '2020-03-05 19:11:00', 14.50, 5.59, 105, 5),
(11, "Completed", '2020-03-05 19:11:00', 17.00, 5.59, 105, 5),
(12, "Completed", '2020-03-05 19:11:00', 14.00, 5.68, 105, 5);
INSERT INTO pizzatoppings VALUES (11, "Four Cheese Blend", false), (11, "Ham", true), (11, "Pineapple", true),
(12, "Four Cheese Blend", false), (12, "Jalapenos", false), (12, "Bacon", false),
(10, "Four Cheese Blend", false), (10, "Pepperoni", false), (10, "Sausage", false);
INSERT INTO orderdiscount VALUES (5, "Gameday special");
INSERT INTO pizzadiscount VALUES (11, "Specialty Pizza");
INSERT INTO deliverycustomer VALUES (1, 1, "115 Party Blvd, Anderson SC 29621");
INSERT INTO delivery VALUES (5, 1);
--
--
-- 5
INSERT INTO orders VALUES (6, 16.85, 7.85);
INSERT INTO pizza VALUES (13, "Completed", '2020-03-02 17:30:00', 16.85, 7.85, 107, 6);
INSERT INTO pizzatoppings VALUES (13, "Green Pepper", false),
(13, "Onion", false),
(13, "Roma tomato", false),
(13, "Mushrooms", false),
(13, "Black Olives", false),
(13, "Goat Cheese", false);
INSERT INTO pizzadiscount VALUES (13, "Specialty Pizza");
INSERT INTO customer VALUES (2, "Matt Engers", "864-474-9953");
INSERT INTO pickup VALUES (6, 2);
--
--
-- 6
INSERT INTO orders VALUES (7, 13.25, 3.20);
INSERT INTO pizza VALUES (14, "Completed", '2020-03-02 18:17:00', 13.25, 3.20, 9, 7);
INSERT INTO pizzatoppings VALUES (14, "Chicken", false),
(14, "Green Pepper", false),
(14, "Onion", false),
(14, "Mushrooms", false),
(14, "Four Cheese Blend", false);
INSERT INTO customer VALUES (3, "Frank Turner", "864-232-8944");
INSERT INTO deliverycustomer VALUES (2, 3, "6745 Wessex St. Anderson SC 29621");
INSERT INTO delivery VALUES (7, 2);
--
--
-- 7
INSERT INTO orders VALUES (8, 12 + 12, 3.75 + 2.55);
INSERT INTO pizza VALUES (15, "Completed", '2020-03-06 20:32:00', 12, 3.75, 9, 8),
(16, "Completed", '2020-03-06 20:32:00', 12, 2.55, 9, 8);
INSERT INTO pizzatoppings VALUES (15, "Four Cheese Blend", true),
(16, "Regular Cheese", false), (16, "Pepperoni", true);
INSERT INTO customer VALUES (4, "Milo Auckerman", "864-878-5679");
INSERT INTO deliverycustomer VALUES (3, 4, "8879 Suburban Home, Anderson, SC 29621");
INSERT INTO delivery VALUES (8, 3);
INSERT INTO orderdiscount VALUES(8,"employee");
