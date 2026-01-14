package com.example.finalprojectpam.Proposal

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finalprojectpam.Persuratan.Surat
import com.example.finalprojectpam.R
import com.google.firebase.database.FirebaseDatabase

class EditProposalActivity : AppCompatActivity() {

    private lateinit var proposalId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_proposal)

        // Ambil ID surat dari intent
        proposalId = intent.getStringExtra("proposalId") ?: ""

        if (proposalId.isEmpty()) {
            Toast.makeText(this, "ID Surat tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val etTanggal = findViewById<EditText>(R.id.etTanggal)
        val etNmProgram = findViewById<EditText>(R.id.etNmProgram)
        val etKetua = findViewById<EditText>(R.id.etKetua)
        val etTotalDana = findViewById<EditText>(R.id.etTotalDana)
        val btnSimpan = findViewById<Button>(R.id.btnSimpan)

        val ref = FirebaseDatabase.getInstance().getReference("proposal").child(proposalId)

        // ================= LOAD DATA =================
        ref.get().addOnSuccessListener { snapshot ->
            val proposal = snapshot.getValue(Proposal::class.java)

            if (proposal == null) {
                Toast.makeText(this, "Data proposal tidak ditemukan", Toast.LENGTH_SHORT).show()
                finish()
                return@addOnSuccessListener
            }

            // 🔐 Validasi status
            if (proposal.status != "Revisi") {
                Toast.makeText(this, "Hanya proposal Revisi yang bisa diedit", Toast.LENGTH_SHORT).show()
                finish()
                return@addOnSuccessListener
            }

            // Isi form
            etTanggal.setText(proposal.tglProgram)
            etNmProgram.setText(proposal.nmProgram)
            etKetua.setText(proposal.ketuPlak)
            etTotalDana.setText(proposal.ttlDana)
        }

        // ================= SIMPAN =================
        btnSimpan.setOnClickListener {
            val updatedData = mapOf(
                "tglProgram" to etTanggal.text.toString(),
                "nmProgram" to etNmProgram.text.toString(),
                "ketuPlak" to etKetua.text.toString(),
                "ttlDana" to etTotalDana.text.toString(),
                "status" to "Pending"
            )

            ref.updateChildren(updatedData).addOnSuccessListener {
                Toast.makeText(this, "Proposal berhasil diperbarui", Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "Gagal menyimpan perubahan", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
