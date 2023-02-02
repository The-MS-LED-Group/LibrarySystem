package com.github.msledgroup.culturalcenterlibrary

interface QRCodeFoundListener {
    fun onQRCodeFound(qrCode: String?)
    fun qrCodeNotFound()
}


