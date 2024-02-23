# Employee Management System

Welcome to the Employee Management System! This project is a Java SpringBoot application designed to manage employees and departments using REST-based APIs.

## Key Features

- **Create, Read, Update, and Delete Employees:** Easily manage employee records by adding, viewing, updating, and deleting employee information.
- **Create, Read, Update, and Delete Departments:** Efficiently handle department management tasks, including creation, viewing, updating, and deletion of departments.
- **Move Employees Between Departments:** Seamlessly transfer employees from one department to another as organizational needs evolve.
- **List Employee Names and IDs:** Quickly retrieve a list of employee names and corresponding IDs for reference.
- **Detailed Employee and Department Information:** Access detailed information about employees and departments, including name, date of birth, salary, department, address, role/title, joining date, yearly bonus percentage, and reporting manager for employees, and name, creation date, and department head for departments.
- **Implement Pagination for GET APIs:** Ensure efficient data retrieval by paginating responses for GET APIs, with each page containing a specified number of items.
- **Reporting API:** Provide a reporting API to display reporting chains, allowing users to retrieve hierarchical reporting structures within the organization.

## API Documentation

For detailed documentation on how to use each API, refer to the [API Documentation 📄](https://docs.google.com/document/d/15yCYTILNaAr8jLRpyc_wxRP9Hb6RwEjBSyIIm3OTCk4).

## SQL Tables

### Employees Table
- Stores information about employees including their unique identifier, name, date of birth, salary, department they belong to, address, role, joining date, yearly bonus percentage, and the identifier of their reporting manager.

### Departments Table
- Contains details about departments such as their unique identifier, name, creation date, and the identifier of the department head.

## SQL Documentation

For documentation on the SQL table creation scripts, please refer to [SQL Documentation 📄](https://docs.google.com/document/d/1o_0snSL9q-h47Qg6vVvvmjZteK7fT0XtJx9toDcOWxY/edit?usp=drive_link).

#### SQL Database Diagram

![SQL Database Diagram 💠](https://i.ibb.co/swtJWDv/sql-diagram.png)
