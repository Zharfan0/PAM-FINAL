package com.example.finalprojectpam.Proposal

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
import com.example.finalprojectpam.Proposal.Proposal
import com.example.finalprojectpam.R
import com.google.firebase.database.FirebaseDatabase

class ProposalAdapter(
    private val context: Context,
    private val list: ArrayList<Proposal>,
    private val role: String = ""
) : RecyclerView.Adapter<ProposalAdapter.ProposalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProposalViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_proposal, parent, false)
        return ProposalViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ProposalViewHolder, position: Int) {
        val proposal = list[position]

        // Set text
        holder.tvProker.text = proposal.nmProgram
        holder.tvDivisiProker.text = proposal.divisi
        holder.tvTglProker.text = proposal.tglProgram
        holder.tvStatus.text = proposal.status

        // Set warna status
        holder.tvStatus.setBackgroundColor(getStatusColor(proposal.status))

        // Alternating row background
        holder.itemView.setBackgroundColor(if (position % 2 == 0) Color.parseColor("#FAFAFA") else Color.WHITE)

        // Tombol Action
        holder.btnAction.isEnabled = proposal.status != "Selesai"
        holder.btnAction.setOnClickListener {
            showPopupMenu(holder.btnAction, proposal, holder.tvStatus)
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

    private fun showPopupMenu(button: Button, proposal: Proposal, tvStatus: TextView) {
        val popupMenu = PopupMenu(context, button)

        val isSekreDivisi = role == "SEKRE_DIVISI"

        when (proposal.status) {
            "Draft" -> {
                popupMenu.menu.add("Kirim")
                if (!isSekreDivisi) {
                    popupMenu.menu.add("Delete")
                }
            }

            "Pending" -> {
                if (!isSekreDivisi) {
                    popupMenu.menu.add("Revisi")
                    popupMenu.menu.add("ACC")
                }
            }

            "Revisi" -> {
                popupMenu.menu.add("Edit Revisi")
                if (!isSekreDivisi) {
                    popupMenu.menu.add("Delete")
                }
            }

            "ACC" -> {
                if (!isSekreDivisi) {
                    popupMenu.menu.add("Delete")
                }
                popupMenu.menu.add("Selesai")
            }
        }

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.title.toString()) {
                "Kirim" -> updateStatus(proposal, "Pending", tvStatus)
                "Revisi" -> {
                    val intent = Intent(context, RevProposalActivity::class.java)
                    intent.putExtra("proposalId", proposal.id)
                    context.startActivity(intent)
                }
                "ACC" -> updateStatus(proposal, "ACC", tvStatus)
                "Selesai" -> updateStatus(proposal, "Selesai", tvStatus)
                "Delete" -> deleteProposal(proposal)
                "Edit Revisi" -> {
                    val intent = Intent(context, EditProposalActivity::class.java)
                    intent.putExtra("proposalId", proposal.id)
                    context.startActivity(intent)
                }
            }
            true
        }

        popupMenu.show()

        // styling tetap (punya kamu)
        for (i in 0 until popupMenu.menu.size()) {
            val menuItem = popupMenu.menu.getItem(i)
            val s = SpannableString(menuItem.title)
            s.setSpan(ForegroundColorSpan(Color.WHITE), 0, s.length, 0)
            menuItem.title = s
        }

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



    private fun updateStatus(proposal: Proposal, newStatus: String, tvStatus: TextView) {
        val ref = FirebaseDatabase.getInstance().getReference("proposal").child(proposal.id)
        ref.child("status").setValue(newStatus).addOnSuccessListener {
            proposal.status = newStatus
            tvStatus.text = newStatus
            tvStatus.setBackgroundColor(getStatusColor(newStatus))
            Toast.makeText(context, "Status updated: $newStatus", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to update status", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteProposal(proposal: Proposal) {
        val ref = FirebaseDatabase.getInstance().getReference("proposal").child(proposal.id)
        ref.removeValue().addOnSuccessListener {
            Toast.makeText(context, "Proposal deleted", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to delete proposal", Toast.LENGTH_SHORT).show()
        }
    }

    class ProposalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvProker: TextView = itemView.findViewById(R.id.tvProker)
        val tvDivisiProker: TextView = itemView.findViewById(R.id.tvDivisiProker)
        val tvTglProker: TextView = itemView.findViewById(R.id.tvTglProker)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val btnAction: Button = itemView.findViewById(R.id.btnAction)
    }
}
