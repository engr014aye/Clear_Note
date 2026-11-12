# ProGuard rules for ClearNote (Offline-first Productivity Tool)

# Room Database
-keepclassmembers class * extends androidx.room.RoomDatabase {
    <init>();
}
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# Keep Data Models & Entities
-keep class com.umerini.clearnote.data.local.entity.** { *; }
-keep class com.umerini.clearnote.data.model.** { *; }

# Kotlin Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Compose
-keepclassmembers class * extends androidx.compose.runtime.State { *; }
