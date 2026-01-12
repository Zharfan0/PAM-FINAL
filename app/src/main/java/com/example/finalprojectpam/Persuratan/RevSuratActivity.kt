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

        // Ambil ID surat
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
        val etRevisi = findViewById<EditText>(R.id.etRevisi)
        val btnSimpan = findViewById<Button>(R.id.btnSimpan)

        val ref = FirebaseDatabase.getInstance()
            .getReference("surat")
            .child(suratId)

        // ================= LOAD DATA =================
        ref.get().addOnSuccessListener { snapshot ->
            val surat = snapshot.getValue(Surat::class.java)

            if (surat == null) {
                Toast.makeText(this, "Data surat tidak ditemukan", Toast.LENGTH_SHORT).show()
                finish()
                return@addOnSuccessListener
            }

            // (Opsional tapi disarankan)
            if (surat.status != "Pending") {
                Toast.makeText(this, "Surat tidak dapat direvisi", Toast.LENGTH_SHORT).show()
                finish()
                return@addOnSuccessListener
            }

            // Tampilkan data surat (READ ONLY)
            etTanggal.text = surat.tanggal
            etPerihal.text = surat.perihal
            etIsi.text = surat.isi
            etKetua.text = surat.ketua
            etTujuan.text = surat.tujuan
            etNomor.text = surat.nomor
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
