# Introduction
This is the folder of the Java / Springboot implementation of the RESTful endpoints that support frontend React components.
It includes the static HTML as thymeleaf templates and React scripts.

# Key features
- An JWT token is generated for the login function that is passed from SpringBoot to React as a cookie, so that the latter
can present for the protected endpoints.
- A JWT servlet filter is implemented for the token authentication.
- Spring cache is used to store the responses of remote microservices that improves the responsiveness of the system from
React frontend, and save token costs on OpenAI endpoints.
- PostgreSQL database is used to store the user details and car model inventory.
- Spring AI is used to trigger OpenAI endpoints.

