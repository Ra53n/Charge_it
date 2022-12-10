package chargeit.app.presentation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import chargeit.app.R
import chargeit.app.navigation.NavigatorImpl
import chargeit.navigator.Navigator
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.fragment_container
        ) as NavHostFragment

        navController = navHostFragment.navController

        loadKoinModules(module {single<Navigator> { NavigatorImpl(navController) }})

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.main, R.id.profile)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        val actionBar = supportActionBar

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.full_station_info) {
                actionBar?.let {
                    it.show()
                }
            } else {
                actionBar?.let {
                    it.hide()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }
}