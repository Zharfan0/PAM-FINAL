package com.example.finalprojectpam.Proposal

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finalprojectpam.databinding.ActivityCreateProposalBinding
import com.example.finalprojectpam.databinding.ActivityCreateSuratBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar


class CreateProposalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateProposalBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateProposalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi Firebase Database
        database = FirebaseDatabase.getInstance().getReference("proposal")

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
        val NmProgram = binding.etNmProgram.text.toString()
        val KetuPlak = binding.eKetua.text.toString()
        val TtlDana = binding.etTotalDana.text.toString()

        if (tanggal.isEmpty() || NmProgram.isEmpty() || KetuPlak.isEmpty() || TtlDana.isEmpty()) {
            Toast.makeText(this, "Harap isi semua kolom!", Toast.LENGTH_SHORT).show()
            return
        }

        val surat = Proposal(
            NmProgram = tanggal,
            TglProgram = NmProgram,
            KetuPlak = KetuPlak,
            TtlDana = TtlDana,
            Divisi = "Sekre IT",  // nanti ambil dari user profile
            Status = "Draft"
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

