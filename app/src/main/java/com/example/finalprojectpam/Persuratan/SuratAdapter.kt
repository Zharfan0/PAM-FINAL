package com.example.finalprojectpam.Persuratan

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojectpam.R
import com.google.firebase.database.FirebaseDatabase

class SuratAdapter(
    private val context: Context,
    private val list: ArrayList<Surat>
) : RecyclerView.Adapter<SuratAdapter.SuratViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuratViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_surat, parent, false)
        return SuratViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: SuratViewHolder, position: Int) {
        val surat = list[position]

        // Set text
        holder.tvNama.text = surat.ketua
        holder.tvDivisi.text = surat.divisi
        holder.tvNoSurat.text = surat.nomor
        holder.tvStatus.text = surat.status

        // Set warna status
        holder.tvStatus.setBackgroundColor(getStatusColor(surat.status))

        // Alternating row background
        holder.itemView.setBackgroundColor(if (position % 2 == 0) Color.parseColor("#FAFAFA") else Color.WHITE)

        // Tombol Action
        holder.btnAction.isEnabled = surat.status != "Selesai"
        holder.btnAction.setOnClickListener {
            showPopupMenu(holder.btnAction, surat, holder.tvStatus)
        }
    }

    private fun getStatusColor(status: String): Int {
        return when (status) {
            "Draft" -> Color.GRAY
            "Pending" -> Color.parseColor("#FFC107") // Amber
            "Revisi" -> Color.parseColor("#FF9800") // Orange
            "ACC" -> Color.parseColor("#4CAF50") // Green
            "Selesai" -> Color.parseColor("#2196F3") // Blue
            else -> Color.GRAY
        }
    }

    private fun showPopupMenu(button: Button, surat: Surat, tvStatus: TextView) {
        val popupMenu = PopupMenu(context, button)

        // Tambahkan menu sesuai status
        when (surat.status) {
            "Draft" -> { popupMenu.menu.add("Kirim"); popupMenu.menu.add("Delete") }
            "Pending" -> { popupMenu.menu.add("Revisi"); popupMenu.menu.add("ACC") }
            "Revisi" -> { popupMenu.menu.add("Edit Revisi"); popupMenu.menu.add("Delete") }
            "ACC" -> { popupMenu.menu.add("Delete"); popupMenu.menu.add("Selesai") }
        }

        // Set warna text & background
        try {
            val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
            fieldMPopup.isAccessible = true
            val menuPopupHelper = fieldMPopup.get(popupMenu)
            val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
            val setForceIcons = classPopupHelper.getMethod("setForceShowIcon", Boolean::class.javaPrimitiveType)
            setForceIcons.invoke(menuPopupHelper, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.title.toString()) {
                "Kirim" -> updateStatus(surat, "Pending", tvStatus)
                "Revisi" -> updateStatus(surat, "Revisi", tvStatus)
                "ACC" -> updateStatus(surat, "ACC", tvStatus)
                "Selesai" -> updateStatus(surat, "Selesai", tvStatus)
                "Delete" -> deleteSurat(surat)
                "Edit Revisi" -> {
                    val intent = Intent(context, EditSuratActivity::class.java)
                    intent.putExtra("suratId", surat.id)
                    context.startActivity(intent)
                }
            }
            true
        }

        // **Custom background & text warna**
        popupMenu.setOnDismissListener { /* optional */ }
        popupMenu.show()

        // Setelah show, iterasi semua menu item
        for (i in 0 until popupMenu.menu.size()) {
            val menuItem = popupMenu.menu.getItem(i)
            val s = SpannableString(menuItem.title)
            s.setSpan(ForegroundColorSpan(Color.WHITE), 0, s.length, 0)
            menuItem.title = s
        }

        // Set background via popup window (API 23+)
        try {
            val menuHelperField = PopupMenu::class.java.getDeclaredField("mPopup")
            menuHelperField.isAccessible = true
            val menuPopupHelper = menuHelperField.get(popupMenu)
            val popup = menuPopupHelper.javaClass.getMethod("getPopup").invoke(menuPopupHelper) as androidx.appcompat.widget.ListPopupWindow
            popup.setBackgroundDrawable(ColorDrawable(Color.parseColor("#2196F3"))) // biru
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun updateStatus(surat: Surat, newStatus: String, tvStatus: TextView) {
        val ref = FirebaseDatabase.getInstance().getReference("surat").child(surat.id)
        ref.child("status").setValue(newStatus).addOnSuccessListener {
            surat.status = newStatus
            tvStatus.text = newStatus
            tvStatus.setBackgroundColor(getStatusColor(newStatus))
            Toast.makeText(context, "Status updated: $newStatus", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to update status", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteSurat(surat: Surat) {
        val ref = FirebaseDatabase.getInstance().getReference("surat").child(surat.id)
        ref.removeValue().addOnSuccessListener {
            Toast.makeText(context, "Surat deleted", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to delete surat", Toast.LENGTH_SHORT).show()
        }
    }

    class SuratViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvNama)
        val tvDivisi: TextView = itemView.findViewById(R.id.tvDivisi)
        val tvNoSurat: TextView = itemView.findViewById(R.id.tvNoSurat)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val btnAction: Button = itemView.findViewById(R.id.btnAction)
    }
}
