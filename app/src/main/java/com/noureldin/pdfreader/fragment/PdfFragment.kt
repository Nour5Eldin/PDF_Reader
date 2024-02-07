package com.noureldin.pdfreader.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.PDFView
import com.google.android.material.appbar.MaterialToolbar
import com.noureldin.pdfreader.R
import java.io.File

class PdfFragment : Fragment() {
    private lateinit var pdfView: PDFView
    private lateinit var pdfPath: String

    companion object {
        private const val ARG_PDF_PATH = "pdf_path"

        fun newInstance(pdfPath: String): PdfFragment {
            val fragment = PdfFragment()
            val args = Bundle()
            args.putString(ARG_PDF_PATH, pdfPath)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pdf, container, false)

        // Set up the toolbar
        val toolbar: MaterialToolbar = view.findViewById(R.id.pdfToolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { requireActivity().onBackPressed()}
        pdfView = view.findViewById(R.id.pdfView)

        // Get PDF path from arguments
        pdfPath = arguments?.getString(ARG_PDF_PATH, "") ?: ""

        // Load and display PDF
        displayPdf()

        return view


    }

    private fun displayPdf() {
        // Use AndroidPdfViewer to load and display PDF
        pdfView.fromFile(File(pdfPath))
            .defaultPage(0)
            .enableSwipe(true)
            .swipeHorizontal(false)
            .load()
    }

}