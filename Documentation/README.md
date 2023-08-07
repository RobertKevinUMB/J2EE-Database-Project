# Musely take home assessment 
## _Role : Software Engineer, Server Applications (J2EE)_
--------
--------
**_Introduction_**

The assessment involved enhancing the existing application by implementing additional features and improving various aspects of the project. The goal was to demonstrate proficiency in software engineering concepts and showcase programming strengths.

**_Features Added_**

1. Durable Persistence Layer:

- Integrated Mysql from AWS RDS as the preferred database and for storing task-related data
- Utilized Hibernate as the persistence layer framework to handle database operations effectively

2. Multiple User Creation API:

- Implemented an API endpoint that allows the creation of multiple users in a single call
- Ensured that if an error occurs during the creation of one user, the creation of all users respect to that request is prevented by transaction database iteration methods

3. Bank Balance for Users:

- Extended the User model to include a "bank balance" field to track financial information
- Created APIs to credit and debit a user's balance, enforcing validation rules and handling concurrent requests appropriately

4. Robust Error Handling:

- Implemented input validation mechanisms to ensure data integrity
- Utilized appropriate HTTP responses in the REST layer for clear error communication.
- Added exception handling and error reporting mechanisms 

**_Additional Feature - FrontEnd Using ReactJs Framework_**

- Implemented a functional Web Application using ReactJs Framework

**_Difficulties Encountered_**

- Setup had issue with Eclipse Maven spport

- Faced issues Accesing the backend Endpoint from ReactJS Frontend

**_Areas for Improvement_**

Given more time, the following improvements would be considered:

* Enhancing the front-end interface to provide a more intuitive and visually appealing user experience
* Implementing more comprehensive test cases to achieve higher code coverage and ensure maximum reliability
* Implementing a comprehensive logging mechanism to capture detailed application activity for debugging and monitoring purposes
* Incorporating a caching layer to optimize database access and improve overall system performance

* Implement swagger for Restapi Documentation.

**_Conclusion_**

Overall, working on this assessment was a valuable experience. The added features, including a durable persistence layer, multiple user creation API, bank balance functionality, robust error handling, showcased proficiency in various aspects of software engineering. Despite encountering challenges, the project allowed for the demonstration of strong problem-solving skills. If given more time, further enhancements could be made to improve user experience, reliability, performance, and monitoring capabilities.

## NOTE: Demo Videos are attached for reference 



