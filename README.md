Entertainment Management System - Java Swing

Requirements:
- Java 8+ (JDK)
- MySQL server with the provided 'entertainmentmanagement' database imported
- MySQL Connector/J (JAR) added to project classpath

Setup:
1. Import the SQL dump into MySQL:
   - Use phpMyAdmin or mysql CLI to import the SQL you provided earlier.

2. Update database credentials:
   - Edit src/database/DBConnection.java and set USER and PASSWORD.

3. Add MySQL Connector/J JAR to your project's library (e.g., mysql-connector-java-8.0.xx.jar).

4. Build & Run:
   - In your IDE run MainLauncher, or run:
     javac -cp ".;lib/*" -d out $(find src -name "*.java")
     java -cp ".;out;lib/*" MainLauncher

Notes:
- Passwords are stored as SHA-256 hashes via utils.SecurityUtil.sha256.
- This is a simple, extendable foundation; consider adding:
  - Input validation
  - Role-based views (admin vs normal user)
  - Better password hashing (BCrypt)
  - Venue, Sponsor, Review GUIs and DAOs (skeletons are present)
