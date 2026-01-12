package com.example.finalprojectpam.Proposal

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finalprojectpam.R
import com.google.firebase.database.FirebaseDatabase

class RevProposalActivity : AppCompatActivity() {

    private lateinit var proposalId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_revisi_proposal)

        // Ambil ID surat
        proposalId = intent.getStringExtra("proposalId") ?: ""

        if (proposalId.isEmpty()) {
            Toast.makeText(this, "ID Surat tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val etNmProgram = findViewById<TextView>(R.id.etNmProgram)
        val etKetua = findViewById<TextView>(R.id.etKetua)
        val etTotalDana = findViewById<TextView>(R.id.etTotalDana)
        val etTglProker = findViewById<TextView>(R.id.etTglProker)

        val etRevisi = findViewById<EditText>(R.id.etRevisi)
        val btnSimpan = findViewById<Button>(R.id.btnSimpan)

        val ref = FirebaseDatabase.getInstance()
            .getReference("proposal")
            .child(proposalId)

        // ================= LOAD DATA =================
        ref.get().addOnSuccessListener { snapshot ->
            val proposal = snapshot.getValue(Proposal::class.java)

            if (proposal == null) {
                Toast.makeText(this, "Data proposal tidak ditemukan", Toast.LENGTH_SHORT).show()
                finish()
                return@addOnSuccessListener
            }

            // (Opsional tapi disarankan)
            if (proposal.status != "Pending") {
                Toast.makeText(this, "proposal tidak dapat direvisi", Toast.LENGTH_SHORT).show()
                finish()
                return@addOnSuccessListener
            }

            // Tampilkan data surat (READ ONLY)
            etNmProgram.text = proposal.nmProgram
            etKetua.text = proposal.ketuPlak
            etTotalDana.text = proposal.ttlDana
            etTglProker.text = proposal.tglProgram
        }

        // ================= SIMPAN REVISI =================
        btnSimpan.setOnClickListener {
            val revisi = etRevisi.text.toString().trim()

            if (revisi.isEmpty()) {
                Toast.makeText(this, "Catatan revisi tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dataRevisi = mapOf(
                "revisi" to revisi,
                "status" to "Revisi"
            )

            ref.updateChildren(dataRevisi)
                .addOnSuccessListener {
                    Toast.makeText(this, "Catatan revisi berhasil dikirim", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal mengirim revisi", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
