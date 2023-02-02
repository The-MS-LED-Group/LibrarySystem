package com.github.msledgroup.culturalcenterlibrary

import android.graphics.ImageFormat.*
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.multi.qrcode.QRCodeMultiReader
import java.nio.ByteBuffer

class QRScanner(private var listener: QRCodeFoundListener?) : ImageAnalysis.Analyzer{


    override fun analyze(image: ImageProxy) {
        if(image.format == YUV_420_888 || image.format == YUV_422_888 || image.format == YUV_444_888){
            val byteBuffer: ByteBuffer? = image.planes[0].buffer
            val imageData = ByteArray(byteBuffer!!.capacity())
            byteBuffer!!.get(imageData)

            val source = PlanarYUVLuminanceSource(
                imageData,
                image.width, image.height,
                0,0,
                image.width,image.height,
                false
            )
            val binaryBitmap = BinaryBitmap(HybridBinarizer(source))

            try{
                val result = QRCodeMultiReader().decode(binaryBitmap)
                listener?.onQRCodeFound(result.text)
            }catch (e: Exception ){
                when(e){
                    is FormatException,is ChecksumException, is NotFoundException -> {
                        listener?.qrCodeNotFound()
                    }
                }
            }

            image.close()
        }
    }


}