package com.student.wine_me_up.barcode_scanner

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import devliving.online.mvbarcodereader.MVBarcodeScanner

// https://github.com/Credntia/MVBarcodeReader

class BarcodeScanner: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(com.student.wine_me_up.R.layout.activity_barcode_scanner)

        val mMode = MVBarcodeScanner.ScanningMode.SINGLE_MANUAL
//        MVBarcodeScanner.Builder()
//            .setScanningMode(mMode)
//            .setFormats(MVBarcodeScanner.BARCODE_FORMATS)
//            .build()
//            .launchScanner(this, REQ_CODE)
    }
}