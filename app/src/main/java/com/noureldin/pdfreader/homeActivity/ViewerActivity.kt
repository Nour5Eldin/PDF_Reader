package com.noureldin.pdfreader.homeActivity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ShareCompat
import com.github.barteksc.pdfviewer.PDFView
import com.noureldin.pdfreader.R
import java.io.File

class ViewerActivity : AppCompatActivity() {
    var pdfView: PDFView? = null
    var back: ImageView? = null
    var share: ImageView? = null
    var title: TextView? = null
    var name: String? = null
    var path: String? = null
    var ishide = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewer)
        initvar()
    }
    private fun initvar() {
        back = findViewById(R.id.back)
        share = findViewById(R.id.share)
        title = findViewById(R.id.file_name)
        pdfView = findViewById(R.id.pdfView)
        back()
        share()
        getintentdata()
        showpdf()
        fullscreen()
    }

    private fun fullscreen() {
        pdfView!!.setOnClickListener {
            if (ishide) {
                findViewById<View>(R.id.materialToolbar).visibility = View.VISIBLE
                ishide = false
            } else {
                findViewById<View>(R.id.materialToolbar).visibility = View.GONE
                ishide = true
            }
        }
    }

    private fun showpdf() {
        pdfView!!.fromFile(File(path)).nightMode(true).load()
    }

    private fun getintentdata() {
        name = intent.getStringExtra("name")
        path = intent.getStringExtra("path")
        title!!.text = name
    }

    private fun back() {
        back!!.setOnClickListener { onBackPressed() }
    }

    private fun share() {
        share!!.setOnClickListener {
            val intent = ShareCompat.IntentBuilder.from(this@ViewerActivity).setType("application/pdf")
                .setStream(Uri.parse(path))
                .setChooserTitle("Choose app")
                .createChooserIntent()
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(intent)
        }
    }
}