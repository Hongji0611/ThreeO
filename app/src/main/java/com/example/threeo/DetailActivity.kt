package com.example.threeo

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.threeo.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailBinding

    var allTime:String = ""
    var appName:String = ""
    var appIcon:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        allTime = intent.getStringExtra("totalTime").toString()
        appName = intent.getStringExtra("appName").toString()
//        appIcon = intent.getStringExtra("appPackageName").toString()
        init()
    }

    fun init(){
        binding.apply {
//            iconImg.setImageResource(appIcon)
            appTitle.text = appName
            totalTime.text = allTime
        }
    }
}