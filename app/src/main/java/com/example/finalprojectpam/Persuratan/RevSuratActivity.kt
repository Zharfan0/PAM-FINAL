package com.example.finalprojectpam.Persuratan

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finalprojectpam.R
import com.google.firebase.database.FirebaseDatabase

class RevSuratActivity : AppCompatActivity() {

    private lateinit var suratId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_revisi_surat)

        // Ambil ID surat dari intent
        suratId = intent.getStringExtra("suratId") ?: ""

        if (suratId.isEmpty()) {
            Toast.makeText(this, "ID Surat tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val etTanggal = findViewById<TextView>(R.id.etTanggal)
        val etPerihal = findViewById<TextView>(R.id.etPerihal)
        val etIsi = findViewById<TextView>(R.id.etIsi)
        val etKetua = findViewById<TextView>(R.id.etKetua)
        val etTujuan = findViewById<TextView>(R.id.etTujuan)
        val etNomor = findViewById<TextView>(R.id.etNomor)
        val btnSimpan = findViewById<Button>(R.id.btnSimpan)

        val ref = FirebaseDatabase.getInstance().getReference("surat").child(suratId)

        // ================= LOAD DATA =================
        ref.get().addOnSuccessListener { snapshot ->
            val surat = snapshot.getValue(Surat::class.java)

            if (surat == null) {
                Toast.makeText(this, "Data surat tidak ditemukan", Toast.LENGTH_SHORT).show()
                finish()
                return@addOnSuccessListener
            }

            // 🔐 Validasi status
            if (surat.status != "Revisi") {
                Toast.makeText(this, "Hanya surat Revisi yang bisa diedit", Toast.LENGTH_SHORT).show()
                finish()
                return@addOnSuccessListener
            }

            // Isi form
            etTanggal.setText(surat.tanggal)
            etPerihal.setText(surat.perihal)
            etIsi.setText(surat.isi)
            etKetua.setText(surat.ketua)
            etTujuan.setText(surat.tujuan)
            etNomor.setText(surat.nomor)
        }

        // ================= SIMPAN =================
        btnSimpan.setOnClickListener {
            val updatedData = mapOf(
                "perihal" to etPerihal.text.toString(),
                "isi" to etIsi.text.toString(),
                "ketua" to etKetua.text.toString(),
                "tujuan" to etTujuan.text.toString(),
                "nomor" to etNomor.text.toString()
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
