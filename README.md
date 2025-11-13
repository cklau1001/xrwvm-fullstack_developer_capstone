[![Build status](https://github.com/cklau1001/xrwvm-fullstack_developer_capstone/actions/workflows/main.yml/badge.svg)](https://github.com/cklau1001/xrwvm-fullstack_developer_capstone/actions/workflows/main.yml)

# Introduction
This is a sample application to demostrate a microservice architecture with 
the following 4 microservices.

# Technology stack
- SpringBoot 
  - Spring security for establishing fine-grained entitlement and JWT authentication with React
  - Spring cache for storing responses of remote microservices
- PostgreSQL database used by SpringBoot
- NodeJS / Express
- React as frontend
- MongoDB
- IBM Watsion Sentiment Analyzer

Note: Django is deprecated in this version. 

```

user ---> frontend microservice             - RESTful / JWT token  -->   Dealers microserrvice
            Springboot                                            NodeJS / Express
             (endpoint and proxy to backend)                  MongoDB            
            PostgreSQL DB                                          (store dealers info)
             (store user details and car models)                    
            React                           - RESTful / JWT token  -->   Car inventory microserrvice
             (frontend)                                        NodeJS / Express
                                                               MongoDB
                                                                (store car inventory)
             |
             |  RESTful 
             |
             V
          IBM Watsion Sentiment analyzer microservice 
          (Provide sentiment analysis on provided comment)
```
