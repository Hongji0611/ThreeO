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
            val hour = allTime.toLong()/3600000
            val min = allTime.toLong()/60000 - hour*60
            val totalMin = (hour*60 + min);
            totalTime.text = "${hour}시간 ${min}분"

            //list를 관리하는 메니저 등록
            array.add(R.drawable.book)
            array.add(R.drawable.run)
            array.add(R.drawable.butterfly)
            array.add(R.drawable.music)
            array.add(R.drawable.money)
            array.add(R.drawable.turtle)
            array.add(R.drawable.movie)
            array.add(R.drawable.english)
            array.add(R.drawable.fog)
            array.add(R.drawable.rocket)
            array.add(R.drawable.teacher)
            array.add(R.drawable.otter)


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
                        R.drawable.book ->{
                            resultVal.text = "${hour / 6} 권 읽었다!"
                            flag.text = "한 권당 6시간 기준"
                        }
                        R.drawable.run ->{
                            resultVal.text = "${totalMin * 9.6} Kcal 소모했다!"
                            flag.text = "달리기 1분 9.6kcal 기준"
                        }
                        R.drawable.butterfly->{
                            resultVal.text = "날개짓 ${totalMin / 500}번 했다!"
                            flag.text = "은줄팔랑나비 1분 500회 기준"
                        }
                        R.drawable.music->{
                            resultVal.text = "${totalMin / 3} 곡 들었다!"
                            flag.text = "1곡 3분 기준"
                        }
                        R.drawable.money->{
                            resultVal.text = "${hour * 8720} 원 벌었다!"
                            flag.text = "시급 8720원 기준"
                        }
                        R.drawable.turtle->{
                            resultVal.text = "${hour / 35} m 헤엄쳤다!"
                            flag.text = "바다거북 35km/h 기준"
                        }
                        R.drawable.movie->{
                            resultVal.text = "${hour / 2} 편 봤다!"
                            flag.text = "영화 한 편 2시간 기준"
                        }
                        R.drawable.english->{
                            resultVal.text = "${(totalMin / 1.3).toInt()} 개 외웠다!"
                            flag.text = "단어 1개 1분20초 기준"
                        }
                        R.drawable.fog->{
                            resultVal.text = "탄소 ${totalMin * 3.6} g 배출했다!"
                            flag.text = "인터넷 접속 시 1분 3.6g 기준 "
                        }
                        R.drawable.rocket->{
                            resultVal.text = "ISS까지 ${totalMin / 30} 번 다녀왔다!"
                            flag.text = "왕복 30분 기준"
                        }
                        R.drawable.teacher->{
                            resultVal.text = "2학점짜리 ${hour / 2} 주차 들었다!"
                            flag.text = "전공 2학점 1주 2시간 기준"
                        }
                        R.drawable.otter->{
                            resultVal.text = "조개 ${totalMin * 0.5}개 까먹었다!"
                            flag.text = "해달 1분 0.5개 섭취 기준"
                        }
                    }
                }
            }
            recyclerView2.adapter = adapter
        }
    }
}
