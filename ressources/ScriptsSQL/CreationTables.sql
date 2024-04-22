DROP TABLE contient;
DROP TABLE compose;
DROP TABLE commande;
DROP TABLE Pizza;
DROP TABLE Ingredient;
DROP TABLE usersRest;
Create table ingredient(
    ino serial,
    nom varchar(255),
    prix real,
    constraint pk_ino PRIMARY KEY(ino)
);

INSERT INTO ingredient VALUES(1,'Saumon',6.0),(2,'Chèvre',3.0),(3,'Miel',2.0),(4,'Poivrons',1.5),(5,'Mozzarella',1.5),(6,'ciboulette',0.5),(7,'capres',2),(8,'Creme fraiche',2);

CREATE table pizza(
    pno serial,
    nom varchar(255),
    prix real,
    constraint pk_pno PRIMARY KEY(pno)
);

INSERT INTO pizza VALUES(1,'Norvégienne',4.0),(2,'Chèvre Miel', 4.0),(3,'Margherita', 4.0),(4,'Orientale', 4.0);

CREATE table commande(
    cno serial Unique,
    userName varchar(255),
    date Date,
    prix real,
    constraint pk_cno PRIMARY KEY(cno)
);

INSERT INTO commande(userName,date,prix) VALUES('Xavier', '2024-01-15',25.0),('Manon', '2024-02-15', 22.0),('Manel', '2024-02-20', 20.0);

Create table contient(
    pno int,
    ino int,
    constraint pk_key_pno_cno PRIMARY KEY(pno,ino),
    constraint fk_pnoContient FOREIGN KEY(pno)
        REFERENCES pizza(pno)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    constraint fk_inoContient FOREIGN KEY(ino)
        REFERENCES ingredient(ino)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

Create table compose(
    cno int,
    pno int,
    constraint pk_key_cno_pno PRIMARY KEY(cno,pno),
    constraint fk_cnoCompose FOREIGN KEY(cno)
        REFERENCES commande(cno)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    constraint fk_pnoCompose FOREIGN KEY(pno)
        REFERENCES pizza(pno)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);


Create table usersRest(
    login varchar(255),
    password varchar(255),
    constraint pk_loginUsers PRIMARY KEY(login)
);
INSERT INTO usersRest VALUES ('Manel','passManel'),('Xavier','passXavier');
INSERT INTO contient VALUES(1,1),(1,6),(1,7),(1,8);
