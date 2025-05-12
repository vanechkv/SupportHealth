package com.example.supporthealth.profile.sharing.domain.api.repository

import com.example.supporthealth.profile.sharing.domain.models.EmailData

interface ExternalNavigator {

    fun openEmail(emailData: EmailData)
}