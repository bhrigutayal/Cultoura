package com.tourismclient.cultoura.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.tourismclient.cultoura.R
import com.tourismclient.cultoura.databinding.ActivityMainBinding
import com.tourismclient.cultoura.databinding.AppBarMainBinding
import com.tourismclient.cultoura.fragments.NearbyEventsFragment
import com.tourismclient.cultoura.fragments.SavedEventsFragment

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawer: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up toolbar
        val toolbar: Toolbar = binding.appBarMain.toolbarMainActivity
        setSupportActionBar(toolbar)

        // Set up drawer navigation
        drawer = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        // Set up bottom navigation
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            navigateToBottomNavDestination(item.itemId)
        }

        // Default fragment on first launch
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, NearbyEventsFragment())
                .commit()
            bottomNavigationView.selectedItemId = R.id.navigation_nearby
        }
    }

    private fun navigateToBottomNavDestination(itemId: Int): Boolean {
        val selectedFragment: Fragment = when (itemId) {
            R.id.navigation_nearby -> NearbyEventsFragment()
            R.id.navigation_saved -> SavedEventsFragment()
            else -> return false
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, selectedFragment)
            .commit()
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle drawer navigation item clicks here
        when (item.itemId) {
            R.id.nav_plans -> {
                startActivity(Intent(this, SelectPlanTypeActivity::class.java))
            }
        }

        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}