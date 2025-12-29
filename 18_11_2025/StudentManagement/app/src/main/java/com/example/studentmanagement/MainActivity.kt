package com.example.studentmanagement

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Thiết lập Navigation Controller
        // Đảm bảo file layout activity_main.xml đã có FragmentContainerView với id là nav_host_fragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as? NavHostFragment

        if (navHostFragment != null) {
            val navController = navHostFragment.navController
            // (Tuỳ chọn) Setup Action Bar để hiển thị tiêu đề Fragment
            // setupActionBarWithNavController(navController)
        }
    }
}