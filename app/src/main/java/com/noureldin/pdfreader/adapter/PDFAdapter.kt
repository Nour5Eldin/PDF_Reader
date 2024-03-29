package com.noureldin.pdfreader.adapter

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ShareCompat
import androidx.recyclerview.widget.RecyclerView
import com.noureldin.pdfreader.R
import com.noureldin.pdfreader.homeActivity.ViewerActivity
import java.io.File


class PDFAdapter(var activity: Activity, var list: List<File>) :
    RecyclerView.Adapter<PDFAdapter.ViewHolder>() {
    fun filterlist(list: List<File>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.pdf_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val file = list[position]
        holder.name.text = file.name
        holder.path.text = file.path
        holder.itemView.setOnClickListener {
            val intent = Intent(activity, ViewerActivity::class.java)
            intent.putExtra("name", file.name)
            intent.putExtra("path", file.path)
            activity.startActivity(intent)
        }
        holder.share.setOnClickListener {
            val intent = ShareCompat.IntentBuilder.from(activity).setType("application/pdf")
                .setStream(Uri.parse(file.path)).setChooserTitle("Choose app")
                .createChooserIntent()
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            activity.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var path: TextView
        var share: ImageView

        init {
            name = itemView.findViewById(R.id.file_name)
            path = itemView.findViewById(R.id.file_path)
            share = itemView.findViewById(R.id.share)
        }
    }
}
