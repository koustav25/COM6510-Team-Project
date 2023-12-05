package com.example.myapplication.function

import android.content.Context
import android.content.Intent
import android.net.Uri

object Web {
    fun OpenWeb(context: Context) {
        val url = "https://www.google.com"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }
}