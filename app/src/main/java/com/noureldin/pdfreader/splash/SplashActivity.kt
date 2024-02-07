package com.noureldin.pdfreader.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.noureldin.pdfreader.R
import com.noureldin.pdfreader.homeActivity.HomeActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initSplash()
    }

    private fun initSplash() {
        Handler(mainLooper).postDelayed({
         startActivity(Intent(this,HomeActivity::class.java))
        },2500)
    }
}