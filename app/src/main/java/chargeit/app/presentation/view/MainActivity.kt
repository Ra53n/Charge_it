package chargeit.app.presentation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import chargeit.app.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, MainFragment.newInstance())
            .commit()
    }
}