# Introduction
This is a sample application to demostrate a microservice architecture with 
the following three microservices.

# Techology stack
- Python Django
- NodeJS / Express
- React
- SQLite
- MongoDB


```

user ---> frontend microservice             --->   Dealers microserrvice
            django                                    NodeJS / Express
             (endpoint and proxy to backend)          MongoDB            
            SQL lite                                   (store dealers info)
             (store user details)                                    
            React
             (frontend)

             
             |
             |  RESTful 
             |
             V
          IBM Watsion Sentiment analyzer microservice 
          (Provide sentiment analysis on provided comment)
```

