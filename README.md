# To-Do List App

## Overview
The **To-Do List App** is a simple task management application built using **Jetpack Compose** and **Room Database (SQLite)**. It allows users to add, update, delete, and view their tasks seamlessly.

## Features
- **Add Tasks**: Users can add tasks to their list.
- **Delete Tasks**: Remove completed or unnecessary tasks.
- **View All Tasks**: Display all tasks stored in the local database.
- **Local Persistence**: Uses Room Database to store tasks offline.

## Tech Stack
- **Frontend**: Jetpack Compose (Kotlin)
- **Local Database**: Room Database (SQLite)
- **Architecture**: MVVM (Model-View-ViewModel)
- **State Management**: ViewModel & LiveData
- **Dependency Injection**: Hilt (Optional)

## Installation
### Prerequisites
- Android Studio (Latest version)
- Kotlin 1.6+
- Gradle 7+

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/your-repo/todo-list-app.git
   ```
2. Open the project in Android Studio.
3. Sync the Gradle files.
4. Run the app on an emulator or a physical device.

## Project Structure
```
app/
├── data/          # Data layer (Repositories, Models, Room DB, DAO)
├── ui/            # UI layer (Composable functions, Screens, Navigation)
├── viewmodel/     # ViewModels (Business logic, State Management)
├── utils/         # Utility classes and helper functions
└── MainActivity.kt # Entry point of the app
```

## Contributing
Contributions are welcome! Feel free to fork the repository and submit pull requests.



## Contact
For any questions or feedback, feel free to reach out:
- **Author**: Muhammed Ebrahim
- **LinkedIn**: [Muhammed Ebrahim](https://www.linkedin.com/in/muhammed-ebrahim-397808229/)
- **Email**: ebrahimmuhammed479@gmail.com

