package com.example.threeo

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
import com.example.threeo.adapter.AppListAdapter
import com.example.threeo.data.TimeData
import com.example.threeo.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    lateinit var adapter: AppListAdapter
    var array = ArrayList<TimeData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init(){
        binding.apply {
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
//                    intent.putExtra("appIcon", adapter.items[position].img)
                    startActivity(intent)
                }
            }
            recyclerView.adapter = adapter

            button.setOnClickListener { view ->
                if(!checkForPermission()) {
                    Toast.makeText(
                        this@MainActivity, "권한 설정이 필요합니다.",
                        Toast.LENGTH_LONG
                    ).show()
                    startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
                }else{
                    adapter.clearData()
                    array.clear()
                    val usageStats = getAppUsageStats()
                    showAppUsageStats(usageStats)
                    adapter.addData(array)
                }
            }
        }
    }

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
            array.add(TimeData(icon, appname, it.totalTimeInForeground.toString()))
        }
    }

    private fun getAppUsageStats(): MutableList<UsageStats> {
        val cal = Calendar.getInstance()
        cal.add(Calendar.YEAR, -1)

        val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val queryUsageStats = usageStatsManager
            .queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY, cal.timeInMillis,
                System.currentTimeMillis()
            )
        return queryUsageStats
    }

    private fun checkForPermission(): Boolean {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), packageName)
        return mode == AppOpsManager.MODE_ALLOWED
    }
}