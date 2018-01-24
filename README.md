## CashRegister

**Cash Register System** (Topic 4).
   
The **Cashier** can:
- open the receipt;
- add the selected products by code from the database (parsley = 234, bread = 222) or by the name of the product;
- specify the number of products or weight; 
- cancel receipt.
 
The **Senior Cashier** can:
- create the return receipt;
- return one item in the receipt and return the money to the customer;
- make X-Tape and Z-Tape reports. 

The **Merchandiser** can:
- create products;
- specify quantity in the warehouse.

## Installation
Clone repository:
```
> git clone https://github.com/marisabell/CashRegister.git CashRegister
```
Navigate to CashRegister dir:
```
> cd CashRegister
```
Run database migration:
```
> mvn -Dflyway.url=jdbc:mysql://localhost:3306 -Dflyway.user=root -Dflyway.password=root -Dflyway.schemas=cashregister  flyway:migrate
```
Create war:
```
> mvn clean package -Ddir=path-to-tomcat-webapps

ex. on Windows

> mvn clean package -Ddir="C:\Program Files\Apache Software Foundation\Tomcat 8.0\webapps"
```

## Launching 
Run Tomcat:
```
> path-to-tomcat-bin/catalina run

ex. on Windows

> "C:\Program Files\Apache Software Foundation\Tomcat 8.0\bin\catalina" run
```

Open [http://localhost:8080](http://localhost:8080)

## Testing
DAO tests are disabled by default. Run test database migration first in order to run them:
```
> mvn -Dflyway.url=jdbc:mysql://localhost:3306 -Dflyway.user=root -Dflyway.password=root -Dflyway.schemas=cashregister_test flyway:migrate
```
Run tests:
```
> mvn test -Dtest=**/*DAOTest.java
```

