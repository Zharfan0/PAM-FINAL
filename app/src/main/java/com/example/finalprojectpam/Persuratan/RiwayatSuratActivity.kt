package com.example.finalprojectpam.Persuratan

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojectpam.LandingActivity
import com.example.finalprojectpam.R
import com.example.finalprojectpam.SignInActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RiwayatSuratActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat_surat)

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

        val rvSurat: RecyclerView = findViewById(R.id.rvSurat)
        val database = FirebaseDatabase.getInstance().getReference("surat")
        rvSurat.layoutManager = LinearLayoutManager(this)

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<Surat>()
                for (data in snapshot.children) {
                    val surat = data.getValue(Surat::class.java)
                    if (surat != null) {
                        surat.id = data.key ?: ""
                        // ✅ Filter hanya status "Selesai"
                        if (surat.status == "Selesai") {
                            list.add(surat)
                        }
                    }
                }
                rvSurat.adapter = SuratAdapter(this@RiwayatSuratActivity, list)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@RiwayatSuratActivity, "Gagal load data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })


        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Navigasi menu
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {

                R.id.menu_home -> {
                    startActivity(Intent(this, LandingActivity::class.java))
                }

                R.id.menu_daftar_surat -> {
                    startActivity(Intent(this, PersuratanActivity::class.java))
                }

                R.id.menu_riwayat -> {
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
