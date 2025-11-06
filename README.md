# Equipment Management System (Spring Boot)

## Prerequisites
- JDK 17+
- Maven 3.8+
- MySQL 8.x

## Database
1. Create database:
   ```sql
   CREATE DATABASE IF NOT EXISTS equipment CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
   ```
2. Update `src/main/resources/application.yml` datasource (username/password).
3. On first run, Spring will execute `schema.sql` to create tables.

## Run
```bash
mvn spring-boot:run
```
Visit OpenAPI UI: `http://localhost:8080/swagger-ui/index.html`.

## Notes
- Schema covers: departments, labs, users/roles, devices/assets, approvals, borrow/return, repairs/scrap, consumables/inventory, issues/requisitions, audit logs.
- Next steps: implement entities, repositories, and controllers per modules.

