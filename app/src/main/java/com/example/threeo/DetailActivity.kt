package com.example.threeo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.threeo.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailBinding

    var allTime:String = ""
    var appName:String = ""
    var appIcon:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        allTime = intent.getStringExtra("totalTime").toString()
        appName = intent.getStringExtra("appName").toString()
        appIcon = intent.getIntExtra("appIcon",0)
        init()
    }

    fun init(){
        binding.apply {
            iconImg.setImageResource(appIcon)
            appTitle.text = appName
            totalTime.text = allTime
        }
    }
}