package com.example.finalprojectpam.Proposal

import android.database.sqlite.SQLiteProgram

data class Proposal(
    var id: String = "",
    val nmProgram: String = "",
    val ketuPlak: String = "",
    val ttlDana: String = "",
    val tglProgram: String = "",
    val divisi: String = "",
    var status: String = "Draft"
)
