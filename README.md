# Money Transfer API
A Java RESTful API for money transfers between users accounts

### Technologies
- Java 11
- Javalin
- Jetty Container
- Fuel HTTP
- In Memory data base (for Demo app)
- slf4j logger
- JUnit4
- REST Assured for testing

## How to use

```
git clone https://github.com/davidarce/money-transfer-api.git   
```
```
mvn package
```
```
java -jar money-transfer-api-service/target/money-transfer-api-service-0.0.1-SNAPSHOT.jar
```

# Endpoints

```
- GET     /api/money/transfer/accounts?site=:site&language=:language - get all accounts
- GET     /api/money/transfer/accounts/:number?site=:site&language=:language - get an account by account number
- POST    /api/money/transfer/accounts?site=:site&language=:language - create an account
- GET     /api/money/transfer/transactions?site=:site&language=:language - get all transactions
- GET     /api/money/transfer/transactions/:id?site=:site&language=:language - get a transaction by transaction id
- POST    /api/money/transfer/transactions?site=:site&language=:language - create a transaction for transfering money between accounts
```

## Http Status Code Summary

```
200 OK - Everything worked as expected
201 OK - Resource created
400 Bad request - The request due to something that is perceived to be a client error 
404 Not Found - The requested resource does not exist
500 Internal Server error - The server has encountered a situation it doesn't know how to handle.
```

## Account Resources

## GET /api/money/transfer/accounts?site=:site&language=:language

##### Example

##### Request

```
GET /api/money/transfer/accounts?site=US&language=en
```

##### Response
```
[
    {
        "number": "f5c3e99a-876b-43e9-a8cd-11698abad05e",
        "owner": "ARCE",
        "createdAt": "2019-12-03T09:26:37.146938",
        "funds": "USD1,000.00"
    },
    {
        "number": "f3c6a7f9-0513-4796-a75b-d915202fa398",
        "owner": "REVOLUT",
        "createdAt": "2019-12-03T09:26:33.586963",
        "funds": "USD1,000.00"
    }
]
```

## POST /api/money/transfer/accounts?site=:site&language=:language

##### Example

###### Request

```
POST /api/money/transfer/accounts?site=US&language=en
```

Body

```
{
	"owner": "REVOLUT",
	"funds": {
		"currency": "USD",
		"value": "1000"
	}
}
```

###### Response

```
{
    "number": "f1f06a00-3adc-4b4c-b370-0934c670ffc8",
    "owner": "REVOLUT",
    "createdAt": "2019-12-03T09:27:57.491235",
    "funds": "USD1,000.00"
}
```

## Transaction Resources

## GET /api/money/transfer/transactions?site=:site&language=:language

##### Example

##### Request

```
GET /api/money/transfer/transactions?site=US&language=en
```

##### Response
```
[
    {
        "id": "1f795519-458f-4a44-bd38-da07ac8b4e9d",
        "origin": "f1f06a00-3adc-4b4c-b370-0934c670ffc8",
        "destination": "e377bd78-9037-44da-847d-c01094e4bc99",
        "amount": "USD100.00",
        "status": "CREATED",
        "createdAt": "2019-12-03T09:29:15.400804"
    }
]
```

## POST /api/money/transfer/transactions?site=:site&language=:language

##### Example

###### Request

```
POST /api/money/transfer/transactions?site=US&language=en
```

Body

```
{
	"origin": "f1f06a00-3adc-4b4c-b370-0934c670ffc8",
	"destination": "e377bd78-9037-44da-847d-c01094e4bc99",
	"amount": {
		"currency": "USD",
		"value": "100"
	}
}
```

###### Response

```

    "id": "1f795519-458f-4a44-bd38-da07ac8b4e9d",
    "origin": "f1f06a00-3adc-4b4c-b370-0934c670ffc8",
    "destination": "e377bd78-9037-44da-847d-c01094e4bc99",
    "amount": "USD100.00",
    "status": "CREATED",
    "createdAt": "2019-12-03T09:29:15.400804"
}
```

## Please read the full documentation

```
http://localhost:8080/api/money/transfer/documentation
```



