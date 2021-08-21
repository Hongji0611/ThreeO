package com.example.threeo

import android.R.drawable
import android.app.AppOpsManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.threeo.adapter.AppListAdapter
import com.example.threeo.data.TimeData
import com.example.threeo.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    lateinit var adapter: AppListAdapter
    var array = ArrayList<TimeData>()

    var findType = 0

    //xml파일과 코틀린 파일을 연결
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    //화면 생성시 초기화
    private fun init(){
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
//                    val bitmap = (adapter.items[position].img as BitmapDrawable).bitmap
//                    intent.putExtra("appBitmap", bitmap)
                    startActivity(intent)
                }
            }
            recyclerView.adapter = adapter

            //버튼 이벤트 추가
            menu.setOnClickListener {
                val intent = Intent(this@MainActivity, MenuActivity::class.java)
                startActivity(intent)
            }
            day.setOnClickListener {
                findType = 1
                day.setBackgroundResource(R.drawable.push_box)
                week.setBackgroundResource(R.drawable.fill_box)
                month.setBackgroundResource(R.drawable.fill_box)
                year.setBackgroundResource(R.drawable.fill_box)
                getList()
            }

            week.setOnClickListener {
                findType = 2
                day.setBackgroundResource(R.drawable.fill_box)
                week.setBackgroundResource(R.drawable.push_box)
                month.setBackgroundResource(R.drawable.fill_box)
                year.setBackgroundResource(R.drawable.fill_box)
                getList()
            }

            month.setOnClickListener {
                day.setBackgroundResource(R.drawable.fill_box)
                week.setBackgroundResource(R.drawable.fill_box)
                month.setBackgroundResource(R.drawable.push_box)
                year.setBackgroundResource(R.drawable.fill_box)
                findType = 3
                getList()
            }

            year.setOnClickListener {
                day.setBackgroundResource(R.drawable.fill_box)
                week.setBackgroundResource(R.drawable.fill_box)
                month.setBackgroundResource(R.drawable.fill_box)
                year.setBackgroundResource(R.drawable.push_box)
                findType = 4
                getList()
            }
        }
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
            array.clear()
            adapter.clearData()
            val usageStats: MutableList<UsageStats>
            usageStats = getAppUsageStats()
            showAppUsageStats(usageStats)
            array.distinct()
            adapter.addData(array)
        }
    }

    //패키지 명과 등등 앱 정보 가져오기
    private fun showAppUsageStats(usageStats: MutableList<UsageStats>) {
        usageStats.sortWith(Comparator { right, left ->
            compareValues(left.lastTimeUsed, right.lastTimeUsed)
        })

        usageStats.forEach {
            Log.e("ThreeO", "패키지명: ${it.packageName}, lastTimeUsed: ${Date(it.lastTimeUsed)}, " +
                    "totalTimeInForeground: ${it.totalTimeInForeground}")

            val icon: Drawable = this.packageManager.getApplicationIcon(it.packageName)
            val p: PackageInfo = this.packageManager.getPackageInfo(it.packageName, 0)
            val appname = p.applicationInfo.loadLabel(packageManager).toString()
            if(it.totalTimeInForeground.toString() != "0")
                array.add(TimeData(icon, appname, (it.totalTimeInForeground).toString()))
        }
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