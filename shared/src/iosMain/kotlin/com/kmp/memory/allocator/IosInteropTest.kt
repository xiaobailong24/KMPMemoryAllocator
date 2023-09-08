@file:OptIn(BetaInteropApi::class, ExperimentalForeignApi::class, ExperimentalForeignApi::class)

package com.kmp.memory.allocator

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.sizeOf
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGBitmapContextCreate
import platform.CoreGraphics.CGBitmapContextCreateImage
import platform.CoreGraphics.CGColorSpaceCreateDeviceRGB
import platform.CoreGraphics.CGContextRelease
import platform.CoreGraphics.CGImageAlphaInfo
import platform.CoreGraphics.CGImageRelease
import platform.UIKit.UIImage


internal class Bitmap(
    var image: UIImage?
) {
    val imageSize: Int
        get() = image?.size()?.size ?: 0

    val width: Int
        get() = (image?.size?.useContents { width }?.toInt()) ?: 0
    val height: Int
        get() = (image?.size?.useContents { height }?.toInt()) ?: 0
}

internal class UIImageFactory {
    fun createImage(width: Int, height: Int): UIImage = memScoped {
        val bytesPerPixel = 4
        val bytesPerRow = bytesPerPixel * width
        val bitsPerComponent = 8

        // C interop memory allocation: https://kotlinlang.org/docs/native-c-interop.html#memory-allocation
        val bytes: CPointer<ByteVar> = allocArray(height * bytesPerRow)
        val colorSpace = CGColorSpaceCreateDeviceRGB()
        val bitmapInfo = CGImageAlphaInfo.kCGImageAlphaPremultipliedFirst.value
        val context = CGBitmapContextCreate(
            bytes,
            width.toULong(),
            height.toULong(),
            bitsPerComponent.toULong(),
            bytesPerRow.toULong(),
            colorSpace,
            bitmapInfo
        )
        // 创建CGImage
        val cgImage = CGBitmapContextCreateImage(context)
        // 释放内存
        val tempUIImage: UIImage = UIImage.imageWithCGImage(cgImage)
        CGImageRelease(cgImage)
        CGContextRelease(context)
        return tempUIImage
    }
}

object IosInteropTest {
    fun test() =
//        autoreleasepool {
        UIImageFactory().createImage(8 * 1024, 8 * 1024).run {
            val b = Bitmap(this)
            println("IosInteropTest| ${b.imageSize}=${b.width}*${b.height}")
        }
//        }

//        memScoped {
//            // 8MB
//            allocArray<ByteVar>(8 * 1024 * 1024).run {
//                println("sizeOf<ByteVar>()=${sizeOf<ByteVar>()}")
//            }
//        }
}