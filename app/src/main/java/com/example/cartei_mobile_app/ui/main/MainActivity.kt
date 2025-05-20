package com.example.cartei_mobile_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cartei_mobile_app.R
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.fragment.NavHostFragment



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val satzId = intent.getIntExtra("satzId", 0)
        supportFragmentManager.setFragmentResult("satzIdKey", Bundle().apply {
            putInt("satzId", satzId)
        })


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_start -> {
                    navController.popBackStack(R.id.nav_start, false)
                    navController.navigate(R.id.nav_start)
                    true
                }
                R.id.nav_karten -> {
                    navController.popBackStack(R.id.nav_karten, false)
                    navController.navigate(R.id.nav_karten)
                    true
                }
                R.id.nav_fortschritt -> {
                    navController.popBackStack(R.id.nav_fortschritt, false)
                    navController.navigate(R.id.nav_fortschritt)
                    true
                }
                else -> false
            }
        }
    }


}
