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