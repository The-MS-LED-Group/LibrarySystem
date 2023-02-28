package com.github.msledgroup.culturalcenterlibrary

import io.github.g0dkar.qrcode.QRCode

interface QRCodeFoundListener {
    fun onQRCodeFound(qrCode: String?)
    fun qrCodeNotFound()
}