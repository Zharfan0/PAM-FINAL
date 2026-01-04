package com.example.finalprojectpam.Persuratan

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojectpam.R

class SuratAdapter(
    private val context: Context,
    private val suratList: List<Surat>
) : RecyclerView.Adapter<SuratAdapter.SuratViewHolder>() {

    inner class SuratViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama: TextView = view.findViewById(R.id.tvNama)
        val tvDivisi: TextView = view.findViewById(R.id.tvDivisi)
        val tvNoSurat: TextView = view.findViewById(R.id.tvNoSurat)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val btnAction: Button = view.findViewById(R.id.btnAction)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuratViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_surat, parent, false)
        return SuratViewHolder(view)
    }

    override fun onBindViewHolder(holder: SuratViewHolder, position: Int) {
        val surat = suratList[position]
        holder.tvNama.text = surat.ketua
        holder.tvDivisi.text = surat.divisi
        holder.tvNoSurat.text = surat.nomor
        holder.tvStatus.text = surat.status

        // Contoh action: tampilkan Toast
        holder.btnAction.setOnClickListener {
            Toast.makeText(context, "Action untuk ${surat.perihal}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = suratList.size
}
