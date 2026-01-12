package com.example.finalprojectpam

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.finalprojectpam.Persuratan.PersuratanActivity
import com.example.finalprojectpam.Proposal.ProposalActivity
import com.google.firebase.auth.FirebaseAuth

class LandingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        // =====================
        // 1. Ambil session user
        // =====================
        val prefs = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
        val role = prefs.getString("ROLE", null)

        // Jika session rusak → logout paksa
        if (role == null) {
            FirebaseAuth.getInstance().signOut()
            finish()
            return
        }

        // =====================
        // 2. Inisialisasi View
        // =====================
        val tvRole = findViewById<TextView>(R.id.tvRole)
//        val btnTambah = findViewById<Button>(R.id.btnTambahSurat)
//        val btnACC = findViewById<Button>(R.id.btnACC)
//        val btnDelete = findViewById<Button>(R.id.btnDelete)
        val btnPersuratan = findViewById<Button>(R.id.btnPersuratan)
        val btnProposal = findViewById<Button>(R.id.btnProposal)

        // =====================
        // 3. Set teks role
        // =====================
        tvRole.text = when (role) {
            "SEKRE_DIVISI" -> "SEKRETARIS DIVISI"
            "SEKRE_UMUM" -> "SEKRETARIS UMUM"
            else -> "UNKNOWN ROLE"
        }

        // =====================
        // 4. Atur menu berdasarkan role
        // =====================
//        when (role) {
//            "SEKRE_DIVISI" -> {
//                btnTambah.visibility = View.VISIBLE
//                btnACC.visibility = View.GONE
//                btnDelete.visibility = View.GONE
//            }
//
//            "SEKRE_UMUM" -> {
//                btnTambah.visibility = View.GONE
//                btnACC.visibility = View.VISIBLE
//                btnDelete.visibility = View.VISIBLE
//            }
//        }

        // =====================
        // 5. Navigasi halaman
        // =====================
        btnPersuratan.setOnClickListener {
            startActivity(Intent(this, PersuratanActivity::class.java))
        }

        btnProposal.setOnClickListener {
            startActivity(Intent(this, ProposalActivity::class.java))
        }
    }
}
