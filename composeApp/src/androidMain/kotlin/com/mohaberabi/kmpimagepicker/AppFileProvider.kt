package com.mohaberabi.kmpimagepicker

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.util.Objects

class AppFileProvider : FileProvider(
    R.xml.path_provider
) {

    companion object {
        fun createFile(
            context: Context,
        ): Uri {
            val file = File.createTempFile(
                "pic_${System.currentTimeMillis()}",
                ".png",
                context.cacheDir
            ).apply {
                createNewFile()
            }
            val auth = context.applicationContext.packageName + ".provider"
            return getUriForFile(
                Objects.requireNonNull(context),
                auth,
                file,
            )
        }
    }
}