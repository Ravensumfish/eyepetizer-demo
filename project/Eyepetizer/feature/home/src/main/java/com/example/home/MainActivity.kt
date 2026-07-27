package com.example.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import com.example.home.databinding.ActivityMainBinding
import androidx.navigation.findNavController
import com.therouter.router.Route

@Route(path = "/feature/home/MainActivity")
class MainActivity :  AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val navIds = intArrayOf(
        R.id.fragment_home,
        R.id.fragment_daily,
        R.id.fragment_discovery,
        R.id.fragment_mine
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 设置窗口
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // 绑定布局
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.navHostFragment.post {
            navController = binding.navHostFragment.findNavController()

            // 设置底部导航监听
            binding.bottomBar.setSelectListener { position ->
                when (position) {
                    0 -> navController.navigate(R.id.fragment_home)
                    1 -> navController.navigate(R.id.fragment_daily)
                    2 -> navController.navigate(R.id.fragment_discovery)
                    3 -> navController.navigate(R.id.fragment_mine)
                }
            }

            // 设置默认选中首页
            binding.bottomBar.setDefaultPage()
        }
    }


}