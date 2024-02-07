package com.noureldin.pdfreader.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.noureldin.pdfreader.R
import com.noureldin.pdfreader.model.PdfModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PdfListAdapter(private val pdfFiles: List<PdfModel>) :
RecyclerView.Adapter<PdfListAdapter.ViewHolder>() {

    private var onItemClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        this.onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.pdf_item_list, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pdfFile = pdfFiles[position]

        holder.nameTextView.text = pdfFile.name
        holder.sizeTextView.text = "${pdfFile.size} KB"
        holder.timeTextView.text = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            .format(Date(pdfFile.time))

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(position)
        }
    }

    override fun getItemCount(): Int {
        return pdfFiles.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.file_name)
        val sizeTextView: TextView = itemView.findViewById(R.id.file_size)
        val timeTextView: TextView = itemView.findViewById(R.id.file_time)
    }
}
