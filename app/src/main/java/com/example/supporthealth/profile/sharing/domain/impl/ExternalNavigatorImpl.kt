package com.example.supporthealth.profile.sharing.domain.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.supporthealth.profile.sharing.domain.api.repository.ExternalNavigator
import com.example.supporthealth.profile.sharing.domain.models.EmailData

class ExternalNavigatorImpl(private val context: Context): ExternalNavigator {

    override fun openEmail(emailData: EmailData) {
        val supportIntent = Intent(Intent.ACTION_SENDTO)
        supportIntent.apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.targetEmail))
            putExtra(Intent.EXTRA_SUBJECT, emailData.subjectEmail)
            putExtra(Intent.EXTRA_TEXT, emailData.message)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(supportIntent)
    }
}