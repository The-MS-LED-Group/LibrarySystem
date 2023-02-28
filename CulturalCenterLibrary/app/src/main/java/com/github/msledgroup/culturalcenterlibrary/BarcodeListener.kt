package com.github.msledgroup.culturalcenterlibrary

interface BarcodeListener {
    fun onBarcodeFound(barCode: String?)

    fun barcodeNotFound()
}