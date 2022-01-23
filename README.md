#Jobportal Demo Application in Java

Register client applications, post and search positions. Using in memory database and swagger-ui. 

##Curl test commands

curl -X GET http://localhost:8085/clients

curl -X GET http://localhost:8085/positions/all

curl -X POST http://localhost:8085/clients \
	-H 'Content-Type: application/x-www-form-urlencoded' \
	-d 'name=client4&email=client4%40company4.com'

curl -X POST http://localhost:8085/positions \
	-H 'Content-Type: application/x-www-form-urlencoded' \
	-H 'api-key=1babfc4d-5de7-4dbc-a882-3b24c773016c' \
	-d 'name=Employee%20Manager&location=Budapest'
