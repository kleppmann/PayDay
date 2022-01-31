# PayDay Bank Application

## About this project
Project is about stock sell/buy, transactions in stocks, show risky stocks for handle investment strategy, get notifications after stock transactions and stock changes.

## Used Technologies, frameworks and plugins

- Spring Boot
- Spring Security
- Spring Data
- Liquibase
- Netflix Zuul
- Netflix Eureka
- Netflix Ribbon
- Docker
- PostgreSQL
- RabbitMQ
- Redis
- Websocket
- Prometheus
- Grafana
- SendGrid

## Design Architecture
![alt text](https://github.com/kleppmann/PayDay/blob/master/Design%20Architecture.png)

## Microservices

Project is consists of following Gradle service
- api-gateway
- service-discovery
- auth-service
- stock-service
- stock-adapter
- notification-service
- transaction-service
- user-service
- mail-adapter
- websocket-service
- monitoring-prometheus-service
- monitoring-grafana-service

Each microservice has its own Dockerfile for creation of docker image.

## Run this project

1. Run 'sh build-all.sh'
2. Run 'docker-compose up --build'

## Endpoints

Login credentials:
{
	"username": "admin",
	"password": "admin"
}

| Endpoint                                          | Method            | Description                   |
| -------------                                     |:-------------:    | -----:                        |
| http://localhost:8000/auth                        | POST              | Get jwt token in header       |                  
| http://localhost:8000/stock                       | GET               | Get available stocks          |
| http://localhost:8000/stock/transaction           | POST              | POST stock transaction        |
| http://localhost:8000/stock/preferential          | GET               | Get preferential stocks       |
| http://localhost:8000/stock/{id}/volatility       | GET               | Get volatility of the stock   |
| http://localhost:8000/stock/users/{userId}/stocks | GET               | Get users' stocks             |

Example post transaction json:

{
	"userId": 1,
	"stockId": 1,
	"transactionType": "BUY",
	"maximumPrice": {
		"value": 100,
		"currency": "USD"
	},
	"urlHook": "example.com"
}

## See monitoring in grafana

First in your browser http://localhost:3000
Next log in with user and password 'admin/admin' then create a datasource, select prometheus. 
Datasource url: http://monitoring-prometheus-service:9090 (check docker container name)
After that you can import a dashboard 'jvm-micrometer.json' that I have included in Grafana folder.
