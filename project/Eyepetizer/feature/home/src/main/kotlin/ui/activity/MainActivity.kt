package ui.activity

import android.app.Activity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.ComponentActivity
import com.example.home.R
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.home.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity

class MainActivity :  AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var navIds=intArrayOf(
        R.id.fragment_home,
                R.id.fragment_daily,
                R.id.fragment_find,
        R.id.fragment_mine
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment)

        val bottomBar = binding.includeBottomBar.bottomNavBar
        bottomBar.setDefaultPage()
        bottomBar.setSelectListener { pos ->
            navController.navigate(navIds[pos])
        }



    }
}