package com.example.threeo.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.threeo.R
import com.example.threeo.`interface`.TodoApi
import com.example.threeo.adapter.IfListAdapter
import com.example.threeo.data.DetailData
import com.example.threeo.databinding.ActivityDetailBinding
import com.example.threeo.json.AveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*


class DetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailBinding

    var allTime: String = ""
    var appName: String = ""
    var packageStr: String = ""
    var idByANDROID_ID: String = ""
    var findType: Int = -1

    lateinit var icon: Drawable

    lateinit var adapter: IfListAdapter
    var array = ArrayList<DetailData>()

    private val requiredPermissions = arrayOf(
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        allTime = intent.getStringExtra("totalTime").toString()
//        appName = intent.getStringExtra("appName").toString()
//        packageStr = intent.getStringExtra("packageName").toString()
//        idByANDROID_ID = intent.getStringExtra("idByANDROID_ID").toString()
//        findType = intent.getIntExtra("findType", -1)


        //다른 사용자들 평균
        //Retrofit 객체 생성
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://wouldhavedone-back.herokuapp.com")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()

        //retrofit 객체를 통해 인터페이스 생성
//        val service = retrofit.create(TodoApi::class.java)
//        lateinit var callOtherUseAverage:Call<AveData>
//        if(packageStr != "전체시간")
//            callOtherUseAverage = service.otherUseAverage(idByANDROID_ID, findType, appName)
//        else
//            callOtherUseAverage = service.otherUseAverage(idByANDROID_ID, findType, packageStr)
//
//        callOtherUseAverage.enqueue(object: Callback<AveData>{
//            override fun onResponse(call: Call<AveData>, response: Response<AveData>) {
//                if(response.isSuccessful){
//                    Log.e("otherUseAverage", "성공 ${response.body()!!.result}")
//                    Log.e("otherUseAverage", "성공 ${response}")
//                    val allTime = response.body()!!.result
//                    val hour = allTime/3600000
//                    val min = allTime/60000 - hour*60
//                    Log.e("값 확인", "${hour}시간 ${min}분")
//                    binding.otherAverage.text = "${hour}시간 ${min}분"
//
//                }else{
//                    Log.e("otherUseAverage", "code == 400")
//                }
//            }
//
//            override fun onFailure(call: Call<AveData>, t: Throwable) {
//                Log.e("otherUseAverage", "실패 : $t")
//            }
//        })


//        if(packageStr != "전체시간")
//            icon = this.packageManager.getApplicationIcon(packageStr)
//        else
//            icon = this.packageManager.getApplicationIcon(packageName)
//        init()
//        checkPermission()
//    }
//
//    private fun openScreenshot(imageFile: File) {
//        val intent = Intent()
//        intent.action = Intent.ACTION_VIEW
//        val uri: Uri = Uri.fromFile(imageFile)
//        intent.setDataAndType(uri, "image/*")
//        startActivity(intent)
//    }
//
//    private fun checkPermission() {
//        //거절되었거나 아직 수락하지 않은 권한(퍼미션)을 저장할 문자열 배열 리스트
//        var rejectedPermissionList = ArrayList<String>()
//
//        //필요한 퍼미션들을 하나씩 끄집어내서 현재 권한을 받았는지 체크
//        for(permission in requiredPermissions){
//            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
//                //만약 권한이 없다면 rejectedPermissionList에 추가
//                rejectedPermissionList.add(permission)
//            }
//        }
//        //거절된 퍼미션이 있다면...
//        if(rejectedPermissionList.isNotEmpty()){
//            //권한 요청!
//            val array = arrayOfNulls<String>(rejectedPermissionList.size)
//            ActivityCompat.requestPermissions(this, rejectedPermissionList.toArray(array), 1111)
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            1111 -> {
//                if(grantResults.isNotEmpty()) {
//                    for((i, permission) in permissions.withIndex()) {
//                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
//                            //권한 획득 실패
//                            Toast.makeText(this, "$permission 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show()
//                            return
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    fun getScreenShotFromView(v: View): Bitmap? {
//        var screenshot: Bitmap? = null
//        try {
//            if (v != null) {
//                screenshot = Bitmap.createBitmap(v.measuredWidth, v.measuredHeight, Bitmap.Config.ARGB_8888)
//                val canvas = Canvas(screenshot)
//                v.draw(canvas)
//            }
//        } catch (e: Exception) {
//            Log.e("BLABLA", "Failed to capture screenshot because:" + e.message)
//        }
//        return screenshot
//    }
//
//    fun init(){
//        binding.apply {
//            if(packageStr != "전체시간")
//                iconImg.setImageDrawable(icon)
//            else
//                iconImg.setImageResource(R.drawable.all_time)
//            appTitle.text = appName
//
//            val hour = allTime.toLong()/3600000
//            val min = allTime.toLong()/60000 - hour*60
//            val totalMin = (hour*60 + min);
//            totalTime.text = "${hour}시간 ${min}분"
//
//            //버튼 이벤트
//            backBtn.setOnClickListener {
//                onBackPressed()
//            }
//
//            recyclerView2.layoutManager = LinearLayoutManager(this@DetailActivity, LinearLayoutManager.HORIZONTAL,false)
//            adapter = IfListAdapter(array)
//
//            adapter.itemClickListener = object : IfListAdapter.OnItemClickListener{
//                override fun OnItemClick(
//                    holder: IfListAdapter.MyViewHolder,
//                    view: View,
//                    data: DetailData,
//                    position: Int
//                ) {
//                    adapter.settingPushBtn(position)
//
//                    when(adapter.items[position].img){
//                        R.drawable.book ->{
//                            resultVal.text = "${(hour / 6).toInt()} 권 읽었다!"
//                            flag.text = "한 권당 6시간 기준"
//                        }
//                        R.drawable.run ->{
//                            resultVal.text = "${(totalMin * 9.6).toInt()} Kcal 소모했다!"
//                            flag.text = "달리기 1분 9.6kcal 기준"
//                        }
//                        R.drawable.butterfly ->{
//                            resultVal.text = "날개짓 ${(totalMin / 500).toInt()}번 했다!"
//                            flag.text = "은줄팔랑나비 1분 500회 기준"
//                        }
//                        R.drawable.music ->{
//                            resultVal.text = "${(totalMin / 3).toInt()} 곡 들었다!"
//                            flag.text = "1곡 3분 기준"
//                        }
//                        R.drawable.money ->{
//                            resultVal.text = "${(hour * 8720).toInt()} 원 벌었다!"
//                            flag.text = "시급 8720원 기준"
//                        }
//                        R.drawable.turtle ->{
//                            resultVal.text = "${(hour / 35).toInt()} m 헤엄쳤다!"
//                            flag.text = "바다거북 35km/h 기준"
//                        }
//                        R.drawable.movie ->{
//                            resultVal.text = "${(hour / 2).toInt()} 편 봤다!"
//                            flag.text = "영화 한 편 2시간 기준"
//                        }
//                        R.drawable.english ->{
//                            resultVal.text = "${(totalMin / 1.3).toInt()} 개 외웠다!"
//                            flag.text = "단어 1개 1분20초 기준"
//                        }
//                        R.drawable.fog ->{
//                            resultVal.text = "탄소 ${(totalMin * 3.6).toInt()} g 배출했다!"
//                            flag.text = "인터넷 접속 시 1분 3.6g 기준 "
//                        }
//                        R.drawable.rocket ->{
//                            resultVal.text = "ISS까지 ${(totalMin / 30).toInt()} 번 다녀왔다!"
//                            flag.text = "왕복 30분 기준"
//                        }
//                        R.drawable.teacher ->{
//                            resultVal.text = "2학점짜리 ${(hour / 2).toInt()} 주차 들었다!"
//                            flag.text = "전공 2학점 1주 2시간 기준"
//                        }
//                        R.drawable.otter ->{
//                            resultVal.text = "조개 ${(totalMin * 0.5).toInt()}개 까먹었다!"
//                            flag.text = "해달 1분 0.5개 섭취 기준"
//                        }
//                    }
//                }
//            }
//            recyclerView2.adapter = adapter
//        }
//    }
    }
}
