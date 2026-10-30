# 🎟️ SeatSync – Smart Multi-Service Booking System

**SeatSync** is a web-based multi-service booking platform developed using Java Spring Boot. It provides a centralized platform where users can discover, book and manage movies, events, hotels and sports facilities.

The application includes customer and administrator modules, booking management, ticket availability tracking, promotional offers, demo payment options and QR code ticket generation.

---

## 🚀 Key Features

### 👤 1. User Management
- User registration and login.
- Secure password handling.
- User profile management.
- Personalized user dashboard.
- View personal booking details.
- Booking history.
- Cancel existing bookings.

### 🎬 2. Movie Booking Module
- Browse available movies.
- View movie posters and descriptions.
- Explore movie details and show information.
- View available show timings.
- Select seats for movie bookings.
- View booking details and ticket information.
- Manage movie reservations.
- View movie bookings in booking history.

### 🎉 3. Event Booking Module
- Browse upcoming events.
- Explore event details, venue and location.
- View event dates and ticket prices.
- Display total and available ticket counts.
- Check ticket availability before booking.
- Book event tickets.
- Automatically decrease available tickets after booking.
- Restore ticket availability when a booking is cancelled.
- Display sold-out events when tickets are unavailable.

### 🏨 4. Hotel Booking Module
- Browse available hotels.
- View hotel information and details.
- Explore hotel locations and pricing.
- Book hotel services.
- Apply promotional discount coupons.
- Calculate discounted booking amounts.
- Manage hotel reservations.

#### 🎁 Hotel Offers and Coupons

| Coupon Code | Discount |
|---|---:|
| WELCOME10 | 10% OFF |
| HOTEL20 | 20% OFF |

Users can apply eligible coupon codes during hotel booking to receive discounts.

### ⚽ 5. Sports Facility Booking Module
- Browse available sports facilities.
- Explore different sports categories.
- Book sports facilities.
- View booking details.
- Generate QR code tickets.
- Access sports booking information.
- Cancel sports bookings.
- Manage sports reservations through booking history.

### 💳 6. Payment Module
- Demo payment functionality.
- UPI payment option.
- Cash on Delivery (COD) option where supported.
- Display booking amount and payment details.
- Confirm bookings after successful payment processing.

*Note: The application uses demo payment processing and does not currently integrate a live payment gateway.*

### 🎫 7. Booking Management
- Centralized booking management.
- Unique booking references.
- View booking details.
- Track booking status.
- Booking history for customers.
- Cancel bookings.
- Update booking status.
- Maintain event ticket inventory during booking and cancellation.

### 🛠️ 8. Admin Dashboard

The administrator can manage the different services and monitor booking activities through a dedicated dashboard.

**Admin functionalities:**

**User Management**
- View registered users.
- Search and manage users.

**Movie Management**
- Add new movies.
- Update movie information.
- Add movie posters.
- Manage movie records and shows.

**Event Management**
- Add new events.
- Update event details.
- Manage event venues and dates.
- Set ticket prices.
- Configure total and available tickets.
- Activate or deactivate events.
- Search and manage events.

**Hotel Management**
- Add hotels.
- Update hotel information.
- Manage hotel records.

**Sports Management**
- Add sports facilities.
- Update facility information.
- Manage sports facility listings.

**Booking Management**
- View customer bookings.
- Monitor booking information.
- Manage booking records and statuses.

---

## 🧰 Technologies Used

| Category | Technologies |
|---|---|
| Programming Language | Java |
| Backend Framework | Spring Boot |
| ORM Framework | Hibernate |
| Persistence | Spring Data JPA |
| Database | MySQL |
| Frontend | HTML, CSS, JavaScript |
| Build Tool | Maven |
| Server | Embedded Apache Tomcat |
| Database Connectivity | JDBC |
| Development Environment | Eclipse / Spring Tool Suite |

---

## 🏗️ Project Architecture

SeatSync follows a layered architecture to organize application functionality.

- **Controller Layer:** Handles HTTP requests and API endpoints.
- **Service Layer:** Implements business logic and booking operations.
- **Repository Layer:** Handles database operations using Spring Data JPA.
- **Entity Layer:** Defines database entities and relationships.
- **Configuration Layer:** Manages application and security configuration.
- **Static Resources:** Contains HTML, CSS and JavaScript frontend pages.

---

## 📂 Project Structure

```text
SeatSync/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── SeatSync/
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── entity/
│   │   │       ├── repository/
│   │   │       ├── service/
│   │   │       └── SeatsyncApplication.java
│   │   │
│   │   └── resources/
│   │       ├── static/
│   │       └── application.properties
│   │
│   └── test/
│
├── .gitignore
├── pom.xml
└── README.md
```

---

## ⚙️ Installation and Setup

### 1. Clone the Repository

```bash
git clone https://github.com/vennalash/SeatSync.git
```

### 2. Open the Project

- Open Eclipse or Spring Tool Suite.
- Import the project as an Existing Maven Project.
- Wait for Maven dependencies to download.

### 3. Create MySQL Database

Open MySQL and execute:

```sql
CREATE DATABASE seatsync_db;
```

### 4. Configure Database Credentials

The application uses environment variables for database credentials.

Configure:

```text
DB_USERNAME=your_mysql_username
DB_PASSWORD=your_mysql_password
```

Check `application.properties` and configure the required database settings.

### 5. Run the Application

Run the main class:

`SeatsyncApplication.java`

Run it as a Spring Boot application.

### 6. Access the Application

Open your browser:

```text
http://localhost:8082
```

---

## 🎯 Project Objectives

- Develop a centralized multi-service booking platform.
- Simplify movie, event, hotel and sports facility reservations.
- Provide a convenient booking experience for customers.
- Implement automated event ticket inventory management.
- Provide promotional offers and discounts.
- Simplify administrative operations.
- Maintain organized booking records and history.

---

## 🔮 Future Enhancements

- Integration with a live payment gateway.
- Email and SMS booking notifications.
- Advanced analytics for administrators.
- Enhanced reporting and dashboard visualizations.
- Improved mobile-responsive user interface.
- More personalized offers and recommendations.

---

## 👩‍💻 Developer

**Vennala S H**

GitHub: [vennalash](https://github.com/vennalash)

Project Repository: [SeatSync](https://github.com/vennalash/SeatSync)

---

⭐ If you find this project interesting, consider giving the repository a star!

**Built with Java, Spring Boot and MySQL ❤️**
