package com.example.finalprojectpam.Proposal

import android.database.sqlite.SQLiteProgram

data class Proposal(
    var id: String = "",
    val NmProgram: String = "",
    val TglProgram: String = "",
    val KetuPlak: String = "",
    val TtlDana: String = "",
    val Divisi: String = "",
    var status: String = "Draft"
)
