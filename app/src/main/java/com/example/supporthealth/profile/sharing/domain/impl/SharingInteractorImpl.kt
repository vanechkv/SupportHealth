package com.example.supporthealth.profile.sharing.domain.impl

import android.content.Context
import com.example.supporthealth.R
import com.example.supporthealth.profile.sharing.domain.api.interactor.SharingInteractor
import com.example.supporthealth.profile.sharing.domain.api.repository.ExternalNavigator
import com.example.supporthealth.profile.sharing.domain.models.EmailData

class SharingInteractorImpl(
    private val context: Context,
    private val externalNavigator: ExternalNavigator
) : SharingInteractor {
    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            context.getString(R.string.support_email),
            context.getString(R.string.support_subject),
            context.getString(R.string.support_text)
        )
    }
}