package com.example.home

import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import com.example.home.R
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.home.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import com.example.home.BottomNavigationBarView
import androidx.core.view.WindowInsetsCompat
import com.alibaba.android.arouter.facade.annotation.Route

@Route(path = "/feature/home/MainActivity")
class MainActivity :  AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var navIds=intArrayOf(
        R.id.fragment_home,
        R.id.fragment_daily,
        R.id.fragment_discovery,
        R.id.fragment_mine
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window,false)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val rootLayout=findViewById<View>(android.R.id.content)
        ViewCompat.setOnApplyWindowInsetsListener(rootLayout){
            view,insets->
            val systemBars=insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                0,
                systemBars.top,
                0,
                systemBars.bottom
            )
            insets}


        navController = findNavController(R.id.nav_host_fragment)

        val bottomBar = binding.bottomBar
        bottomBar.setDefaultPage()
        bottomBar.setSelectListener { pos ->
            navController.navigate(navIds[pos])
        }



    }
}