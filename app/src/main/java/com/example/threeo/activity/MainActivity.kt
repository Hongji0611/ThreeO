package com.example.threeo.activity

import android.app.AppOpsManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.threeo.R
import com.example.threeo.`interface`.TodoApi
import com.example.threeo.adapter.AppListAdapter
import com.example.threeo.data.TimeData
import com.example.threeo.databinding.ActivityMainBinding
import com.example.threeo.json.AppData
import com.example.threeo.json.PostData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    lateinit var adapter: AppListAdapter
    var array = ArrayList<TimeData>()

    var findType = 0
    var calculateTime:Long = 0L
    var idByANDROID_ID = ""

    //xml파일과 코틀린 파일을 연결
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        getList()
    }

    //화면 생성시 초기화
    private fun init(){
        idByANDROID_ID = Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
        Log.e("idByANDROID_ID: ", idByANDROID_ID)

        binding.apply {
            //list를 관리하는 메니저 등록
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL,false)
            adapter = AppListAdapter(array)
            adapter.itemClickListener = object :AppListAdapter.OnItemClickListener{
                override fun OnItemClick(
                    holder: AppListAdapter.MyViewHolder,
                    view: View,
                    data: TimeData,
                    position: Int
                ) {
                    val intent = Intent(this@MainActivity, DetailActivity::class.java)
                    intent.putExtra("totalTime", adapter.items[position].time)
                    intent.putExtra("appName", adapter.items[position].appName)
                    intent.putExtra("packageName", adapter.items[position].packageStr)
                    intent.putExtra("idByANDROID_ID", idByANDROID_ID)
                    intent.putExtra("findType", findType)
                    startActivity(intent)
                }
            }
            recyclerView.adapter = adapter

            //버튼 이벤트 추가
            menu.setOnClickListener {
                val intent = Intent(this@MainActivity, MenuActivity::class.java)
                startActivityForResult (intent, 100)
            }
            day.setOnClickListener {
                findType = 1
                day.setBackgroundResource(R.drawable.push_box)
                week.setBackgroundResource(R.drawable.fill_box)
                month.setBackgroundResource(R.drawable.fill_box)
                year.setBackgroundResource(R.drawable.fill_box)
                getList()
                postList(findType)
            }

            week.setOnClickListener {
                findType = 2
                day.setBackgroundResource(R.drawable.fill_box)
                week.setBackgroundResource(R.drawable.push_box)
                month.setBackgroundResource(R.drawable.fill_box)
                year.setBackgroundResource(R.drawable.fill_box)
                getList()
                postList(findType)
            }

            month.setOnClickListener {
                day.setBackgroundResource(R.drawable.fill_box)
                week.setBackgroundResource(R.drawable.fill_box)
                month.setBackgroundResource(R.drawable.push_box)
                year.setBackgroundResource(R.drawable.fill_box)
                findType = 3
                getList()
                postList(findType)
            }

            year.setOnClickListener {
                day.setBackgroundResource(R.drawable.fill_box)
                week.setBackgroundResource(R.drawable.fill_box)
                month.setBackgroundResource(R.drawable.fill_box)
                year.setBackgroundResource(R.drawable.push_box)
                findType = 4
                getList()
                postList(findType)
            }

            show.setOnClickListener {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("totalTime", calculateTime.toString())
                intent.putExtra("appName", "나의 총 휴대폰 사용 시간")
                intent.putExtra("packageName", "전체시간")
                startActivity(intent)
            }
        }
    }

    private fun postList(period:Int){
        //Retrofit 객체 생성
        val retrofit = Retrofit.Builder()
            .baseUrl("https://wouldhavedone-back.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //retrofit 객체를 통해 인터페이스 생성
        val service = retrofit.create(TodoApi::class.java)

        //Body에 담을 데이터 생성
        val adapter_data = adapter.getData()
        var appList = ArrayList<AppData>()
        appList.add(AppData("전체시간", calculateTime)) //전체시간
        for(app in adapter_data){
            appList.add(AppData(app.appName, app.time.toLong()))
        }

        val now = Date().time
        var body = PostData(idByANDROID_ID, period, now, appList)

        service.postData(body).enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.d("Response:: ", response.body().toString())
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("CometChatAPI::", "Failed API call with call: " + call +
                        " + exception: " + t)
            }

        })
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
            adapter.clearData()
            val usageStats: MutableList<UsageStats> = getAppUsageStats()
            showAppUsageStats(usageStats)
        }
    }

    //패키지 명과 등등 앱 정보 가져오기
    private fun showAppUsageStats(usageStats: MutableList<UsageStats>) {
        usageStats.sortWith(Comparator { right, left ->
            compareValues(left.lastTimeUsed, right.lastTimeUsed)
        })
        calculateTime = 0L

        var count = 0
        usageStats.forEach {
            val icon: Drawable = this.packageManager.getApplicationIcon(it.packageName)
            val p: PackageInfo = this.packageManager.getPackageInfo(it.packageName, 0)
            val appname = p.applicationInfo.loadLabel(packageManager).toString()
            if(count <10 && it.totalTimeInForeground.toString() != "0" && icon.toString() != "android.graphics.drawable.AdaptiveIconDrawable@eedfade"){
                Log.e("ThreeO", "패키지명: ${it.packageName}, 이미지명: $icon lastTimeUsed: ${Date(it.lastTimeUsed)}, " +
                        "totalTimeInForeground: ${it.totalTimeInForeground}")
                adapter.addData(TimeData(icon, appname, (it.totalTimeInForeground).toString(), it.packageName))
                calculateTime += it.totalTimeInForeground
                count++
            }
        }
        binding.allTime.text = "${calculateTime/3600000}시간 ${(calculateTime%3600000)/60000}분"
    }

    //앱 정보 가져올 때 주기 설정
    private fun getAppUsageStats(): MutableList<UsageStats> {
        val cal = Calendar.getInstance()
        cal.add(Calendar.YEAR, -1)

        val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        return when(findType){
            1-> {
                usageStatsManager
                    .queryUsageStats(
                        UsageStatsManager.INTERVAL_DAILY, cal.timeInMillis,
                        System.currentTimeMillis()
                    )
            }
            2-> {
                usageStatsManager
                    .queryUsageStats(
                        UsageStatsManager.INTERVAL_WEEKLY, cal.timeInMillis,
                        System.currentTimeMillis()
                    )
            }
            3->{
                usageStatsManager
                    .queryUsageStats(
                        UsageStatsManager.INTERVAL_MONTHLY, cal.timeInMillis,
                        System.currentTimeMillis()
                    )
            }
            else -> usageStatsManager
                .queryUsageStats(
                    UsageStatsManager.INTERVAL_YEARLY, cal.timeInMillis,
                    System.currentTimeMillis()
                )
        }
    }

    //권한 확인
    private fun checkForPermission(): Boolean {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), packageName)
        return mode == AppOpsManager.MODE_ALLOWED
    }
}