package com.example.davidblanco.projectod


import android.app.FragmentManager
import android.app.FragmentTransaction
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_view_navigation.*
import kotlinx.android.synthetic.main.app_bar_view_navigation.*

class ViewNavigation : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var email: String? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_navigation)
        setSupportActionBar(toolbar)
        var sharedPreferences: SharedPreferences =getSharedPreferences("Credenciales", Context.MODE_PRIVATE)
        email = sharedPreferences.getString("email","email")
        val fm: android.support.v4.app.FragmentManager? = getSupportFragmentManager()
        fm!!.beginTransaction().replace(R.id.container, ProjectFragment()).commit()
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.view_navigation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.

        val fm: android.support.v4.app.FragmentManager? = getSupportFragmentManager()
        when (item.itemId) {

            R.id.nav_project -> {
                fm!!.beginTransaction().replace(R.id.container, ProjectFragment()).commit()
            }
            R.id.nav_profile -> {
                var second: ProfileFragment = ProfileFragment()
                var bundle: Bundle = Bundle();
                bundle.putString("Email", email)
                second.setArguments(bundle)
              fm!!.beginTransaction().replace(R.id.container, second).commit()
            }
            R.id.nav_stadistic -> {
                fm!!.beginTransaction().replace(R.id.container, StadisticFragment()).commit()
            }
            R.id.nav_about -> {
                fm!!.beginTransaction().replace(R.id.container, AboutFragment()).commit()
            }
            R.id.nav_signOff -> {
                var sharedPreferences: SharedPreferences = getSharedPreferences("Credenciales", Context.MODE_PRIVATE)
                var editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putBoolean("sesion", false)
                editor.commit()
                val viewLogin = Intent(applicationContext, ViewLogin::class.java)//lanza la siguiente actividad
                this.finish();
                startActivity(viewLogin)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
