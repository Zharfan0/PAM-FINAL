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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


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

        // Tombol tanggal
        binding.btnTanggal.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    binding.btnTanggal.text = "$dayOfMonth/${month + 1}/$year"
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }
    }

    private fun submitSurat() {
        val tanggal = binding.btnTanggal.text.toString()
        val perihal = binding.etPerihalSurat.text.toString()
        val isi = binding.etIsiSurat.text.toString()
        val ketua = binding.etKetua.text.toString()
        val tujuan = binding.etTujuan.text.toString()
        val nomor = binding.etNomorSurat.text.toString()

        if (tanggal.isEmpty() || perihal.isEmpty() || isi.isEmpty() || ketua.isEmpty() || tujuan.isEmpty()) {
            Toast.makeText(this, "Harap isi semua kolom!", Toast.LENGTH_SHORT).show()
            return
        }

        val surat = Surat(
            tanggal = tanggal,
            perihal = perihal,
            isi = isi,
            ketua = ketua,
            tujuan = tujuan,
            nomor = nomor,
            divisi = "Sekre IT",  // nanti ambil dari user profile
            status = "Draft"
        )

        val suratId = database.push().key
        if (suratId != null) {
            database.child(suratId).setValue(surat)
                .addOnSuccessListener {
                    Toast.makeText(this, "Surat berhasil disimpan!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Gagal menyimpan: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}

