package com.example.here4u.data.remote.repositories

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Properties
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

object EmailSender {

    suspend fun sendEmail(
        recipientEmail: String,
        recipientName: String,
        locationMessage: String,
        senderName: String?,
        username: String,
        appPassword: String
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val props = Properties().apply {
                put("mail.smtp.auth", "true")
                put("mail.smtp.starttls.enable", "true")
                put("mail.smtp.host", "smtp.gmail.com")
                put("mail.smtp.port", "587")
            }

            val session = Session.getInstance(props, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(username, appPassword)
                }
            })

            val message = MimeMessage(session).apply {
                setFrom(InternetAddress(username, "Here4U Emergency Alert"))
                setRecipient(Message.RecipientType.TO, InternetAddress(recipientEmail))
                subject = "🚨 Emergency Alert from $senderName - Here4U App"
                setContent(
                    """
                    <h2 style="color: #d32f2f;">🚨 EMERGENCY ALERT</h2>
                    <p>Dear <strong>$recipientName</strong>,</p>

                    <p>This is an <strong>emergency alert</strong> from <strong>$senderName</strong> using the Here4U app.</p>

                    <div style="background-color: #ffebee; padding: 15px; border-left: 4px solid #d32f2f; margin: 15px 0;">
                        <h3 style="color: #d32f2f; margin-top: 0;">Location Information:</h3>
                        <p style="font-family: monospace; background: #f5f5f5; padding: 10px; border-radius: 4px;">
                            ${locationMessage.replace("\n", "<br>")}
                            <a href=https://www.google.com/maps/search/?api=1&query=${locationMessage} style="color:#1976d2;">Open in Google Maps</a>
                        </p>
                    </div>

                    <p><strong>Please reach out to $senderName as soon as possible.</strong></p>

                    <p style="margin-top: 30px; font-size: 14px; color: #666;">
                        Best regards,<br>
                        <strong>Here4U Team</strong><br>
                        <em>Mental Health Support App</em>
                    </p>
                    """.trimIndent(),
                    "text/html; charset=utf-8"
                )
            }

            Transport.send(message)
            Log.d("EmailSender", "Email sent to $recipientEmail")
            true
        } catch (e: Exception) {
            Log.e("EmailSender", "Error sending email to $recipientEmail: ${e.message}")
            false
        }
    }
}
