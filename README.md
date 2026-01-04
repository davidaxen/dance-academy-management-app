<h1 align="center">💃 MyDance</h1>

<p align="center">
  <a href="https://www.android.com/"><img alt="Platform" src="https://img.shields.io/badge/platform-android%20%7C%20ios-brightgreen.svg"/></a>
  <a href="https://developer.android.com/about/versions/oreo"><img alt="API" src="https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat"/></a>
  <a href="https://github.com/JetBrains/kotlin/releases/tag/v2.1.0"><img alt="Kotlin" src="https://img.shields.io/badge/Kotlin-2.1.0-blueviolet"/></a>
  <a href="https://github.com/davidaxen/dance-academy-management-app/blob/main/LICENSE"><img alt="License" src="https://img.shields.io/github/license/davidaxen/dance-academy-management-app"/></a>
</p>

**MyDance** is a comprehensive dance academy management application built with Kotlin Multiplatform. It provides a complete solution for managing dance academies, with dedicated interfaces for students, teachers, and academy administrators. The app enables class scheduling, reservation management, and academy administration across Android and iOS platforms. The project follows a modular architecture, separating features, data, and core functionality into independent modules for better maintainability and scalability.

> **Note:** This app uses the [Firebase Kotlin SDK](https://github.com/GitLiveApp/firebase-kotlin-sdk) (GitLiveApp), not the official Firebase SDK, as it's the only Firebase SDK that supports Kotlin Multiplatform. This provides a Kotlin-first API that works seamlessly across Android and iOS.
> 
> **Note:** This app requires Firebase configuration. Make sure to set up your `google-services.json` (Android) and `GoogleService-Info.plist` (iOS) files before building.

## 📱 Features

### 🎓 For Students
- **Calendar View**: View your reserved classes in a monthly calendar format
- **Class Reservations**: Browse available classes by week and make reservations
- **Reservation Management**: Cancel existing reservations directly from the calendar or reservation screen
- **Class Details**: View detailed information about classes including schedule, teacher, and capacity

### 👨‍🏫 For Teachers
- **Class Management**: View all assigned classes with weekly and specific date options
- **Student Reservations**: Access detailed lists of students who have reserved classes
- **Reservation Tracking**: Filter and search student reservations by name and dance role
- **Class Details**: View comprehensive class information including schedule and enrolled students

### 🏛️ For Academy Administrators
- **Student Management**: 
  - View and manage all students in the academy
  - Send invitations to new students
  - View detailed student profiles
- **Teacher Management**:
  - View and manage all teachers in the academy
  - Send invitations to new teachers
  - View detailed teacher profiles
- **Class Management**:
  - Create new classes (weekly or specific date classes)
  - Edit existing classes
  - View class details and student reservations
  - Manage class schedules and capacity
- **User Profile**: Manage academy information, settings, and account details

### 🔐 Authentication & Registration
- **User Registration**: Complete registration process with personal information
- **Role Selection**: Choose between student, teacher, or academy administrator roles
- **Academy Registration**: Academy administrators can register their academy with logo upload
- **Subscription Management**: Academy subscription and payment management
- **Multi-Academy Support**: Users can be part of multiple academies and switch between them

## 🛠️ Tech Stack

- **UI Framework**: Compose Multiplatform (Android & iOS)
- **Architecture**: Clean Architecture with MVVM (Model-View-ViewModel)
- **Dependency Injection**: Koin
- **Navigation**: Navigation Compose
- **Backend**: Firebase (Authentication, Firestore, Storage)
- **Image Loading**: Coil
- **Networking**: Ktor Client
- **Serialization**: Kotlinx Serialization
- **Date/Time**: Kotlinx DateTime
- **Image Picker**: Peekaboo

## 📁 Project Structure

```
MyDanceKMP/
├── composeApp/              # Main application module
│   ├── src/
│   │   ├── androidMain/     # Android-specific code
│   │   ├── iosMain/         # iOS-specific code
│   │   └── commonMain/      # Shared Kotlin code
│   │       └── kotlin/com/daxen/mydancekmpsharedui/
│   │           ├── navigation/  # Navigation setup
│   │           └── main/         # Main screens
├── core/                    # Core modules
│   ├── firebase/            # Firebase configuration
│   └── ui/                  # UI components and theme
├── data/                    # Data layer modules
│   ├── auth/                # Authentication data
│   ├── classes/             # Classes data
│   ├── reservation/         # Reservations data
│   ├── students/            # Students data
│   ├── teachers/            # Teachers data
│   └── user/                # User data
└── features/                # Feature modules
    ├── academy/             # Academy management features
    │   ├── classes/         # Class management
    │   ├── students/        # Student management
    │   └── teachers/        # Teacher management
    ├── auth/                # Authentication features
    ├── student/             # Student features
    │   ├── calendar/        # Calendar view
    │   └── reservation/     # Reservation management
    ├── teacher/             # Teacher features
    │   └── classes/         # Class management
    └── user/                # User profile features
```

## 🚀 Getting Started

### Prerequisites

1. **Android Studio** (latest version recommended)
2. **Xcode** (for iOS development, macOS required)
3. **Kotlin Multiplatform** plugin installed
4. **Firebase Project** set up with:
   - Authentication enabled
   - Firestore database configured
   - Storage bucket configured
5. **Firebase Kotlin SDK**: The project uses [GitLiveApp's Firebase Kotlin SDK](https://github.com/GitLiveApp/firebase-kotlin-sdk) for multiplatform Firebase support

### Setup

1. Clone the repository:
```bash
git clone https://github.com/davidaxen/dance-academy-management-app.git
cd dance-academy-management-app
```

2. **Firebase Configuration**:
   - For Android: Place your `google-services.json` file in `composeApp/` directory
   - For iOS: Place your `GoogleService-Info.plist` file in `iosApp/iosApp/` directory

3. **Build the Project**:
   - Open the project in Android Studio
   - Sync Gradle files
   - For Android: Build and run on an emulator or physical device
   - For iOS: Open `iosApp/iosApp.xcodeproj` in Xcode and build

### Building for Android

1. Open the project in Android Studio
2. Sync Gradle files
3. Select an Android device/emulator
4. Click Run or press `Shift+F10`

### Building for iOS

1. Open `iosApp/iosApp.xcodeproj` in Xcode
2. Select your development team in project settings
3. Select an iOS simulator or device
4. Click Run or press `Cmd+R`

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📄 License

This project is licensed under the GPL-3.0 License - see the (<a href="https://github.com/davidaxen/dance-academy-management-app/blob/main/LICENSE">LICENSE</a>) file for details.

## 👨‍💻 Author

David Bracamonte Martins
