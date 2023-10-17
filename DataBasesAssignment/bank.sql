CREATE TABLE ������� (
  ID INTEGER   NOT NULL ,
  ����� VARCHAR(15)    ,
  ������� VARCHAR(15)    ,
  ��������� VARCHAR(15)    ,
  �� VARCHAR(15)    ,
  ��� INTEGER    ,
  ���� VARCHAR(20)    ,
  ���� VARCHAR(20)    ,
  ������� INTEGER    ,
  �� INTEGER    ,
  �������� VARCHAR(250)      ,
PRIMARY KEY(ID));




CREATE TABLE ����������_������ (
  ID INTEGER   NOT NULL ,
  PIN INTEGER    ,
  ����������_����� DATE      ,
PRIMARY KEY(ID));




CREATE TABLE ��������� (
  ID INTEGER   NOT NULL ,
  ����� VARCHAR(15)    ,
  ������� VARCHAR(15)    ,
  ���_������������ VARCHAR(10)    ,
  ��������� VARCHAR(50)      ,
PRIMARY KEY(ID));




CREATE TABLE ����������� (
  ID INTEGER   NOT NULL ,
  ID������ INTEGER   NOT NULL ,
  ������������ BOOL    ,
  ����� BOOL    ,
  ������� BOOL    ,
  �������� INTEGER      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID������)
    REFERENCES �������(ID));


CREATE INDEX �����������_FKIndex1 ON ����������� (ID������);


CREATE INDEX IFK_������ ON ����������� (ID������);


CREATE TABLE eBANKING (
  ID INTEGER   NOT NULL ,
  ID������ INTEGER   NOT NULL ,
  Username VARCHAR(20)    ,
  passwrd VARCHAR(20)    ,
  email VARCHAR(20)      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID������)
    REFERENCES �������(ID));


CREATE INDEX eBANKING_FKIndex1 ON eBANKING (ID������);


CREATE INDEX IFK_�������� ON eBANKING (ID������);


CREATE TABLE ������_����������� (
  ID INTEGER   NOT NULL ,
  ID����������� INTEGER   NOT NULL ,
  ������� BOOL    ,
  �������� BOOL    ,
  ���� INTEGER    ,
  ����������_������� DATE      ,
PRIMARY KEY(ID)  ,
  FOREIGN KEY(ID�����������)
    REFERENCES �����������(ID));


CREATE INDEX ������_�����������_FKIndex1 ON ������_����������� (ID�����������);


CREATE INDEX IFK_����������� ON ������_����������� (ID�����������);


CREATE TABLE ��������� (
  ID����������� INTEGER   NOT NULL ,
  ID����������_������_ INTEGER   NOT NULL   ,
PRIMARY KEY(ID�����������, ID����������_������_)    ,
  FOREIGN KEY(ID�����������)
    REFERENCES �����������(ID),
  FOREIGN KEY(ID����������_������_)
    REFERENCES ����������_������(ID));


CREATE INDEX �����������_has_����������_������_FKIndex1 ON ��������� (ID�����������);
CREATE INDEX �����������_has_����������_������_FKIndex2 ON ��������� (ID����������_������_);


CREATE INDEX IFK_Rel_03 ON ��������� (ID�����������);
CREATE INDEX IFK_Rel_04 ON ��������� (ID����������_������_);


CREATE TABLE ���������� (
  ID��������� INTEGER   NOT NULL ,
  ID������ INTEGER   NOT NULL ,
  ��� TIME    ,
  ���������� DATE      ,
PRIMARY KEY(ID���������, ID������)    ,
  FOREIGN KEY(ID���������)
    REFERENCES ���������(ID),
  FOREIGN KEY(ID������)
    REFERENCES �������(ID));


CREATE INDEX ���������_has_�������_FKIndex1 ON ���������� (ID���������);
CREATE INDEX ���������_has_�������_FKIndex2 ON ���������� (ID������);


CREATE INDEX IFK_Rel_05 ON ���������� (ID���������);
CREATE INDEX IFK_Rel_06 ON ���������� (ID������);



