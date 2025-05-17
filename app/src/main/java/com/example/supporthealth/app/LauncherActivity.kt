package com.example.supporthealth.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.supporthealth.main.ui.MainActivity
import com.example.supporthealth.welcome.detailsOnbording.domain.api.interactor.DetailsOnBordingInteractor
import com.example.supporthealth.welcome.main.ui.WelcomeActivity
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser
        val intent = if (user == null) {
            Intent(this, WelcomeActivity::class.java)
        } else {
            Intent(this, MainActivity::class.java)
        }

        startActivity(intent)
        finish()
    }
}