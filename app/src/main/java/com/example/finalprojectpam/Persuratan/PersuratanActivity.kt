package com.example.finalprojectpam.Persuratan

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.widget.Toolbar
import com.example.finalprojectpam.LandingActivity
import com.example.finalprojectpam.R
import com.example.finalprojectpam.SignInActivity
import com.google.firebase.auth.FirebaseAuth

class PersuratanActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar

    private lateinit var btnBuatSurat: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_persuratan)
        btnBuatSurat = findViewById(R.id.btnBuatSurat)


        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)
        toolbar = findViewById(R.id.toolbar)

        //btnNavigasi membuat surat
        btnBuatSurat.setOnClickListener {
            val intent = Intent(this, CreateSuratActivity::class.java)
            startActivity(intent)
        }


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
                    FirebaseAuth.getInstance().signOut()

                    val intent = Intent(this, SignInActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()

                }
            }
            drawerLayout.closeDrawers()
            true
        }
    }
}
