#!/bin/sh

cd websocket-service; gradle clean build -x test; cd ..
cd auth-service; gradle clean build -x test; cd ..
cd user-service; gradle clean build -x test; cd ..
cd stock-service; gradle clean build -x test; cd ..
cd stock-adapter; gradle clean build -x test; cd ..
cd transaction-service; gradle clean build -x test; cd ..
cd mail-adapter; gradle clean build -x test; cd ..
cd api-gateway; gradle clean build -x test; cd ..
cd notification-service; gradle clean build -x test; cd ..
cd service-discovery; gradle clean build -x test; cd ..