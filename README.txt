================================================================================
KAAFI BANK MANAGEMENT SYSTEM
               Group-4 Bank Management
================================================================================

PROJECT TITLE:
KAAFI BANK Management System - A Java Desktop Application

GROUP MEMBERS:
1. Abdiwasa mahammed    - 1701996
2. Abdi osman           - 1701992
3. Farhan hassan        - 1702097
4. Abdukerim Redwan     - 1701997
5. Abdukerim Alemar     - DDU1702298

UNIVERSITY:
Dire Dawa University
Course: Object-Oriented Programming (CS201)
Instructor: Dr. Smith

================================================================================
HOW TO RUN THE PROJECT
================================================================================

1. REQUIREMENTS:
   - Java Development Kit (JDK) 8 or higher
   - Java Runtime Environment (JRE)

2. COMPILATION:
   Open command prompt in the project directory and run:
   
   javac *.java
   
   This will compile all Java source files.

3. EXECUTION:
   Run the main class:
   
   java BankManagementSystem

4. ALTERNATIVE (Using an IDE):
   - Import the project into Eclipse, IntelliJ IDEA, or NetBeans
   - Run BankManagementSystem.java as the main class

================================================================================
DEFAULT LOGIN CREDENTIALS
================================================================================

ADMIN LOGIN:
   Username: admin
   Password: admin123

CLIENT LOGIN (Sample Accounts):
   Account #: ACC001
   PIN: 1234
   
   Account #: ACC002
   PIN: 5678
   
   Account #: ACC003
   PIN: 9999

================================================================================
MAIN FEATURES
================================================================================

ADMIN SIDE:
   - Login with username and password
   - View all registered clients
   - Search for client by account number
   - Add new client account
   - Delete or deactivate client account
   - View total number of accounts
   - View transaction records
   - Logout or exit system

CLIENT SIDE:
   - Login with account number and PIN
   - View account details
   - Check account balance
   - Deposit money
   - Withdraw money
   - Transfer money to another account
   - View transaction history
   - Logout or exit system

================================================================================
OOP CONCEPTS DEMONSTRATED
================================================================================

1. ENCAPSULATION:
   - Private fields in Account, Transaction, User classes
   - Public getters and setters for controlled access
   - Example: Account balance accessed via getBalance()/setBalance()

2. INHERITANCE:
   - Admin and Client classes extend abstract User class
   - Method inheritance: login(), logout(), displayInfo()

3. POLYMORPHISM:
   - Method overriding: displayInfo() in Admin and Client classes
   - Each subclass provides its own implementation

4. ABSTRACTION:
   - Abstract User class defines common behavior
   - Forces subclasses to implement abstract methods
   - Hides implementation details from user

================================================================================
EXCEPTION HANDLING
================================================================================

Custom Exceptions:
   - InvalidAmountException: For invalid deposit/withdrawal amounts
   - InsufficientBalanceException: For insufficient funds
   - AccountNotFoundException: When account doesn't exist
   - InvalidLoginException: For authentication failures

================================================================================
FILE HANDLING
================================================================================

Data Storage Files:
   - clients.txt: Stores client account information
   - admins.txt: Stores admin login credentials
   - transactions.txt: Stores transaction history

================================================================================
NOTES
================================================================================

- Sample data files are created automatically on first run
- Passwords/PINs are hidden during input
- Exit confirmation prevents accidental closure
- All operations persist data to files immediately

================================================================================