# CGPA Management System

A simple CGPA Management System implemented in Java with a MySQL backend. This application enables students to input their grades for various subjects, calculates their CGPA, and provides an admin interface to view student details.

## Table of Contents
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Setup Instructions](#setup-instructions)
- [Usage Instructions](#usage-instructions)
- [Database Structure](#database-structure)
- [License](#license)
- [Contributing](#contributing)
- [Acknowledgements](#acknowledgements)

## Features

- **Student Interface**: 
  - Students can enter multiple subjects and their corresponding grades.
  - CGPA is calculated based on the entered grades using a predefined grading system.

- **Admin Interface**: 
  - Admins can view student details, including their grades and calculated CGPA.
  - Easily search for students by name to retrieve their information.

- **Database Management**: 
  - The system allows for resetting the database, which can be useful for testing and development.

- **Grade System**: 
  - Grades are represented as letters based on a numerical score:
    - **O**: 90-100
    - **A+**: 80-89
    - **A**: 70-79
    - **B+**: 60-69
    - **B**: 50-59
    - **C**: 40-49
    - **RA**: 1-39 (re-attempt required)

## Technologies Used

- **Java**: Programming language used for the application development.
- **MySQL**: Database management system used to store student and grade information.
- **JDBC**: Java Database Connectivity for connecting Java applications to the MySQL database.
- **Swing**: GUI toolkit for building the graphical user interface.

## Prerequisites

Before you begin, ensure you have met the following requirements:

- **Java Development Kit (JDK)** installed on your machine (version 8 or higher).
- **MySQL Server** installed and running.
- A MySQL JDBC driver (e.g., `mysql-connector-java-X.X.X.jar`).
- An IDE like Eclipse, IntelliJ IDEA, or any text editor for Java development.

## Setup Instructions

1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/cgpa-management-system.git
   cd cgpa-management-system
