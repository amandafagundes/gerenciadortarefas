#!/bin/sh

echo "Informe a senha root do MySQL"

mysql -u root -p < dbscript.sql

cd React

npm install popper.js@^1.14.7 --save
npm install jquery@1.9.1 --save
npm install bootstrap --save
npm install mdbreact --save
npm install reactstrap --save


cd ../Backend/target/

java -jar desafio-0.0.1-SNAPSHOT.jar

