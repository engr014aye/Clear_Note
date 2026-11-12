# ClearNote 💎

**Liquid Glass Offline Notepad & Productivity Tool for Android**

ClearNote is a production-ready, 100% offline-first productivity notepad developed natively using **Kotlin** and **Jetpack Compose**. It features an ultra-premium **Liquid Glass** design system, Apple-inspired fluid animations, tactile haptic feedback, and custom spring-physics rubber-band bouncing overscroll.

---

## 🚀 Key Specifications

- **Application Name**: ClearNote
- **Package ID**: `com.umerini.clearnote`
- **Architecture**: Modern Android Architecture (MVVM, Clean Architecture, Repository Pattern, Flow)
- **Local Persistence**: Android Room Database (`ClearNoteDatabase`) with reactive `Flow`
- **Zero Online Tracking**: Absolutely zero internet permissions (`android.permission.INTERNET` is neither requested nor used)
- **UI Framework**: 100% Jetpack Compose with Material 3
- **Minimum SDK**: API 26 (Android 8.0 Oreo)
- **Target SDK**: API 35 (Android 15)
- **Language**: Kotlin 2.0.21 exclusively

---

## ✨ Liquid Glass UI Design System

- **Translucent Glass Containers**: Multi-layered frosted glass surfaces with subtle backdrop tinting.
- **Specular Refraction Borders**: Razor-sharp glass edge highlights fading smoothly with linear gradients.
- **Apple-Inspired Spring Physics**: Custom rubber-band bouncing overscroll modifier (`Modifier.bouncingOverscroll()`) across all scrollables.
- **Tactile Haptic Engine**: Tailored vibration feedback on every tap, toggle, pin, category selection, and delete interaction.
- **Dynamic Glass Themes**: 6 lustrous frosted glass palettes (Frost Cyan, Emerald Glow, Amethyst Quartz, Sunset Amber, Rose Glass, and Pure Crystal).

---

## 🛠 Project Structure

```
com.umerini.clearnote
├── ClearNoteApplication.kt       # Application class & repository singleton
├── MainActivity.kt               # Edge-to-edge Compose entry point
├── data
│   ├── local
│   │   ├── ClearNoteDatabase.kt  # Room database definition
│   │   ├── NoteDao.kt            # Room DAO with reactive Flow queries
│   │   └── entity
│   │       └── NoteEntity.kt     # Room entity with domain mappers
│   ├── model
│   │   ├── Note.kt               # Core domain note model
│   │   ├── NoteCategory.kt       # Note categories enum
│   │   ├── ChecklistItem.kt      # Checklist serialization/deserialization
│   │   └── GlassThemeColor.kt    # Frosted glass color palettes
│   └── repository
│       ├── NoteRepository.kt     # Repository interface
│       └── NoteRepositoryImpl.kt # Offline Room repository implementation
└── ui
    ├── components
    │   ├── LiquidGlassTopBar.kt  # Frosted glass app bar
    │   ├── LiquidGlassCard.kt    # Note card with specular highlight
    │   ├── LiquidGlassSearchBar.kt # Animated search bar
    │   ├── LiquidGlassPill.kt    # Frosted category pills
    │   ├── LiquidGlassFab.kt     # Floating action button with spring glow
    │   ├── LiquidGlassDialog.kt  # Confirmation & info dialogs
    │   └── TactileHaptics.kt     # Centralized tactile haptics helper
    ├── editor
    │   ├── NoteEditorViewModel.kt # Editor state & auto-save engine
    │   └── NoteEditorScreen.kt   # Freeform & checklist note editor
    ├── navigation
    │   └── ClearNoteNavHost.kt   # Fluid animated Compose navigation
    ├── notes
    │   ├── NotesViewModel.kt     # Main list state & search filtering
    │   ├── NotesListScreen.kt    # Main note list & responsive grid
    │   ├── CategorySelectorRow.kt# Horizontally scrollable category pills
    │   └── EmptyNotesState.kt    # Elegant crystal clear empty state
    └── theme
        ├── Color.kt              # Liquid glass color system & gradient brushes
        ├── Glassmorphism.kt      # Custom Compose modifiers for glass styling
        ├── SpringPhysics.kt      # Rubber-band bouncy overscroll modifier
        ├── Theme.kt              # System-wide ClearNote theme
        └── Type.kt               # Refined modern typography
```

---

## 🛡 Privacy & Offline Guarantee

ClearNote is engineered with a strict **offline-first philosophy**:
- No tracking SDKs (no Firebase, no telemetry, no third-party analytics)
- No network requests (no HTTP clients, no ads)
- All notes and checklists remain strictly on the local SQLite database on your device
- Complete data privacy by design

---

## 📦 Building the Project

Open the project directory in Android Studio or compile via command line:

```bash
# Set Java 17 Home
$env:JAVA_HOME="C:\Program Files\Java\jdk-17"

# Build Debug APK
./gradlew.bat assembleDebug

# Build Release APK
./gradlew.bat assembleRelease
```
