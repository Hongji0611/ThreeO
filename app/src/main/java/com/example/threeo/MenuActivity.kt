package com.example.threeo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.threeo.databinding.ActivityMainBinding
import com.example.threeo.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {
    lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }
    private fun init(){

    }
}