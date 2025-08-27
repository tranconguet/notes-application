# Notes App 📒

A simple yet modern **Notes application** written in **Kotlin**, built with **minimal dependencies**.  
The app demonstrates **Clean Architecture**, **MVI pattern**, **Jetpack Compose UI**, and persistence with **Room + Paging 3**.

---

## ✨ Features
- 📄 **List Notes** with pagination using [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3).
- ➕ **Create a new note**.
- 📝 **Update existing notes**.
- ❌ **Delete notes**.
- 💾 Notes are stored locally in a **Room database**.

---

## 📱 Screenshots

<img width="270" height="600" alt="Image" src="https://github.com/user-attachments/assets/2cc4f6cd-e7e2-47d4-8692-eac426805eed" />

<img width="270" height="600" alt="Image" src="https://github.com/user-attachments/assets/2c63f123-99fa-410a-89f3-a82aff681f1f" />

<img width="270" height="600" alt="Image" src="https://github.com/user-attachments/assets/5ed63a62-3cb6-468b-b87c-b7ee70c18a8a" />

**Video record:** https://github.com/user-attachments/assets/c004bade-90d7-4990-b4f0-5cbe763eddc7
---

## Installation note

- If you're facing this error while installing dependencies: **_The project is using an incompatible version (AGP 8.12.1) of the Android Gradle plugin. Latest supported version is AGP 8.7.3_**
-> Please update your Android Studio to **latest version**!

---

## 🛠 Tech Stack
- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Architecture**:
    - [Clean Architecture](https://developer.android.com/topic/architecture) (Domain, Data, Presentation layers)
    - [MVI pattern](https://proandroiddev.com/a-robust-mvi-with-jetpack-compose-e08882d2c4ff) for predictable state management
- **Persistence**: [Room](https://developer.android.com/training/data-storage/room)
- **Pagination**: [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3)
- **Coroutines & Flow**: For async + reactive streams
- **Testing**: JUnit, kotlinx-coroutines-test, MockK (no extra DI frameworks)

---

## 📐 Project Structure

- **Data layer**: Handles local DB with Room, and provides `Flow<PagingData<Note>>`.
- **Domain layer**: Defines use cases (`CreateNote`, `DeleteNote`, `UpdateNote`, `GetNotesPagedFlow`, etc.).
- **Presentation layer**: Implements ViewModels (MVI) and Compose UI screens.

<img src="https://developer.android.com/static/topic/libraries/architecture/images/mad-arch-overview-data.png" alt="Demo Note" width="300"/>

---

## ✅ Unit Testing
The project includes **unit tests** for:
- Use cases, Repositories (proving Clean Architecture separation).
- ViewModels (proving MVI state & effect correctness).

Tests are written with:
- **JUnit** for assertions
- **MockK** for mocking dependencies
- **kotlinx-coroutines-test** for coroutine + Flow testing

---

## 🔑 Key Decisions
- **No DI framework**: To minimize dependencies, manual dependency injection is used.
- **MVI over MVVM**: Ensures predictable, testable UI state management.
- **Paging 3 + Room**: Efficiently loads notes with built-in paging support.
