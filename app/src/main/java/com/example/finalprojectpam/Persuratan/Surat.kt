package com.example.finalprojectpam.Persuratan

data class Surat(
    var id: String = "",
    val tanggal: String = "",
    val perihal: String = "",
    val isi: String = "",
    val ketua: String = "",
    val tujuan: String = "",
    val nomor: String = "",
    val divisi: String = "",
    val revisi: String = "",
    var status: String = "Draft"
)
