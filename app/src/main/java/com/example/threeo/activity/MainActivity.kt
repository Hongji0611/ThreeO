package com.example.threeo.activity

import android.app.AppOpsManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.threeo.R
import com.example.threeo.adapter.AppListAdapter
import com.example.threeo.adapter.IfListAdapter
import com.example.threeo.data.DetailData
import com.example.threeo.data.TimeData
import com.example.threeo.databinding.ActivityMainBinding
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var appAdapter: AppListAdapter
    private lateinit var metaphorAdapter: IfListAdapter

    private val standardText = arrayOf("한 권당 6시간 기준",
        "달리기 1분 9.6kcal 기준",
        "은줄팔랑나비 1분 500회 기준",
        "1곡 3분 기준",
        "시급 8720원 기준",
        "바다거북 35km/h 기준",
        "영화 한 편 2시간 기준",
        "단어 1개 1분20초 기준",
        "인터넷 접속 시 1분 3.6g 기준 ",
        "왕복 30분 기준",
        "전공 2학점 1주 2시간 기준",
        "해달 1분 0.5개 섭취 기준",
    )

    private var dateType = false
    private var standardType = 0

    private var allTime:Long = 0L
    private var userId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    //화면 생성시 초기화
    private fun init(){
        binding.apply {
            //Metaphor list 매니저 등록
            metaphorAdapter = IfListAdapter(setMetaphorList())
            metaphorAdapter.itemClickListener = object : IfListAdapter.OnItemClickListener{
                override fun OnItemClick(
                    holder: IfListAdapter.MyViewHolder,
                    view: View,
                    data: DetailData,
                    position: Int
                ) {
                    standard.text = standardText[position]
                    standardType = position
                    metaphorAdapter.settingPushBtn(position)
                }
            }
            metaphorRecyclerView.adapter = metaphorAdapter

            //app List 매니저 등록
            appAdapter = AppListAdapter(arrayListOf())
            appAdapter.itemClickListener = object :AppListAdapter.OnItemClickListener{
                override fun OnItemClick(
                    holder: AppListAdapter.MyViewHolder,
                    view: View,
                    data: TimeData,
                    position: Int
                ) {
                    val intent = Intent(this@MainActivity, DetailActivity::class.java)
                    startActivity(intent)
                }
            }
            appRecyclerView.adapter = appAdapter

            //버튼 이벤트 추가
            menu.setOnClickListener {
                val intent = Intent(this@MainActivity, MenuActivity::class.java)
                startActivityForResult (intent, 100)
            }

            today.setOnClickListener {
                dateType = false
                today.setBackgroundResource(R.drawable.push_box)
                week.setBackgroundResource(R.drawable.fill_box)
                getList()
            }

            week.setOnClickListener {
                dateType = true
                today.setBackgroundResource(R.drawable.fill_box)
                week.setBackgroundResource(R.drawable.push_box)
                getList()
            }
        }
    }

    private fun setMetaphorList(): ArrayList<DetailData> {
        return arrayListOf<DetailData>(
            DetailData(0, R.drawable.book, true),
            DetailData(1, R.drawable.run),
            DetailData(2, R.drawable.butterfly),
            DetailData(3, R.drawable.music),
            DetailData(4, R.drawable.money),
            DetailData(5, R.drawable.turtle),
            DetailData(6, R.drawable.movie),
            DetailData(7, R.drawable.english),
            DetailData(8, R.drawable.fog),
            DetailData(9, R.drawable.teacher),
            DetailData(10, R.drawable.otter)
        )
    }

    private fun getList(){
        //스크린 타임 권한 요청
        if(!checkForPermission()) {
            Toast.makeText(
                this@MainActivity, "권한 설정이 필요합니다.",
                Toast.LENGTH_LONG
            ).show()
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }else{
            //스크린 타임 리스트 가져오기
            val usageStats: MutableList<UsageStats> = getAppUsageStats()
            showAppUsageStats(usageStats)
        }
    }

    //패키지 명과 등등 앱 정보 가져오기
    private fun showAppUsageStats(usageStats: MutableList<UsageStats>) {
        usageStats.sortWith(Comparator { right, left ->
            compareValues(left.lastTimeUsed, right.lastTimeUsed)
        })

        allTime = 0L
        var count = 0
        var arrayList = arrayListOf<TimeData>()

        usageStats.forEach {
            try {
                val icon: Drawable = this.packageManager.getApplicationIcon(it.packageName)
                val p: PackageInfo = this.packageManager.getPackageInfo(it.packageName, 0)
                val appname = p.applicationInfo.loadLabel(packageManager).toString()
                if (count < 10 && it.totalTimeInForeground.toString() != "0" && icon.toString() != "android.graphics.drawable.AdaptiveIconDrawable@eedfade") {
                    Log.e(
                        "ThreeO",
                        "패키지명: ${it.packageName}, 이미지명: $icon lastTimeUsed: ${Date(it.lastTimeUsed)}, " +
                                "totalTimeInForeground: ${it.totalTimeInForeground}"
                    )
                    arrayList.add(
                        TimeData(
                            icon,
                            appname,
                            (it.totalTimeInForeground).toString(),
                            it.packageName
                        )
                    )
                    allTime += it.totalTimeInForeground
                    count++
                }
            } catch (e : PackageManager.NameNotFoundException){
                Log.e(
                    "ThreeO",
                    "패키지명: ${it.packageName} totalTimeInForeground: ${it.totalTimeInForeground}")
            }
        }

        appAdapter.setData(arrayList)
        binding.usage.text = "${allTime/3600000}시간 ${(allTime%3600000)/60000}분"

    }

    //앱 정보 가져올 때 주기 설정
    private fun getAppUsageStats(): MutableList<UsageStats> {
        val cal = Calendar.getInstance()
        cal.add(Calendar.YEAR, -1)

        val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        return when(dateType){
            false-> {
                usageStatsManager
                    .queryUsageStats(
                        UsageStatsManager.INTERVAL_DAILY, cal.timeInMillis,
                        System.currentTimeMillis()
                    )
            }
            true-> {
                usageStatsManager
                    .queryUsageStats(
                        UsageStatsManager.INTERVAL_WEEKLY, cal.timeInMillis,
                        System.currentTimeMillis()
                    )
            }
        }
    }

    //권한 확인
    private fun checkForPermission(): Boolean {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), packageName)
        return mode == AppOpsManager.MODE_ALLOWED
    }
}