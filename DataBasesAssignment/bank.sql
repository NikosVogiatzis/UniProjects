CREATE TABLE пекатгс (
  ID INTEGER   NOT NULL ,
  ╪МОЛА VARCHAR(15)    ,
  еПЧМУЛО VARCHAR(15)    ,
  пАТЯЧМУЛО VARCHAR(15)    ,
  ат VARCHAR(15)    ,
  ажл INTEGER    ,
  пЭКГ VARCHAR(20)    ,
  оДЭР VARCHAR(20)    ,
  аЯИХЛЭР INTEGER    ,
  тй INTEGER    ,
  тГКщЖЫМА VARCHAR(250)      ,
PRIMARY KEY(ID));




CREATE TABLE пистытийес_йаятес (
  ID INTEGER   NOT NULL ,
  PIN INTEGER    ,
  гЛЕЯОЛГМъА_КчНГР DATE      ,
PRIMARY KEY(ID));




CREATE TABLE упаккгкос (
  ID INTEGER   NOT NULL ,
  ╪МОЛА VARCHAR(15)    ,
  еПЧМУЛО VARCHAR(15)    ,
  тГК_ЕПИЙОИМЫМъАР VARCHAR(10)    ,
  дИЕЩХУМСГ VARCHAR(50)      ,
PRIMARY KEY(ID));




CREATE TABLE коцаяиаслос (
  ID INTEGER   NOT NULL ,
  IDпекатг INTEGER   NOT NULL ,
  тАЛИЕУТГЯъОУ BOOL    ,
  ╪ЬЕЫР BOOL    ,
  дАМЕъОУ BOOL    ,
  уПЭКОИПО INTEGER      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(IDпекатг)
    REFERENCES пекатгс(ID));


CREATE INDEX коцаяиаслос_FKIndex1 ON коцаяиаслос (IDпекатг);


CREATE INDEX IFK_амгйеи ON коцаяиаслос (IDпекатг);


CREATE TABLE eBANKING (
  ID INTEGER   NOT NULL ,
  IDпекатг INTEGER   NOT NULL ,
  Username VARCHAR(20)    ,
  passwrd VARCHAR(20)    ,
  email VARCHAR(20)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(IDпекатг)
    REFERENCES пекатгс(ID));


CREATE INDEX eBANKING_FKIndex1 ON eBANKING (IDпекатг);


CREATE INDEX IFK_диахетеи ON eBANKING (IDпекатг);


CREATE TABLE йимгсг_коцаяиаслоу (
  ID INTEGER   NOT NULL ,
  IDкоцаяиаслоу INTEGER   NOT NULL ,
  аМэКГЬГ BOOL    ,
  йАТэХЕСГ BOOL    ,
  пОСЭ INTEGER    ,
  гЛЕЯОЛГМъА_ЙъМГСГР DATE      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(IDкоцаяиаслоу)
    REFERENCES коцаяиаслос(ID));


CREATE INDEX йимгсг_коцаяиаслоу_FKIndex1 ON йимгсг_коцаяиаслоу (IDкоцаяиаслоу);


CREATE INDEX IFK_йатацяажгйе ON йимгсг_коцаяиаслоу (IDкоцаяиаслоу);


CREATE TABLE сумдеетаи (
  IDкоцаяиаслоу INTEGER   NOT NULL ,
  IDпистытийес_йаятес_ INTEGER   NOT NULL   ,
PRIMARY KEY(IDкоцаяиаслоу, IDпистытийес_йаятес_)    ,
  FOREIGN KEY(IDкоцаяиаслоу)
    REFERENCES коцаяиаслос(ID),
  FOREIGN KEY(IDпистытийес_йаятес_)
    REFERENCES пистытийес_йаятес(ID));


CREATE INDEX коцаяиаслос_has_пистытийес_йаятес_FKIndex1 ON сумдеетаи (IDкоцаяиаслоу);
CREATE INDEX коцаяиаслос_has_пистытийес_йаятес_FKIndex2 ON сумдеетаи (IDпистытийес_йаятес_);


CREATE INDEX IFK_Rel_03 ON сумдеетаи (IDкоцаяиаслоу);
CREATE INDEX IFK_Rel_04 ON сумдеетаи (IDпистытийес_йаятес_);


CREATE TABLE енупгяетеи (
  IDупаккгкоу INTEGER   NOT NULL ,
  IDпекатг INTEGER   NOT NULL ,
  ©ЯА TIME    ,
  гЛЕЯОЛГМъА DATE      ,
PRIMARY KEY(IDупаккгкоу, IDпекатг)    ,
  FOREIGN KEY(IDупаккгкоу)
    REFERENCES упаккгкос(ID),
  FOREIGN KEY(IDпекатг)
    REFERENCES пекатгс(ID));


CREATE INDEX упаккгкос_has_пекатгс_FKIndex1 ON енупгяетеи (IDупаккгкоу);
CREATE INDEX упаккгкос_has_пекатгс_FKIndex2 ON енупгяетеи (IDпекатг);


CREATE INDEX IFK_Rel_05 ON енупгяетеи (IDупаккгкоу);
CREATE INDEX IFK_Rel_06 ON енупгяетеи (IDпекатг);



