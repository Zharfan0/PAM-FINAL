package com.example.finalprojectpam.Persuratan

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finalprojectpam.R
import com.example.finalprojectpam.databinding.ActivityCreateSuratBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar

class CreateSuratActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateSuratBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateSuratBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi Firebase Database
        database = FirebaseDatabase.getInstance().getReference("surat")

        // Tombol Submit
        binding.btnSubmit.setOnClickListener {
            submitSurat()
        }

        // Tombol Batal
        binding.btnBatal.setOnClickListener {
            finish() // kembali ke activity sebelumnya
        }

        binding.btnTanggal.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    binding.btnTanggal.text = "$dayOfMonth/${month + 1}/$year"
                },
                calendar.get(Calendar.YEAR),  // Tahun sekarang
                calendar.get(Calendar.MONTH), // Bulan sekarang
                calendar.get(Calendar.DAY_OF_MONTH) // Hari sekarang
            )
            datePicker.show() // tampilkan kalender
        }

    }

    private fun submitSurat() {
        // Ambil data dari EditText
        val tanggal = binding.btnTanggal.text.toString() // nanti bisa diubah ke DatePicker
        val perihal = binding.etPerihalSurat.text.toString()
        val isi = binding.etIsiSurat.text.toString()
        val ketua = binding.etKetua.text.toString()
        val tujuan = binding.etTujuan.text.toString()
        val nomor = binding.etNomorSurat.text.toString()

        // Validasi sederhana
        if (tanggal.isEmpty() || perihal.isEmpty() || isi.isEmpty() || ketua.isEmpty() || tujuan.isEmpty()) {
            Toast.makeText(this, "Harap isi semua kolom!", Toast.LENGTH_SHORT).show()
            return
        }

        // Buat object Surat
        val surat = Surat(
            tanggal = tanggal,
            perihal = perihal,
            isi = isi,
            ketua = ketua,
            tujuan = tujuan,
            nomor = nomor
        )

        // Generate ID unik untuk surat
        val suratId = database.push().key

        if (suratId != null) {
            database.child(suratId).setValue(surat)
                .addOnSuccessListener {
                    Toast.makeText(this, "Surat berhasil disimpan!", Toast.LENGTH_SHORT).show()
                    finish() // kembali ke halaman sebelumnya
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Gagal menyimpan: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
