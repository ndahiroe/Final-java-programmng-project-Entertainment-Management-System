# Entertainment Management System (EMS)

A comprehensive Java Swing desktop application for managing entertainment events, venues, sponsors, and user reviews.

---

## Overview

The Entertainment Management System is a robust, extensible foundation for event management operations. It provides a complete suite of features for managing users, events, venues, sponsors, and reviews through an intuitive graphical interface.

---

## System Features

### 1. User Management
- User registration and authentication
- Password encryption using SHA-256
- Role-based access control (Admin, User)
- User profile management
- Secure login system

### 2. Event Management
- Create, read, update, delete events
- Track event date, status, and remarks
- Link events to user accounts
- Event description and reference tracking
- Event status management

### 3. Venue Management
- Manage venue information (name, address, capacity)
- Track venue manager and contact details
- Support multiple venues
- Venue capacity tracking

### 4. Sponsor Management
- Add and manage sponsors
- Store sponsor attributes
- Track sponsor creation date
- Manage sponsor information

### 5. Review System
- Users can leave reviews for sponsors
- Manage review attributes
- Track review creation date
- Link reviews to sponsors and users

### 6. Venue-Sponsor Relations
- Link venues with sponsors
- Manage many-to-many relationships
- View all venue-sponsor connections
- Delete relationships as needed

---

## System Architecture

### Layers

#### 1. Presentation Layer (UI)
- `LoginUI.java` - User authentication interface
- `DashboardUI.java` - Main application interface with tabbed panels
- Swing components for professional GUI

#### 2. Data Access Layer (DAO)
- `UserDAO.java` - User database operations
- `EventDAO.java` - Event database operations
- `VenueDAO.java` - Venue database operations
- `SponsorDAO.java` - Sponsor database operations
- `ReviewDAO.java` - Review database operations
- `VenueSponsorDAO.java` - Venue-Sponsor relationship operations

#### 3. Model Layer
- `User.java` - User entity with credentials and profile
- `Event.java` - Event entity with details and status
- `Venue.java` - Venue entity with contact information
- `Sponsor.java` - Sponsor entity with attributes
- `Review.java` - Review entity with content
- `VenueSponsor.java` - Relationship entity

#### 4. Database Connection
- `DBConnection.java` - MySQL connection management
- JDBC integration for database communication

#### 5. Security Utilities
- `SecurityUtil.java` - SHA-256 password hashing
- Secure credential storage

---

## Database Design

### Tables Overview

| Table | Purpose | Key Fields |
|-------|---------|-----------|
| Users | Store user credentials and profiles | UserID, Username, Email, PasswordHash |
| Events | Store event information | EventID, ReferenceID, Date, Status |
| Venues | Store venue details | VenueID, Name, Address, Capacity |
| Sponsors | Store sponsor information | SponsorID, Attribute1-3, CreatedAt |
| Reviews | Store sponsor reviews | ReviewID, Attribute1-3, UserID, SponsorID |
| Venue_Sponsor | Link venues and sponsors | VenueID, SponsorID |

### Entity Relationship Diagram (ERD)
```
Users (1) ──→ (M) Events
Users (1) ──→ (M) Reviews
Sponsors (1) ──→ (M) Reviews
Venues (M) ←→ (M) Sponsors (through Venue_Sponsor)
```

---

## Requirements

### System Requirements
- **Java Version:** Java 8 or higher (JDK 8+)
- **Database:** MySQL Server 5.7+ or MariaDB 10.4+
- **IDE:** Eclipse, IntelliJ IDEA, NetBeans, or similar
- **Build Tool:** Apache Maven or Gradle (optional)

### Dependencies
- **MySQL Connector/J** - JDBC driver for MySQL connectivity
  - Version: 8.0.xx or compatible
  - File: `mysql-connector-java-8.0.xx.jar`

---

## Installation & Setup

### Step 1: Database Setup

1. **Create Database**
   ```sql
   CREATE DATABASE entertainmentmanagement;
   ```

2. **Import SQL Schema**
   - Open phpMyAdmin or MySQL command line
   - Import the provided `entertainmentmanagement.sql` file
   - Verify all tables are created successfully

3. **Verify Database**
   ```sql
   USE entertainmentmanagement;
   SHOW TABLES;
   ```

### Step 2: Project Configuration

1. **Add MySQL Connector JAR**
   - Download MySQL Connector/J (8.0.xx or compatible)
   - Add `mysql-connector-java-8.0.xx.jar` to your project's library folder
   - Add to classpath:
     - **Eclipse:** Project Properties → Java Build Path → Libraries → Add External JAR
     - **IntelliJ:** File → Project Structure → Libraries → Add JAR
     - **NetBeans:** Project Properties → Libraries → Add JAR

2. **Configure Database Connection**
   - Edit `src/database/DBConnection.java`
   - Update the following fields:
     ```java
     private static final String URL = "jdbc:mysql://localhost:3306/entertainmentmanagement";
     private static final String USER = "your_mysql_username";
     private static final String PASSWORD = "your_mysql_password";
     ```
   - Replace `your_mysql_username` and `your_mysql_password` with your MySQL credentials

### Step 3: Build & Run

#### Using IDE (Recommended)
1. Open project in your IDE
2. Right-click on `LoginUI.java` or `MainLauncher.java`
3. Select "Run As" → "Java Application"
4. Application will launch with login screen

#### Using Command Line
```bash
# Compile all Java files
javac -cp ".;lib/mysql-connector-java-8.0.xx.jar" -d out $(find src -name "*.java")

# Run the application
java -cp ".;out;lib/*" LoginUI
```

#### Using Maven (if configured)
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="LoginUI"
```

---

## Usage Guide

### Login Screen
1. Launch the application
2. Enter username and password
3. Click "Login" to access dashboard
4. Invalid credentials will show error message

### Default Test Accounts
| Username | Email | Role |
|----------|-------|------|
| Ndahiro | ndahiroemmanuel100@gmail.com | User |
| emmy | emmy@gmail.com | user |

**Note:** Check database for actual password hashes (SHA-256 encrypted)

### Dashboard Navigation

After successful login, access the tabbed dashboard with the following sections:

#### Events Tab
- **Refresh** - Load all events from database
- **Add Event** - Create new event with:
  - ReferenceID
  - Description
  - Date (yyyy-MM-dd HH:mm format)
  - Status
  - Remarks
  - UserID
- **Edit Selected** - Modify selected event
- **Delete Selected** - Remove event

#### Users Tab
- **Add User** - Register new user with:
  - Username (unique)
  - Email (unique)
  - Full Name
  - Role
  - Password
- **Edit Selected** - Update user profile
- **Delete Selected** - Remove user account

#### Venues Tab
- **Add Venue** - Create venue with:
  - Name
  - Address
  - Capacity
  - Manager
  - Contact
- **Edit Selected** - Modify venue details
- **Delete Selected** - Remove venue

#### Sponsors Tab
- **Add Sponsor** - Add sponsor with three attributes
- **Edit Selected** - Update sponsor information
- **Delete Selected** - Remove sponsor

#### Reviews Tab
- **Add Review** - Create review for sponsor
- **Edit Selected** - Modify review content
- **Delete Selected** - Remove review

#### Venue-Sponsor Tab
- **Add Relation** - Link venue with sponsor
- **Delete Selected** - Remove venue-sponsor link

---

## Technical Implementation

### Security Features
- **Password Hashing:** SHA-256 encryption via `SecurityUtil.sha256()`
- **Input Validation:** Database constraints and application-level validation
- **Unique Constraints:** Username and email uniqueness enforced
- **Role-Based Access:** User roles control feature access

### Database Connectivity
- **JDBC Driver:** MySQL Connector/J for connection management
- **Connection Pooling:** Efficient database resource management
- **Transaction Handling:** ACID compliance for data integrity

### Design Patterns
- **MVC Architecture:** Clear separation of concerns
- **DAO Pattern:** Encapsulated data access logic
- **Singleton Pattern:** Single database connection instance

---

## Project Structure

```
entertainment-management-system/
├── src/
│   ├── ui/
│   │   ├── LoginUI.java
│   │   └── DashboardUI.java
│   ├── dao/
│   │   ├── UserDAO.java
│   │   ├── EventDAO.java
│   │   ├── VenueDAO.java
│   │   ├── SponsorDAO.java
│   │   ├── ReviewDAO.java
│   │   └── VenueSponsorDAO.java
│   ├── model/
│   │   ├── User.java
│   │   ├── Event.java
│   │   ├── Venue.java
│   │   ├── Sponsor.java
│   │   ├── Review.java
│   │   └── VenueSponsor.java
│   ├── database/
│   │   └── DBConnection.java
│   └── utils/
│       └── SecurityUtil.java
├── lib/
│   └── mysql-connector-java-8.0.xx.jar
├── entertainmentmanagement.sql
└── README.md
```

---

## Development Notes

### Key Implementation Details

1. **Password Storage**
   - All passwords are hashed using SHA-256
   - Hash function: `SecurityUtil.sha256(password)`
   - Passwords are never stored in plain text

2. **Date Handling**
   - Event dates use format: `yyyy-MM-dd HH:mm`
   - Timestamps for creation tracked automatically
   - Java DateTime API integration

3. **Database Operations**
   - All DAO classes follow consistent patterns
   - Exception handling for database errors
   - Automatic connection management

---

## Future Enhancements

### High Priority
- [ ] Input validation framework
- [ ] Role-based UI views (Admin vs User dashboards)
- [ ] Enhanced password hashing (BCrypt/Argon2)
- [ ] Complete Venue, Sponsor, and Review GUIs
- [ ] Data export (PDF/Excel)

### Medium Priority
- [ ] Search and filter functionality
- [ ] Event scheduling with notifications
- [ ] Email notifications for events
- [ ] User activity logging
- [ ] Attendance tracking

### Low Priority
- [ ] Ticket sales integration
- [ ] Payment processing
- [ ] Analytics and reporting
- [ ] Mobile app integration
- [ ] Cloud database support

---

## Troubleshooting

### Database Connection Issues
**Problem:** "No suitable driver found"
- **Solution:** Ensure MySQL Connector JAR is in classpath
- Verify JAR version compatibility

**Problem:** "Access denied for user"
- **Solution:** Check database credentials in DBConnection.java
- Verify MySQL server is running
- Ensure user has appropriate permissions

### Login Issues
**Problem:** Cannot login with valid credentials
- **Solution:** Verify user exists in database
- Check password hash matches stored value
- Ensure database connection is working

### Data Not Displaying
**Problem:** Tables appear empty
- **Solution:** Click "Refresh" button in each tab
- Verify database contains sample data
- Check database connection status

### Compilation Errors
**Problem:** Cannot find MySQL classes
- **Solution:** Add MySQL Connector JAR to build path
- Verify correct JAR version for your JDK
- Rebuild project after adding JAR

---

## Performance Tips

1. **Database Optimization**
   - Index frequently queried columns
   - Use connection pooling for multiple operations
   - Batch operations when possible

2. **GUI Optimization**
   - Load data in background threads for large datasets
   - Implement pagination for large result sets
   - Cache frequently accessed data

3. **Memory Management**
   - Close database connections properly
   - Use try-with-resources for resource management
   - Monitor memory usage with large datasets

---

## Best Practices

### Code Quality
- Follow Java naming conventions
- Use meaningful variable and method names
- Add comments for complex logic
- Implement proper error handling

### Database
- Use parameterized queries to prevent SQL injection
- Maintain referential integrity with foreign keys
- Use appropriate data types for columns
- Create regular database backups

### Security
- Never hardcode credentials (use configuration files)
- Validate all user input
- Use HTTPS for any web connections
- Regularly update dependencies




---

**Last Updated:** December 18, 2025
