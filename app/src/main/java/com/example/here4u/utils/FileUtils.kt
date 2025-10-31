package com.example.here4u.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File

object FileUtils {
    fun openPdf(context: Context, file: File) {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "application/pdf")
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        context.startActivity(intent)
    }
}
