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

    var allTime:String = ""
    var appName:String = ""
    var packageStr:String = ""
    var idByANDROID_ID:String = ""
    var findType:Int = -1

    lateinit var icon:Drawable

    lateinit var adapter: IfListAdapter
    var array = ArrayList<DetailData>()

    private val requiredPermissions = arrayOf(
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        allTime = intent.getStringExtra("totalTime").toString()
        appName = intent.getStringExtra("appName").toString()
        packageStr = intent.getStringExtra("packageName").toString()
        idByANDROID_ID = intent.getStringExtra("idByANDROID_ID").toString()
        findType = intent.getIntExtra("findType", -1)


        //?????? ???????????? ??????
        //Retrofit ?????? ??????
        val retrofit = Retrofit.Builder()
            .baseUrl("https://wouldhavedone-back.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //retrofit ????????? ?????? ??????????????? ??????
        val service = retrofit.create(TodoApi::class.java)
        lateinit var callOtherUseAverage:Call<AveData>
        if(packageStr != "????????????")
            callOtherUseAverage = service.otherUseAverage(idByANDROID_ID, findType, appName)
        else
            callOtherUseAverage = service.otherUseAverage(idByANDROID_ID, findType, packageStr)

        callOtherUseAverage.enqueue(object: Callback<AveData>{
            override fun onResponse(call: Call<AveData>, response: Response<AveData>) {
                if(response.isSuccessful){
                    Log.e("otherUseAverage", "?????? ${response.body()!!.result}")
                    Log.e("otherUseAverage", "?????? ${response}")
                    val allTime = response.body()!!.result
                    val hour = allTime/3600000
                    val min = allTime/60000 - hour*60
                    Log.e("??? ??????", "${hour}?????? ${min}???")
                    binding.otherAverage.text = "${hour}?????? ${min}???"

                }else{
                    Log.e("otherUseAverage", "code == 400")
                }
            }

            override fun onFailure(call: Call<AveData>, t: Throwable) {
                Log.e("otherUseAverage", "?????? : $t")
            }
        })


        if(packageStr != "????????????")
            icon = this.packageManager.getApplicationIcon(packageStr)
        else
            icon = this.packageManager.getApplicationIcon(packageName)
        init()
        checkPermission()
    }

    private fun openScreenshot(imageFile: File) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        val uri: Uri = Uri.fromFile(imageFile)
        intent.setDataAndType(uri, "image/*")
        startActivity(intent)
    }

    private fun checkPermission() {
        //?????????????????? ?????? ???????????? ?????? ??????(?????????)??? ????????? ????????? ?????? ?????????
        var rejectedPermissionList = ArrayList<String>()

        //????????? ??????????????? ????????? ??????????????? ?????? ????????? ???????????? ??????
        for(permission in requiredPermissions){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                //?????? ????????? ????????? rejectedPermissionList??? ??????
                rejectedPermissionList.add(permission)
            }
        }
        //????????? ???????????? ?????????...
        if(rejectedPermissionList.isNotEmpty()){
            //?????? ??????!
            val array = arrayOfNulls<String>(rejectedPermissionList.size)
            ActivityCompat.requestPermissions(this, rejectedPermissionList.toArray(array), 1111)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1111 -> {
                if(grantResults.isNotEmpty()) {
                    for((i, permission) in permissions.withIndex()) {
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            //?????? ?????? ??????
                            Toast.makeText(this, "$permission ????????? ????????? ????????? ?????????.", Toast.LENGTH_SHORT).show()
                            return
                        }
                    }
                }
            }
        }
    }

    fun getScreenShotFromView(v: View): Bitmap? {
        var screenshot: Bitmap? = null
        try {
            if (v != null) {
                screenshot = Bitmap.createBitmap(v.measuredWidth, v.measuredHeight, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(screenshot)
                v.draw(canvas)
            }
        } catch (e: Exception) {
            Log.e("BLABLA", "Failed to capture screenshot because:" + e.message)
        }
        return screenshot
    }

    fun init(){
        binding.apply {
            if(packageStr != "????????????")
                iconImg.setImageDrawable(icon)
            else
                iconImg.setImageResource(R.drawable.all_time)
            appTitle.text = appName

            val hour = allTime.toLong()/3600000
            val min = allTime.toLong()/60000 - hour*60
            val totalMin = (hour*60 + min);
            totalTime.text = "${hour}?????? ${min}???"

            //?????? ?????????
            backBtn.setOnClickListener {
                onBackPressed()
            }

            shareBtn.setOnClickListener {
                binding.detailLayout.post(Runnable {
                    val myBitmap = getScreenShotFromView(binding.detailLayout)
                    try {
                        if (myBitmap != null) {
                            // ???????????? ???????????? ??????
                            val now = Date()
                            DateFormat.format("yyyy-MM-dd_hh:mm:ss", now)
                            val fileName = "screenshot$now.png"
                            MediaStore.Images.Media.insertImage(contentResolver, myBitmap, now.toString(), fileName)

                            //????????? ??????
                            val bytes = ByteArrayOutputStream()
                            myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                            val path: String = MediaStore.Images.Media.insertImage(
                                contentResolver,
                                myBitmap,
                                "Title",
                                null
                            )
                            val shareUri = Uri.parse(path)

                            val sharingIntent = Intent(Intent.ACTION_SEND)
                            sharingIntent.setType("image/*")
                            sharingIntent.putExtra(Intent.EXTRA_STREAM, shareUri)
                            startActivity(Intent.createChooser(sharingIntent, "Share image"))
                        }
                    } catch (e: java.lang.Exception) {
                        Log.e("BLABLA", "Error ::" + e.message)
                    }
                })
            }

            //list??? ???????????? ????????? ??????
            array.add(DetailData(R.drawable.book, true))
            array.add(DetailData(R.drawable.run))
            array.add(DetailData(R.drawable.butterfly))
            array.add(DetailData(R.drawable.music))
            array.add(DetailData(R.drawable.money))
            array.add(DetailData(R.drawable.turtle))
            array.add(DetailData(R.drawable.movie))
            array.add(DetailData(R.drawable.english))
            array.add(DetailData(R.drawable.fog))
            array.add(DetailData(R.drawable.rocket))
            array.add(DetailData(R.drawable.teacher))
            array.add(DetailData(R.drawable.otter))


            recyclerView2.layoutManager = LinearLayoutManager(this@DetailActivity, LinearLayoutManager.HORIZONTAL,false)
            adapter = IfListAdapter(array)

            adapter.itemClickListener = object : IfListAdapter.OnItemClickListener{
                override fun OnItemClick(
                    holder: IfListAdapter.MyViewHolder,
                    view: View,
                    data: DetailData,
                    position: Int
                ) {
                    adapter.settingPushBtn(position)

                    when(adapter.items[position].img){
                        R.drawable.book ->{
                            resultVal.text = "${(hour / 6).toInt()} ??? ?????????!"
                            flag.text = "??? ?????? 6?????? ??????"
                        }
                        R.drawable.run ->{
                            resultVal.text = "${(totalMin * 9.6).toInt()} Kcal ????????????!"
                            flag.text = "????????? 1??? 9.6kcal ??????"
                        }
                        R.drawable.butterfly ->{
                            resultVal.text = "????????? ${(totalMin / 500).toInt()}??? ??????!"
                            flag.text = "?????????????????? 1??? 500??? ??????"
                        }
                        R.drawable.music ->{
                            resultVal.text = "${(totalMin / 3).toInt()} ??? ?????????!"
                            flag.text = "1??? 3??? ??????"
                        }
                        R.drawable.money ->{
                            resultVal.text = "${(hour * 8720).toInt()} ??? ?????????!"
                            flag.text = "?????? 8720??? ??????"
                        }
                        R.drawable.turtle ->{
                            resultVal.text = "${(hour / 35).toInt()} m ????????????!"
                            flag.text = "???????????? 35km/h ??????"
                        }
                        R.drawable.movie ->{
                            resultVal.text = "${(hour / 2).toInt()} ??? ??????!"
                            flag.text = "?????? ??? ??? 2?????? ??????"
                        }
                        R.drawable.english ->{
                            resultVal.text = "${(totalMin / 1.3).toInt()} ??? ?????????!"
                            flag.text = "?????? 1??? 1???20??? ??????"
                        }
                        R.drawable.fog ->{
                            resultVal.text = "?????? ${(totalMin * 3.6).toInt()} g ????????????!"
                            flag.text = "????????? ?????? ??? 1??? 3.6g ?????? "
                        }
                        R.drawable.rocket ->{
                            resultVal.text = "ISS?????? ${(totalMin / 30).toInt()} ??? ????????????!"
                            flag.text = "?????? 30??? ??????"
                        }
                        R.drawable.teacher ->{
                            resultVal.text = "2???????????? ${(hour / 2).toInt()} ?????? ?????????!"
                            flag.text = "?????? 2?????? 1??? 2?????? ??????"
                        }
                        R.drawable.otter ->{
                            resultVal.text = "?????? ${(totalMin * 0.5).toInt()}??? ????????????!"
                            flag.text = "?????? 1??? 0.5??? ?????? ??????"
                        }
                    }
                }
            }
            recyclerView2.adapter = adapter
        }
    }
}
