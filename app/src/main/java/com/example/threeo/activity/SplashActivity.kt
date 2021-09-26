package com.example.threeo.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.threeo.databinding.ActivitySplashBinding
import com.example.threeo.sharedPreferences.MyApplication
import java.time.chrono.HijrahChronology.INSTANCE


class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val first = MyApplication.prefs.getBoolean("isFirst", false)
        if (!first) { //최초 실행시
            //이용약관 동의 필요
            binding.apply {
                showTerm.visibility = View.VISIBLE
                termBtn.visibility = View.VISIBLE

                //button
                showTerm.setOnClickListener {
                    val intent = Intent(this@SplashActivity, TermsOfUesActivity::class.java)
                    startActivity(intent)
                }

                termBtn.setOnClickListener {
                    MyApplication.prefs.setBoolean("isFirst", true)
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            }

        } else {
            Handler().postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                finish()
            }, DURATION)
        }
    }
    companion object {
        private const val DURATION : Long = 1000
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}