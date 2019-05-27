#!/bin/sh

echo "Informe a senha root do MySQL"

mysql -u root -p < dbscript.sql

cd Backend/target/

java -jar desafio-0.0.1-SNAPSHOT.jar

