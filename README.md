[![Build status](https://github.com/cklau1001/xrwvm-fullstack_developer_capstone/actions/workflows/main.yml/badge.svg)](https://github.com/cklau1001/xrwvm-fullstack_developer_capstone/actions/workflows/main.yml)

# Introduction
This is a sample application to demostrate a microservice architecture with 
the following 4 microservices.

# Technology stack
- SpringBoot 
  - Spring security for establishing fine-grained entitlement and JWT authentication with React
  - Spring cache for storing responses of remote microservices and AI result that can both improve responsiveness
    of the application and save costs (tokens)
  - Virtual threads are used to optimize the response handling of potential long running AI response and avoid
    the use of an async coding style.
    
- PostgreSQL database used by SpringBoot
- NodeJS / Express
- React as frontend
- MongoDB
- OpenAI chat completion endpoint via OpenRouter (default model: openai/gpt-o4-mini)

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
          OpenRouter chat completion endpoint 
          (Provide sentiment analysis on provided comment)
```
