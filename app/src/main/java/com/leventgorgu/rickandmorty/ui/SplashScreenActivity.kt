package com.leventgorgu.rickandmorty.ui


import com.leventgorgu.rickandmorty.R

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.leventgorgu.rickandmorty.databinding.ActivitySplashScreenBinding


class SplashScreenActivity : AppCompatActivity() {
    private lateinit var activitySplashBinding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySplashBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(activitySplashBinding.root)

        val splashTextView =activitySplashBinding.TextView

        val sharedPreferences = getPreferences(MODE_PRIVATE)
        if (sharedPreferences.getBoolean("isFirstTime", true)) {
            splashTextView.text =  getString(R.string.splash_screen_message_welcome)
            val editor = sharedPreferences.edit()
            editor.putBoolean("isFirstTime", false)
            editor.apply()
        } else {
            splashTextView.text =   getString(R.string.splash_screen_message_hello)
        }

        activitySplashBinding.imageView.animation = AnimationUtils.loadAnimation(this,R.anim.anim_logo)
        activitySplashBinding.TextView.animation = AnimationUtils.loadAnimation(this,R.anim.anim_text)

        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
            finish()
        },2000)

        supportActionBar?.hide()
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.white)))
    }
}