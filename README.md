# TimesheetService
SpringBoot Microservice, to simulate the concurrent saving of timesheets into a simple h2 database.
It offers a REST-endpoint and a scheduler, which will fetch every 60 seconds new timesheet-data from an external source (Simulated with test-data).

# Database Access
1. Open this URL in your browser: http://localhost:8080/h2-console and enter these values:
2. Saved Settings: Generic H2 (Embedded)
3. Driver Class:org.h2.Driver
4. JDBC URL: jdbc:h2:mem:timesheetdb
5. User Name: sa
6. Password: LEAVE EMPTY
7. Click "Connect" - you should see the TIMESHEETS database on the top left

# AWS Lambda/API-Gateway
To mimic an external time tracking system, you can use the files inside the folder /aws.
This service is able to return you example timesheets for 2026, for the months January-April.
1. Copy the *.json files and the .py file inside the folder /aws and create a new AWS Lambda (Python 3.14) with these files
2. Test the correct functionality of the newly created Lambda with this Event JSON: <br> _{
   "queryStringParameters": {
   "month": "4",
   "year": "2026"
   }
   }_ <br>(you can only use values of 1,2,3,4 for the "month" parameter.)
3. Open AWS API Gateway and create a new REST-API, with a GET method, that will call this Lambda

