# Money Transfer API
A Java RESTful API for money transfers between users accounts

## How to use

```
git clone https://github.com/davidarce/money-transfer-api.git   
```
```
mvn clean package
```
```
java -jar money-transfer-api-service/target/money-transfer-api-service-0.0.1-SNAPSHOT.jar
```

# Endpoints

```
- GET     /api/money/transfer/accounts - get all accounts
- GET     /api/money/transfer/accounts/:number - get an account by number
- POST    /api/money/transfer/accounts - create an account
- GET     /api/money/transfer/transactions - get all transactions
- GET     /api/money/transfer/transactions/:id - get a transactions by id
- POST    /api/money/transfer/transactions - create transaction for transfering money between accounts
```

## Http Status Code Summary

```
200 OK - Everything worked as expected
201 OK - Resource created
400 Bad request - The request due to something that is perceived to be a client error 
404 Not Found - The requested resource does not exist
500 Internal Server error - The server has encountered a situation it doesn't know how to handle.
```

## Please read the full documentation

```
http://localhost:8080/api/money/transfer/documentation
```



