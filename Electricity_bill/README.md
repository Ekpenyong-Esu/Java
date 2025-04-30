# Electricity Billing System

A comprehensive Java desktop application that provides electricity billing solutions for both administrators and customers. This system enables electricity providers to efficiently manage customer data, meter information, and billing operations through an intuitive graphical interface.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [System Architecture](#system-architecture)
- [Installation and Setup](#installation-and-setup)
- [Project Structure](#project-structure)
- [Technologies Used](#technologies-used)
- [Database Schema](#database-schema)
- [Usage](#usage)
- [Screenshots](#screenshots)

## Overview

The Electricity Billing System is designed to streamline the management of customer accounts, meter readings, bill calculations, and payment tracking for electricity service providers. It features separate interfaces for administrators and customers with different sets of functionalities appropriate to their roles.

## Features

### Admin Features
- **Customer Management**: Register new customers and view existing customer details
- **Bill Calculation**: Calculate electricity bills based on consumption units and tariff plans
- **Payment Tracking**: View and manage payment deposits and transaction history
- **Meter Management**: Configure meter properties like location, type, phase code, etc.

### Customer Features
- **Account Management**: View and update personal information
- **Bill Viewing**: Check current and past electricity bills
- **Bill Payment**: Pay bills through integrated payment gateway
- **Consumption Reports**: Generate detailed bill and consumption reports

### Common Features
- **Secure Authentication**: Role-based login system with separate admin and customer views
- **User-Friendly Interface**: Intuitive UI with consistent navigation and design
- **Utility Tools**: Access to calculator, notepad, and web browser
- **Data Validation**: Input validation to ensure data integrity

## System Architecture

The application implements the Model-View-Controller (MVC) architectural pattern:

- **Model**: Java classes that represent data entities like Customer, Meter, Bill, etc.
- **View**: Java Swing-based UI components organized by user type (admin/customer)
- **Controller**: Classes that handle business logic and communication between views and data
- **Data Access Layer**: DAO classes that abstract database operations for each entity

## Installation and Setup

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- MySQL Database 5.7 or higher
- IDE (IntelliJ IDEA, Eclipse, or NetBeans)

### Database Configuration
1. Install MySQL and create a new database named `ebs`
2. Create a user with username `ebsuser` and password `password123` (or modify db.properties)
3. Use the following DDL statements to create the required tables:

```sql
-- Create customer table
CREATE TABLE customer (
    name VARCHAR(100),
    meter VARCHAR(20) PRIMARY KEY,
    address VARCHAR(200),
    city VARCHAR(100),
    state VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(20)
);

-- Create login table
CREATE TABLE login (
    meter_no VARCHAR(20),
    username VARCHAR(50),
    name VARCHAR(100),
    password VARCHAR(50),
    user VARCHAR(20)
);

-- Create meter_info table
CREATE TABLE meter_info (
    meter_number VARCHAR(20) PRIMARY KEY,
    meter_location VARCHAR(50),
    meter_type VARCHAR(50),
    phase_code VARCHAR(50),
    bill_type VARCHAR(50),
    days VARCHAR(10)
);

-- Create bill table
CREATE TABLE bill (
    meter VARCHAR(20),
    month VARCHAR(20),
    units INT,
    total_bill DOUBLE,
    status VARCHAR(20)
);

-- Create tax table
CREATE TABLE tax (
    cost_per_unit VARCHAR(20),
    meter_rent VARCHAR(20),
    service_charge VARCHAR(20),
    service_tax VARCHAR(20),
    climate_change_levy VARCHAR(20),
    fixed_tax VARCHAR(20)
);
```

4. Insert default tax rates into the tax table:
```sql
INSERT INTO tax VALUES ('9', '47', '22', '9', '5', '18');
```

### Setting Up the Project
1. Clone or download the project to your local machine
2. Open the project in your preferred IDE
3. Make sure the project has the following external libraries:
   - MySQL Connector J (jar file in the lib folder)
   - rs2xml.jar (for ResultSet to JTable conversion)
4. Configure database connection in `resources/config/db.properties` if needed
5. Build and run the project with `Main.java` as the entry point

## Project Structure

```
Electricity_bill/
├── lib/                        # External libraries
│   ├── mysql-connector-j-9.3.0.jar
│   ├── rs2xml.jar
│   └── sqlitejdbc-v056.jar
├── resources/                  # Resources folder
│   ├── config/                 # Configuration files
│   │   └── db.properties       # Database connection properties
│   └── images/                 # UI images and icons
├── src/                        # Source code
│   ├── Main.java               # Application entry point
│   └── Electricity/            # Main package
│       ├── controller/         # Controllers (MVC)
│       ├── dataAccessOutput/   # Data Access Objects
│       ├── model/              # Data models (MVC)
│       ├── util/               # Utility classes
│       └── view/               # View components (MVC)
│           ├── admin/          # Admin interface
│           ├── common/         # Shared components
│           ├── customer/       # Customer interface
│           └── reports/        # Report generation
└── README.md                   # Project documentation
```

## Technologies Used

- **Java**: Core programming language (JDK 8+)
- **Swing**: Java GUI framework for desktop application
- **MySQL**: Relational database management system
- **JDBC**: Java Database Connectivity API
- **MVC Pattern**: Architectural design pattern
- **DAO Pattern**: Data Access Object pattern

## Database Schema

The system uses the following tables:

1. **customer**: Stores customer personal information
   - name, meter (PK), address, city, state, email, phone
   
2. **login**: Manages user authentication
   - meter_no, username, name, password, user (user type)
   
3. **meter_info**: Contains meter configuration details
   - meter_number (PK), meter_location, meter_type, phase_code, bill_type, days
   
4. **bill**: Stores billing information
   - meter, month, units, total_bill, status
   
5. **tax**: Defines billing rates and constants
   - cost_per_unit, meter_rent, service_charge, service_tax, climate_change_levy, fixed_tax

## Usage

### Admin Operations

1. **Login**: Use admin credentials to access the admin dashboard
2. **Adding New Customer**: 
   - Navigate to Master > New Customer
   - Fill in customer details and create account
   - Configure meter information
3. **Generating Bills**:
   - Navigate to Master > Calculate Bill
   - Select customer meter and enter consumption units
4. **View Reports**:
   - Navigate to Master > Deposit Details to view payment history

### Customer Operations

1. **Login**: Use meter number and password to access customer dashboard
2. **View/Pay Bills**:
   - Navigate to User > Pay Bill to see and pay current bills
3. **View/Update Information**:
   - Navigate to Information > Update Information to modify personal details

## Screenshots

*(Note: Add screenshots of key screens here)*

---

This project was developed as an educational demonstration of Java desktop application development with database integration. It implements proper software architecture principles and design patterns.