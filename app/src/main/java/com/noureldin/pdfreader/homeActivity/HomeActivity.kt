package com.noureldin.pdfreader.homeActivity

import android.Manifest
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.noureldin.pdfreader.R
import com.noureldin.pdfreader.adapter.PDFAdapter
import java.io.File
import java.util.Collections
import java.util.Locale

class HomeActivity : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    var adapter: PDFAdapter? = null
    var list: MutableList<File>? = null
    var progressBar: ProgressBar? = null
    var searchView: androidx.appcompat.widget.SearchView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        checkPermission()
    }
    private fun checkPermission() {
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport) {
                    setuprv()
                }

                override fun onPermissionRationaleShouldBeShown(
                    list: List<PermissionRequest>,
                    permissionToken: PermissionToken
                ) {
                    permissionToken.continuePermissionRequest()
                }
            })
            .check()
    }

    private fun setupsearch() {
        searchView!!.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText != null) {
                    filter(newText)
                } else {
                    Toast.makeText(this@HomeActivity, "No File Found", Toast.LENGTH_SHORT).show()
                }
                return false
            }
        })
    }

    private fun filter(newText: String) {
        val filterlist: MutableList<File> = ArrayList()
        for (item in list!!) {
            if (item.name.lowercase(Locale.getDefault()).contains(newText)) {
                filterlist.add(item)
            }
        }
        adapter!!.filterlist(filterlist)
    }

    private fun setuprv() {
        recyclerView = findViewById(R.id.rv_files)
        progressBar = findViewById(R.id.progressBar)
        searchView = findViewById(R.id.searchView)
        list = ArrayList()
        progressBar?.visibility = View.VISIBLE
        recyclerView?.layoutManager = LinearLayoutManager(this)
        recyclerView?.setHasFixedSize(true)
        setupsearch()
        Thread {
            val files = getallFiles()
            // show latest
            Collections.sort(files) { o1, o2 ->
                java.lang.Long.valueOf(o2.lastModified())
                    .compareTo(o1.lastModified())
            }
            list!!.addAll(files)
            runOnUiThread {
                adapter = PDFAdapter(this@HomeActivity, list!!)
                recyclerView?.adapter = adapter
                handleUiRendering()
            }
        }.start()
    }

    private fun handleUiRendering() {
        progressBar!!.visibility = View.GONE
        if (adapter!!.itemCount == 0) {
            Toast.makeText(this, "No Pdf File In Phone", Toast.LENGTH_SHORT).show()
        } else {
            recyclerView!!.visibility = View.VISIBLE
        }
    }

    private fun getallFiles(): List<File> {
        val uri = MediaStore.Files.getContentUri("external")
        val projection = arrayOf(MediaStore.Files.FileColumns.DATA)
        val selection = MediaStore.Files.FileColumns.MIME_TYPE + "=?"
        val selectionArgs = arrayOf("application/pdf")
        val cursor = contentResolver.query(uri, projection, selection, selectionArgs, null)
        val list = ArrayList<File>()
        val pdfPathIndex = cursor!!.getColumnIndex(MediaStore.Files.FileColumns.DATA)
        while (cursor.moveToNext()) {
            if (pdfPathIndex != -1) {
                val pdfPath = cursor.getString(pdfPathIndex)
                val pdfFile = File(pdfPath)
                if (pdfFile.exists() && pdfFile.isFile) {
                    list.add(pdfFile)
                }
            }
        }
        cursor.close()
        return list
    }
}