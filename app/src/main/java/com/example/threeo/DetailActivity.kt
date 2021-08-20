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
    var array = ArrayList<Int>()

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
            totalTime.text = "${allTime.toLong()/3600000}시간 ${(allTime.toLong()%3600000)/60000}분"

            //list를 관리하는 메니저 등록
            array.add(R.drawable.book)
            array.add(R.drawable.run)
            array.add(R.drawable.music)
            array.add(R.drawable.money)
            array.add(R.drawable.english)
            array.add(R.drawable.rocket)
            array.add(R.drawable.lecture)

            recyclerView2.layoutManager = LinearLayoutManager(this@DetailActivity, LinearLayoutManager.HORIZONTAL,false)
            adapter = IfListAdapter(array)

            adapter.itemClickListener = object : IfListAdapter.OnItemClickListener{
                override fun OnItemClick(
                    holder: IfListAdapter.MyViewHolder,
                    view: View,
                    data: Int,
                    position: Int
                ) {
                    when(adapter.items[position]){
                        R.drawable.book->{
                            resultVal.text = "${500} 권"
                        }
                        R.drawable.run->{
                            resultVal.text = "${500} 칼로리"
                        }
                        R.drawable.music->{
                            resultVal.text = "${500} 곡"
                        }
                        R.drawable.money->{
                            resultVal.text = "${500} 원"
                        }
                        R.drawable.english->{
                            resultVal.text = "${500} 개"
                        }
                        R.drawable.rocket->{
                            resultVal.text = "${500} 번 왕복"
                        }
                        R.drawable.lecture->{
                            resultVal.text = "${500} 번 부화"
                        }
                    }
                }
            }
            recyclerView2.adapter = adapter
        }
    }
}