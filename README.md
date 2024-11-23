[![Contributors](https://img.shields.io/badge/contributors-3-brightgreen.svg?style=for-the-badge)](#)
[![MIT License](https://img.shields.io/badge/license-MIT-blue.svg?style=for-the-badge)](#)

# LibZone - Library Mangement App
<div align="center">
  <img src="/src/main/resources/graphic/images/logo.png" alt="Logo" width="64" height="64">
</div>

---
<h2 style="font-size: 24px;">Contents</h2>

- <span style="font-size: 18px;">[Introduction](#introduction)</span>
- <span style="font-size: 18px;">[Features](#features)</span>
- <span style="font-size: 18px;">[Diagrams](#diagrams)</span>
- <span style="font-size: 18px;">[Built with](#built-with)</span>
- <span style="font-size: 18px;">[Installation](#installation)</span>
- <span style="font-size: 18px;">[License](#license)</span>
- <span style="font-size: 18px;">[Contact](#contact)</span>
---

## Introduction
LibZone is a library management application where librarians and administrators share the same user role, managing tasks such as book cataloging and reader (also call member). This project is the final assignment of Group 11 for the Object-Oriented Programming course, class 2425I_INT2204_19 at VNU-UET.  

Our team:

| Name              | Role        | Github link                   |
|-------------------|-------------|-------------------------------|
| Pham Ngoc Do      | Team Leader | https://github.com/phamngocdo |
| Nguyen Quoc Cuong | Developer   | https://github.com/NCuong314        |
| Nguyen Quang Duy  | Developer   | https://github.com/quangduy164        |

---
## Features
- Login and search for book information are available for both user groups.

- **For Admin (Librarian)**:
  - Edit book details and add new books.
  - Retrieve book details using ISBN through the Google Books API to assist in adding books.
  - View borrowing records and mark books as returned when readers return them.
  - Access member information and delete members for serious violations.

- **For Readers (Members)**:
  - Register an account and update personal information if needed.
  - Borrow and rate when viewing detailed book information.
  - View borrowing history through borrowing receipts.
  - Save and print borrowing receipts.
---
## Diagrams
- Use cases:
<div align="center">
  <img src="/src/main/java/app/Use%20cases%20Diagram.png" alt="UseCases">
</div>

- Database:
<div align="center">
  <img src="/src/main/java/app/dao/Database%20Diagram.png" alt="Database">
</div>

- Class:
<div align="center">
  <img src="/src/main/java/app/base/Class%20Diagram.png" alt="Class">
</div>

---
## Built with
- **Java 21**: For core app.
- **CSS**: For styling the UI.
- **JavaFX**: Framework for building desktop applications.
- **JFoenix**: Material Design UI components for JavaFX.
- **Maven**: Build tool for managing dependencies.
- **SQLite**: Database for data storage.
- **ZXing**: QR code and barcode library.
- **IntelliJ IDEA**: IDE for efficient development.

---
## Installation
- Step 1. **Download IDE and JDK development**:  
    - Download IntelliJ in this [link](https://www.jetbrains.com/idea/download/?section=windows).
    - Create new project, or you can go to step 3 then press Ctrl+Alt+Shift+S and install the lastest jdk.
- Step 2. **Set up UTF-8**:  
    - Because this app was made for Vietnamese people so you need to follow these steps.
    - Click Settings => Editor => File Encodings.
    - In the Global Encoding and Project Encoding box, choose UTF-8.
- Step 3. **Clone project by using git**:  
```bash
   git clone https://github.com/phamngocdo/DictionaryApp-OOPproject.git
```
- Step 4. **Run the application**:
  - Open the project in IDEA.
  - Navigate to package src\main\java\app\run\ and run the RunApp class to start the application.

---
## License
Distributed under the MIT License. See LICENSE.txt for more information.

---
## Contact
Email - [ngocdo992k4@gmail.com](mailto:ngocdo992k4@gmail.com)  
Github - [phamngocdo](https://github.com/phamngocdo) 
