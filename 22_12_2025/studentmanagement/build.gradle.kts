// Top-level build file where you can add configuration options common to all sub-projects/modules.
// File này nằm ở thư mục gốc, bên ngoài folder app
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    // Thêm dòng này:
    id("androidx.navigation.safeargs.kotlin") version "2.7.7" apply false
}