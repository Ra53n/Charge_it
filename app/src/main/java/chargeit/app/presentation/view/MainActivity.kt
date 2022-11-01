package chargeit.app.presentation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import chargeit.app.R
import chargeit.main_screen.view.MapsFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, MapsFragment.newInstance())
            .commit()
    }
}