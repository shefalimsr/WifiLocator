CREATE TABLE mytable(
     ID     INT  NOT NULL AUTO_INCREMENT
   ,building VARCHAR(7) NOT NULL
  ,day      VARCHAR(9) NOT NULL
  ,device   VARCHAR(20)
  ,floor    VARCHAR(6) NOT NULL
  ,level    INTEGER  NOT NULL
  ,mac      VARCHAR(17) NOT NULL
  ,name     VARCHAR(11) NOT NULL
  ,room     INTEGER  NOT NULL
  ,time     TIME NOT NULL
  ,PRIMARY KEY (ID)
);