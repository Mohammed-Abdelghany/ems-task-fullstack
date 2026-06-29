# Employee & Project Management System

Full-stack enterprise application for managing employees, departments, and projects. Built with **Spring Boot 4.0.0** backend and **Angular 19** frontend using standalone components.

---

## 🛠️ Tech Stack

| Layer | Technology | Version |
|-------|-----------|---------|
| **Backend** | Spring Boot | 4.0.0 |
| **Language** | Java | 17 |
| **ORM** | Spring Data JPA + Hibernate | - |
| **Mapping** | MapStruct | 1.5.5.Final |
| **Database** | PostgreSQL | Latest |
| **Frontend** | Angular | 19 |
| **Node.js** | Node | v25.2.1 |
| **UI Framework** | Bootstrap 5 | - |
| **API Documentation** | Swagger/OpenAPI | 3.0.2 |

---

## 📊 Database Schema

```
┌──────────────────┐          ┌────────────────┐          ┌─────────────────┐
│    Employee      │◄─────────│   Department   │─────────►│    Project      │
│  (Many)          │ (1:Many) │   (Pivot)      │(Many:M)  │                 │
└──────────────────┘          └────────────────┘          └─────────────────┘
        │                              │
        │                              └──► Many-to-Many Join Table
        │
        └──► employee_project (Join Table)
             ├── employee_id
             ├── project_id
             └── project_role (ENUM: DEVELOPER, MANAGER, ANALYST)
```

**Relationships:**
- **Department ↔ Employee** - One-to-Many (A department has multiple employees)
- **Project ↔ Department** - Many-to-Many (Projects assigned to multiple departments)
- **Employee ↔ Project** - Many-to-Many with Extra Column (Tracks ProjectRole)

---

## 🚀 Quick Start

### Backend Setup

**1. Create Database**
```bash
psql -U postgres
CREATE DATABASE employee_management_db;
\q
```

**2. Configure** (`backend/src/main/resources/application.properties`)
```properties
spring.application.name=ems
server.port=8080

spring.datasource.url=jdbc:postgresql://localhost:5432/ems_db
spring.datasource.username=postgress
spring.datasource.password=1234
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

**3. Run Backend**
```bash
cd backend
./mvnw spring-boot:run
```

✅ **Server:** http://localhost:8080  
📚 **Swagger UI:** http://localhost:8080/swagger-ui/index.html

---

### Frontend Setup

**1. Install Dependencies**
```bash
cd frontend
npm install
```

**2. Run Development Server**
```bash
ng serve --port 4200
```

✅ **App:** http://localhost:4200

---

## 📂 Project Structure

### Backend (`Spring Boot`)
```
com.example.ems/
├── config/               → OpenAPI & Web configuration
├── controller/           → REST API endpoints
├── dto/                  → Data Transfer Objects (DTOs)
├── entity/               → JPA Entities (Employee, Department, Project)
├── exception/            → Custom exception handling
├── helper/               → Utility classes & helpers
├── mapper/               → MapStruct mappers (Entity ↔ DTO)
├── repo/                 → Spring Data JPA repositories
├── service/              → Business logic & transactions
└── EMSApplication.java   → Main Spring Boot application
```

### Frontend (`Angular 19`)
```
src/app/
├── components/
│   ├── departments/      → Department CRUD components
│   ├── employees/        → Employee CRUD components
│   └── projects/         → Project CRUD components
├── model/                → TypeScript interfaces
├── services/             → API service calls
├── app.routes.ts         → Route definitions
├── app.config.ts         → App configuration
├── app.component.ts      → Root component
└── main.ts               → Bootstrap standalone app
```

---

## 📡 API Endpoints

### Employees
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/employees?page=0&size=10` | Paginated list of employees |
| GET | `/api/employees/{id}` | Get employee by ID |
| POST | `/api/employees` | Create new employee |
| PUT | `/api/employees/{id}` | Update employee |
| DELETE | `/api/employees/{id}` | Delete employee |

### Departments
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/departments` | List all departments |
| GET | `/api/departments/{id}` | Get department by ID |
| POST | `/api/departments` | Create new department |
| PUT | `/api/departments/{id}` | Update department |
| DELETE | `/api/departments/{id}` | Delete department (if no employees) |

### Projects
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/projects?page=0&size=10` | Paginated list of projects |
| GET | `/api/projects/{id}` | Get project by ID |
| POST | `/api/projects` | Create new project |
| PUT | `/api/projects/{id}` | Update project |
| DELETE | `/api/projects/{id}` | Delete project |

---

## ✨ Key Features

✅ **Employee Management** - Full CRUD with validation  
✅ **Department Management** - Prevents deletion if employees assigned  
✅ **Project Assignments** - Assign projects to multiple departments  
✅ **Cascade Operations** - Safe deletion with automatic relationship cleanup  
✅ **Pagination & Sorting** - Efficient data retrieval  
✅ **Responsive UI** - Bootstrap 5 responsive layouts  
✅ **Real-time Updates** - ChangeDetectorRef for instant state changes  
✅ **Type-Safe Mapping** - MapStruct for clean DTO transformations  
✅ **Zero AI Business Logic** - 100% manual implementation  
✅ **Exception Handling** - Proper error messages & HTTP status codes  

---

## 🔐 Business Rules & Constraints

| Rule | Status |
|------|--------|
| Cannot delete department with active employees | ✅ Enforced |
| Deleting employee cascades to project assignments | ✅ Automatic |
| Project requires at least one department | ✅ Validated |
| Employee must belong to exactly one department | ✅ Mandatory |
| Duplicate employee-project-role assignments prevented | ✅ Unique constraint |
| ProjectRole only: DEVELOPER, MANAGER, ANALYST | ✅ Enum enforced |

---

## 🛠️ Development

### MapStruct Issue (if encountered)

If you see `ProjectMapperImpl` not found error:

```bash
# Clean and rebuild
cd backend
mvn clean
rm -rf ~/.m2/repository/org/mapstruct
mvn compile
mvn clean install -DskipTests
```

Ensure `pom.xml` has:
```xml
<annotationProcessorPaths>
    <path>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct-processor</artifactId>
        <version>1.5.5.Final</version>
    </path>
</annotationProcessorPaths>
```

---

## 📋 Prerequisites

- **Java 17+**
- **PostgreSQL 12+**
- **Node.js v25.2.1**
- **Maven 3.8+**
- **Angular CLI 19+**

```bash
# Check versions
java -version
node -v
npm -v
ng version
```

---

## 🚦 Testing the Application

1. **Create a Department**
   - POST `/api/departments` with name
   
2. **Create an Employee**
   - POST `/api/employees` with name, email, department_id
   
3. **Create a Project**
   - POST `/api/projects` with name, department_ids 
   
4. **Assign Employee to Project**
   - Via project creation or update
   
5. **Test Constraints**
   - Try deleting a department with employees (should fail)
   - Delete an employee and check project assignments (should cascade)

---

## 📚 Documentation

- **Swagger UI** - Full interactive API docs at http://localhost:8080/swagger-ui/index.html
- **Entity Relationships** - Defined via JPA annotations (@OneToMany, @ManyToMany)
- **DTOs** - Clean separation of concerns between layers
- **Error Handling** - Comprehensive exception handling with meaningful messages

---

## 🎯 Deployment

### Backend (Production)
```bash
cd backend
mvn clean package
java -jar target/employee-management-system.jar
```

### Frontend (Production)
```bash
cd frontend
ng build --configuration production
# Deploy dist/frontend to your web server
```

---

## 📝 Notes

- ✅ **Pure JPA/Hibernate** - No native SQL queries
- ✅ **Type-Safe Mappings** - MapStruct for automatic DTO conversion
- ✅ **Standalone Angular** - Modern Angular architecture without NgModules
- ✅ **RESTful APIs** - Proper HTTP verbs and status codes
- ✅ **Transaction Management** - @Transactional for complex operations
- ✅ **Pagination Ready** - Spring Data pagination for large datasets

---

## 📞 Support

For issues or improvements, check the following:
1. Database connection (PostgreSQL running?)
2. Maven build (clean install successful?)
3. Port conflicts (8080 and 4200 available?)
4. Node modules updated (npm install done?)

---

**Built by:** Muhammed Abdelghany
**Last Updated:** June 27, 2026"# ems" 
