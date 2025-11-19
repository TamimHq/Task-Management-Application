# Task-Management-Application
A clean, modern __Task Management App__ built with __Android + Kotlin Multiplatform (KMP)__ architecture.
This app supports __full offline usage__, persisting all tasks using __SQLite__, and offers sorting, filtering, searching, due-date highlighting, and CRUD operations.

# 🚀 Features
## Core Features
- Create, Read, Update, Delete (CRUD) tasks
- Each task includes:
  - Title (required)
  - Description (optional)
  - Priority (Low, Medium, High)
  - Status (To Do, In Progress, Done)
  - Due date (optional)
  - Created timestamp
- Works fully offline
- Clean, simple Jetpack Compose UI
- 
# 🎨 User Experience
- View all tasks in a list
- Search tasks by name
- Filter tasks by:
  - Priority
  - Status
- Sort tasks by:
  - Due date
  - Priority
  - Status
-Deadline Highlighting:
  - 🟥 Red → Overdue tasks
  - 🟨 Yellow → Due within 1 day
  - 🟩 Green → Safe

# 🧱 Tech Stack
## Kotlin Multiplatform (KMP)
- Shared model, repository, usecases, and viewmodel
## Android Module
- Jetpack Compose
- Navigation Compose
- Material 3 UI
- Kotlin Coroutines
- SQLite database
## Storage
- Local SQLite database
- Custom **TaskDatabaseHelper** + **SQLiteTaskRepository**

# 📂 Project Structure
TaskManagementApplication/
│
├── app/                     
│   ├── ui/                  
│   ├── navigation/          
│   ├── di/                  
│   └── data/                
│
├── shared/                  
│   ├── model/               
│   ├── repository/          
│   ├── usecase/            
│   └── viewmodel/           
│
└── README.md
# ▶️ How to Run
1. Clone the repo
   git clone https://github.com/TamimHq/Task-Management-Application.git
   cd Task-Management-Application
2. Open the project in Android Studio **Ladybug or newer**
3. Sync Gradle
4. Run the app on an emulator or device

