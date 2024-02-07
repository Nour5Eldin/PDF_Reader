package com.noureldin.pdfreader.homeActivity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.noureldin.pdfreader.R
import com.noureldin.pdfreader.adapter.PdfListAdapter
import com.noureldin.pdfreader.fragment.PdfFragment
import com.noureldin.pdfreader.model.PdfModel
import java.util.Locale

class HomeActivity : AppCompatActivity() {

    private lateinit var pdfListAdapter: PdfListAdapter
    private lateinit var pdfFiles: List<PdfModel>
    private val READ_STORAGE_PERMISSION_CODE = 1
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        progressBar = findViewById(R.id.progressBar)

        val recyclerView: RecyclerView = findViewById(R.id.pdfRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Request storage permission if not granted
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission already granted
            loadPdfFiles()
        } else {
            // Request permission if not granted
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_STORAGE_PERMISSION_CODE
            )
        }

    }

    private fun loadPdfFiles() {
        val recyclerView: RecyclerView = findViewById(R.id.pdfRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        pdfFiles = getPdfFilesFromStorage()
        pdfListAdapter = PdfListAdapter(pdfFiles)

        recyclerView.adapter = pdfListAdapter

        pdfListAdapter.setOnItemClickListener { position ->
            val selectedPdf = pdfFiles[position]
            openPdfFragment(selectedPdf)
        }
        progressBar.visibility = ProgressBar.GONE
    }

    private fun openPdfFragment(pdfModel: PdfModel) {
        val pdfFragment = PdfFragment.newInstance(pdfModel.path)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, pdfFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, load and display PDF files
                loadPdfFiles()
            } else {
                // Permission denied, handle accordingly (show a message, disable functionality, etc.)
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getPdfFilesFromStorage(): List<PdfModel> {
        val pdfFiles = mutableListOf<PdfModel>()

        // Get the external storage directory
        val externalStorageDirectory = Environment.getExternalStorageDirectory()

        // Check if the external storage directory is accessible
        if (externalStorageDirectory.exists() && externalStorageDirectory.isDirectory) {
            // Get all files in the external storage directory
            val files = externalStorageDirectory.listFiles()

            // Filter files with .pdf extension
            files?.filter { it.isFile && it.name.toLowerCase(Locale.ROOT).endsWith(".pdf") }
                ?.forEach {
                    pdfFiles.add(
                        PdfModel(
                            it.name,
                            it.absolutePath,
                            it.length(),
                            it.lastModified()
                        )
                    )
                }
        }

        return pdfFiles
    }
}