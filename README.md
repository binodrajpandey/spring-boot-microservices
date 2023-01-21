## Run required dependencies
```commandline
sh ./gradlew bootJar
docker-compose up -d --build
```

If you want to run locally, do following steps
1. Run required dependencies
```commandline
 docker-compose -f docker-compose-local.yml up -d
```
2. Run services in the following order
- discovery-server
- api-gateway
- product-service
- inventory-service
- order-service

## Monitoring

![](./static/images/grafana-monitor-diagram.drawio.svg)

- Spring-boot app will expose metrics via actuator endpoints.
- Prometheus polls for the metrics at a regular interval configured in prometheus.yml
- Prometheus stores the metrics which acts as datasource for the grafana
- Grafana polls the data from prometheus at a regular interval and display on the dashboard 

## Create a grafana dash-board
- Add data source
- Set prometheus url: http://prometheus:9090
- Create dashboard by importing json `grafana_dashboard.json`

## Check if api-gateway is working fine

### Login
POST: localhost:8080/api/auth/login
```json
{
    "username": "binod",
    "password": "binod"
}
```
For each request accept we need jwt token which we can get from the /api/auth/login api above.
Authorization type: Bearer token
### product-service
POST: localhost:8080/api/product

```json
{
    "name": "iPhone",
    "description": "iPhone14",
    "price": 120
}
```

GET: localhost:8080/api/product

### order-service with inventory-service

POST: localhost:8080/api/order
```json
{
  "orderLineItems":  [
    {
    "id": 1, 
    "skuCode": "iphone_13",
    "quantity": 10,
    "price": 120
    }
]
}
```

### Service with different framework
Install dependencies
```commandline
pip install -r requirement.txt
uvicorn main:app --reload

```
Check the response
GET: localhost:8080/api/journey

expected response:
```json
{
    "message": "Hello I am from journey service"
}
```
