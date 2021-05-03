-- Base
CREATE TABLE toppings (
    name        varchar(255),
    price       FLOAT             NOT NULL,
    cost        FLOAT             NOT NULL,
    inventory   FLOAT             NOT NULL,
    usmall      FLOAT             NOT NULL,
    umedium     FLOAT             NOT NULL,
    ularge      FLOAT             NOT NULL,
    uxlarge     FLOAT             NOT NULL,
    PRIMARY KEY (name)
);
CREATE TABLE discounts (
    name        varchar(255),
    percent     FLOAT,
    dollar      FLOAT,
    PRIMARY KEY (name)
);
CREATE TABLE baseprice (
    id           INT             NOT NULL,
    size         TEXT            NOT NULL,
    crust        TEXT            NOT NULL,
    price        FLOAT           NOT NULL,
    cost         FLOAT           NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE customer (
    id          INT              NOT NULL,
    name        TEXT             NOT NULL,
    phone       TEXT             NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE orders (
    id          INT              NOT NULL,
    price       FLOAT            NOT NULL,
    cost        FLOAT            NOT NULL,
    PRIMARY KEY (id)
);

-- Relational 
CREATE TABLE pizza (
    id          INT              NOT NULL,
    status      TEXT             NOT NULL,
    ordertime   TIMESTAMP        NOT NULL,
    cost        FLOAT            NOT NULL,
    price       FLOAT            NOT NULL,
    base        INT              NOT NULL,
    ordernum    INT              NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (base) REFERENCES baseprice(id),
    FOREIGN KEY (ordernum) REFERENCES orders(id)
);
CREATE TABLE pizzatoppings (
    pizza       INT              NOT NULL,
    topping     varchar(255)     NOT NULL,
    extra       BOOLEAN          DEFAULT false,
    PRIMARY KEY (pizza, topping),
    FOREIGN KEY (pizza) REFERENCES pizza(id),
    FOREIGN KEY (topping) REFERENCES toppings(name)
);
CREATE TABLE pizzadiscount (
    pizza         INT           NOT NULL,
    discount      varchar(255)  NOT NULL,
    PRIMARY KEY (pizza, discount),
    FOREIGN KEY (pizza) REFERENCES pizza(id),
    FOREIGN KEY (discount) REFERENCES discounts(name)
);
CREATE TABLE orderdiscount (
    ordernum      INT          NOT NULL,
    discount      varchar(255) NOT NULL,
    PRIMARY KEY (ordernum, discount),
    FOREIGN KEY (ordernum) REFERENCES orders(id),
    FOREIGN KEY (discount) REFERENCES discounts(name)
);
CREATE TABLE dinein (
    id            INT           NOT NULL,
    tablenum      INT           NOT NULL,
    ordernum      INT           NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (ordernum) REFERENCES orders(id)
);
CREATE TABLE seat (
    seat          INT           NOT NULL,
    ordernum      INT           NOT NULL,
    PRIMARY KEY (seat, ordernum),
    FOREIGN KEY (ordernum) REFERENCES dinein(id)
);
CREATE TABLE pickup (
    ordernum      INT           NOT NULL,
    customer      INT           NOT NULL,
    PRIMARY KEY (ordernum, customer),
    FOREIGN KEY (ordernum) REFERENCES orders(id),
    FOREIGN KEY (customer) REFERENCES customer(id)
);
CREATE TABLE deliverycustomer (
    id            INT           NOT NULL,
    customer      INT           NOT NULL,
    address       TEXT          NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (customer) REFERENCES customer(id)
);
CREATE TABLE delivery (
    ordernum      INT           NOT NULL,
    customer      INT           NOT NULL,
    PRIMARY KEY (ordernum, customer),
    FOREIGN KEY (ordernum) REFERENCES orders(id),
    FOREIGN KEY (customer) REFERENCES deliverycustomer(id)
);
