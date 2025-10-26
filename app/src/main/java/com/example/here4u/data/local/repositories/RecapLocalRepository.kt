package com.example.here4u.data.local.repositories

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RecapLocalRepository @Inject constructor(
    @ApplicationContext private val context: Context
){private val prefs = context.getSharedPreferences("RecapPrefs",Context.MODE_PRIVATE)
    private val LAST_SAVE_KEY="last_save_time"

    fun shouldSaveSummary(): Boolean {
        val lastSaved = prefs.getLong(LAST_SAVE_KEY, 0L)
        val oneWeekMillis = 7 * 24 * 60 * 60 * 1000L
        val now = System.currentTimeMillis()
        return (now - lastSaved) > oneWeekMillis
    }

    fun updateLastSaveTime() {
        prefs.edit().putLong(LAST_SAVE_KEY, System.currentTimeMillis()).apply()
    }

    fun saveSummary(summary: String) {
        try {
            val formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd")
            val date = LocalDate.now().format(formatter)
            val fileName = "weekly_summary_$date.pdf"
            val file = File(context.getExternalFilesDir(null), fileName)

            val pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 (595x842 puntos)
            val page = pdfDocument.startPage(pageInfo)

            val canvas = page.canvas
            val paint = Paint().apply {
                textSize = 14f
                color = Color.BLACK
            }
            val leftMargin = 60f
            var y = 80f

            paint.textAlign= Paint.Align.CENTER
            paint.typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
            paint.textSize = 20f
            paint.isFakeBoldText = true
            canvas.drawText("Weekly Recap - $date", pageInfo.pageWidth/2f, y, paint)

            y += 40f
            paint.textAlign= Paint.Align.LEFT
            paint.textSize = 14f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.isFakeBoldText = false

            y += 30f

            // 🔹 Línea divisoria
            paint.strokeWidth = 1f
            canvas.drawLine(leftMargin, y, pageInfo.pageWidth - leftMargin, y, paint)


            y += 40f

            paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            paint.textSize = 13f
            paint.color = Color.DKGRAY
            val textWidth = pageInfo.pageWidth - leftMargin * 2
            val textPaint = TextPaint(paint)
            val staticLayout = StaticLayout.Builder.obtain(summary, 0, summary.length, textPaint, textWidth.toInt())
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setLineSpacing(1.2f, 1f)
                .build()
            canvas.save()
            canvas.translate(leftMargin, y)
            staticLayout.draw(canvas)
            canvas.restore()

            pdfDocument.finishPage(page)
            pdfDocument.writeTo(FileOutputStream(file))
            pdfDocument.close()
            Log.d("RecapLocalRepo", "Resumen guardado en ${file.absolutePath}")
        } catch (e: Exception) {
            Log.e("RecapLocalRepo", "Error al guardar resumen: ${e.message}", e)
        }
    }
    fun getLastSavedPdf(): File? {
        val dir = context.getExternalFilesDir(null) ?: return null
        val pdfFiles = dir.listFiles { _, name -> name.endsWith(".pdf") } ?: return null
        return pdfFiles.maxByOrNull { it.lastModified() }
    }



}