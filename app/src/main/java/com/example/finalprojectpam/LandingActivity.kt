package com.example.finalprojectpam

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.finalprojectpam.Persuratan.PersuratanActivity
import com.example.finalprojectpam.Proposal.ProposalActivity

class LandingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        // Inisialisasi View
        val tvRole = findViewById<TextView>(R.id.tvRole)
        val btnPersuratan = findViewById<Button>(R.id.btnPersuratan)
        val btnProposal = findViewById<Button>(R.id.btnProposal)

        // Placeholder role (nanti diganti dari DB / session)
        tvRole.text = "SEKRE DIVISI"

        // Navigasi ke halaman Persuratan (dummy dulu)
        btnPersuratan.setOnClickListener {
            val intent = Intent(this, PersuratanActivity::class.java)
            startActivity(intent)
        }

        // Navigasi ke halaman Proposal (dummy dulu)
        btnProposal.setOnClickListener {
            val intent = Intent(this, ProposalActivity::class.java)
            startActivity(intent)
        }
    }
}
