package com.app.vocab.app

import android.app.Application
import com.app.vocab.core.data.Preferences
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class VocabApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Preferences.getInstance(applicationContext)
    }
}