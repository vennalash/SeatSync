# 🎟️ SeatSync – Smart Multi-Service Booking System

SeatSync is a web-based multi-service booking platform developed using **Java Spring Boot**. It allows users to explore and book movies, events, hotels, and sports facilities through a single application.

## 🚀 Features

### 👤 User Management
- User registration and login
- User profile management
- Booking history
- View and cancel bookings

### 🎬 Movie Booking
- Browse available movies
- View movie details and show information
- Select seats
- Book movie tickets

### 🎉 Event Booking
- Explore upcoming events
- View event details and ticket prices
- Check ticket availability
- Book and cancel event tickets
- Automatic ticket availability updates

### 🏨 Hotel Booking
- Browse available hotels
- View hotel information
- Book hotel services
- Apply discount coupons

### ⚽ Sports Facility Booking
- Explore sports facilities
- Book sports facilities
- View booking tickets
- QR code generation
- Booking cancellation

### 🛠️ Admin Dashboard
- Manage users
- Manage movies and shows
- Manage events and ticket availability
- Manage hotels
- Manage sports facilities
- View and manage bookings

## 💻 Technologies Used

| Category | Technologies |
|---|---|
| Backend | Java, Spring Boot |
| Database | MySQL |
| ORM | Hibernate, Spring Data JPA |
| Frontend | HTML, CSS, JavaScript |
| Build Tool | Maven |
| Server | Embedded Tomcat |
| Development Tools | Eclipse / Spring Tool Suite |

## ⚙️ Installation and Setup

### 1. Clone the Repository

```bash
git clone https://github.com/vennalash/SeatSync.git
```

### 2. Open the Project

Import the project into Eclipse or Spring Tool Suite as an existing Maven project.

### 3. Create the MySQL Database

```sql
CREATE DATABASE seatsync_db;
```

### 4. Configure Database Credentials

Set the following environment variables:

```text
DB_USERNAME=your_mysql_username
DB_PASSWORD=your_mysql_password
```

The application uses these variables in `application.properties`.

### 5. Run the Application

Run `SeatsyncApplication.java` as a Spring Boot application.

### 6. Open in Browser

```text
http://localhost:8082
```

## 📁 Project Structure

```text
SeatSync/
│
├── src/
│   ├── main/
│   │   ├── java/SeatSync/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── entity/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   └── SeatsyncApplication.java
│   │   │
│   │   └── resources/
│   │       ├── static/
│   │       └── application.properties
│   │
│   └── test/
│
├── pom.xml
└── README.md
```

## 🎯 Project Objective

The main objective of SeatSync is to provide a centralized booking platform where users can conveniently access multiple booking services. It simplifies booking management and provides administrators with an organized dashboard to manage platform activities.

## 👩‍💻 Developer

**Vennala S H**

GitHub: https://github.com/vennalash

---

⭐ If you find this project interesting, consider giving it a star!
