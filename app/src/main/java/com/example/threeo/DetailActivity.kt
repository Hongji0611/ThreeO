package com.example.threeo

import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.threeo.adapter.AppListAdapter
import com.example.threeo.adapter.IfListAdapter
import com.example.threeo.data.TimeData
import com.example.threeo.databinding.ActivityDetailBinding
import java.util.ArrayList

class DetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailBinding

    var allTime:String = ""
    var appName:String = ""
    lateinit var appIcon:Bitmap

    lateinit var adapter: IfListAdapter
    var array = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        allTime = intent.getStringExtra("totalTime").toString()
        appName = intent.getStringExtra("appName").toString()
//        appIcon = intent.getParcelableExtra("appBitmap")!!
        init()
    }

    fun init(){
        binding.apply {
//            val drawable = BitmapDrawable(resources, appIcon)
            iconImg.setImageResource(R.mipmap.ic_launcher_round)
            appTitle.text = appName
            val hour = allTime.toLong()/3600000
            val min = allTime.toLong()/60000 - hour*60
            val totalMin = (hour*60 + min);
            totalTime.text = "${hour}시간 ${min}분"

            //list를 관리하는 메니저 등록
            array.add("독서")
            array.add("달리기")
            array.add("음악")
            array.add("알바")
            array.add("영화")
            array.add("영어단어")
            array.add("우주선")
            array.add("강의")
            array.add("탄소배출량")
            array.add("바다거북이")
            array.add("나비")
            recyclerView2.layoutManager = LinearLayoutManager(this@DetailActivity, LinearLayoutManager.HORIZONTAL,false)
            adapter = IfListAdapter(array)

            adapter.itemClickListener = object : IfListAdapter.OnItemClickListener{
                override fun OnItemClick(
                    holder: IfListAdapter.MyViewHolder,
                    view: View,
                    data: String,
                    position: Int
                ) {
                    when(adapter.items[position]){
                        "독서"->{
                            resultVal.text = "${hour / 6} 권"
                        }
                        "달리기"->{
                            resultVal.text = "${totalMin * 9.6} 칼로리"
                        }
                        "음악"->{
                            resultVal.text = "${totalMin / 3} 곡"
                        }
                        "알바"->{
                            resultVal.text = "${hour * 8720} 원"
                        }
                        "영화"->{
                            resultVal.text = "${hour / 2} 편"
                        }
                        "영어단어"->{
                            resultVal.text = "${totalMin / 1.3} 개"
                        }
                        "우주선"->{
                            resultVal.text = "${totalMin / 30} 번 왕복"
                        }
                        "강의"->{
                            resultVal.text = "${hour / 2} 주차 수강"
                        }
                    }
                }
            }
            recyclerView2.adapter = adapter
        }
    }
}
