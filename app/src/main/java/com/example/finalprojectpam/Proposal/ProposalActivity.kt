package com.example.finalprojectpam.Proposal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.finalprojectpam.LandingActivity
import com.example.finalprojectpam.Persuratan.RiwayatSuratActivity
import com.example.finalprojectpam.R
import com.example.finalprojectpam.SignInActivity
import com.google.android.material.navigation.NavigationView

class ProposalActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proposal)

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)
        toolbar = findViewById(R.id.toolbar)

        // Set toolbar sebagai ActionBar
        setSupportActionBar(toolbar)

        // Tombol hamburger (≡)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open,
            R.string.close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Navigasi menu
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {

                R.id.menu_home -> {
                    startActivity(Intent(this, LandingActivity::class.java))
                }

                R.id.menu_daftar_surat -> {
                    // Stay di halaman ini
                }

                R.id.menu_riwayat -> {
                    startActivity(Intent(this, RiwayatSuratActivity::class.java))
                }

                R.id.menu_logout -> {
                    startActivity(Intent(this, SignInActivity::class.java))
                    finish()
                }
            }
            drawerLayout.closeDrawers()
            true
        }
    }
}