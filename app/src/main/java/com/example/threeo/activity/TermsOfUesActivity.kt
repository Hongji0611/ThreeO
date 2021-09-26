package com.example.threeo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.threeo.databinding.ActivityTermsOfUesBinding

class TermsOfUesActivity : AppCompatActivity() {
    lateinit var binding: ActivityTermsOfUesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsOfUesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = getSharedPreferences("isFirst", MODE_PRIVATE)
        val first = pref.getBoolean("isFirst", false)
        if (!first) { //최초 실행시
            Log.d("Is first Time?", "first")
            val editor = pref.edit()
            editor.commit()
            //이용약관 동의 필요
            binding.apply {

                backBtn.setOnClickListener {
                    onBackPressed()
                }

                startBtn.setOnClickListener {
                    editor.putBoolean("isFirst", true)
                    val intent = Intent(this@TermsOfUesActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        } else {
            val intent = Intent(this@TermsOfUesActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}