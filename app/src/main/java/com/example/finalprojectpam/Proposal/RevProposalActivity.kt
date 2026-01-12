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

        // Ambil ID surat dari intent
        proposalId = intent.getStringExtra("proposalId") ?: ""

        if (proposalId.isEmpty()) {
            Toast.makeText(this, "ID Surat tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val etNmProgram = findViewById<EditText>(R.id.etNmProgram)
        val etKetua = findViewById<EditText>(R.id.etKetua)
        val etTotalDana = findViewById<EditText>(R.id.etTotalDana)
        val etTglProker = findViewById<TextView>(R.id.etTglProker)
        val btnSimpan = findViewById<Button>(R.id.btnSimpan)

        val ref = FirebaseDatabase.getInstance().getReference("surat").child(proposalId)

        // ================= LOAD DATA =================
        ref.get().addOnSuccessListener { snapshot ->
            val proposal = snapshot.getValue(Proposal::class.java)

            if (proposal == null) {
                Toast.makeText(this, "Data surat tidak ditemukan", Toast.LENGTH_SHORT).show()
                finish()
                return@addOnSuccessListener
            }

            // 🔐 Validasi status
            if (proposal.status != "Revisi") {
                Toast.makeText(this, "Hanya surat Revisi yang bisa diedit", Toast.LENGTH_SHORT).show()
                finish()
                return@addOnSuccessListener
            }

            // Isi form
            etNmProgram.setText(proposal.nmProgram)
            etKetua.setText(proposal.ketuPlak)
            etTotalDana.setText(proposal.ttlDana)
            etTglProker.setText(proposal.tglProgram)
        }

        // ================= SIMPAN =================
        btnSimpan.setOnClickListener {
            val updatedData = mapOf(
                "NmProgram" to etNmProgram.text.toString(),
                "Ketua Pelaksana" to etKetua.text.toString(),
                "Total Dana" to etTotalDana.text.toString(),
                "Tanggal Program" to etTglProker.text.toString(),
            )

            ref.updateChildren(updatedData).addOnSuccessListener {
                Toast.makeText(this, "Surat berhasil diperbarui", Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "Gagal menyimpan perubahan", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
