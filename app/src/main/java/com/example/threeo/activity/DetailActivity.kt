package com.example.threeo.activity

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.threeo.R
import com.example.threeo.data.DetailData
import com.example.threeo.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailBinding

    lateinit var detailData: DetailData
    lateinit var icon: Drawable

    private val ifIcon = arrayListOf<Int>(
        R.drawable.book,
        R.drawable.run,
        R.drawable.butterfly,
        R.drawable.music,
        R.drawable.money,
        R.drawable.turtle,
        R.drawable.movie,
        R.drawable.english,
        R.drawable.fog,
        R.drawable.teacher,
        R.drawable.otter
    )

    private val standardTextList = arrayOf(
        "한 권당 6시간 기준",
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

    private val customTextList = arrayOf(
        "권 읽었다!",
        "Kcal 소모했다!",
        "번 했다!",
        "곡 들었다!",
        "원 벌었다!",
        "m 헤엄쳤다!",
        "편 봤다!",
        "개 외웠다!",
        "g 배출했다!",
        "번 다녀왔다!",
        "주차 들었다!",
        "개 까먹었다!",
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailData = intent.getSerializableExtra("detailData") as DetailData


        init()
    }

    fun init() {
        if (detailData.appName != "전체시간")
            icon = this.packageManager.getApplicationIcon(packageName)

        binding.apply {
            //앱 아이콘
            if (detailData.appName != "전체시간")
                iconImg.setImageDrawable(icon)
            else
                iconImg.setImageResource(R.drawable.all_time)

            //앱 이름
            appTitle.text = detailData.appName

            //타입 아이콘
            standardTypeIcon.setImageResource(ifIcon[detailData.standardType])

            //버튼 이벤트
            backBtn.setOnClickListener {
                onBackPressed()
            }

            //기준
            standardText.text = standardTextList[detailData.standardType]

            //커스텀 시간
            customTime.text = detailData.customTime.toString()

            //커스텀 단위
            customText.text = customTextList[detailData.standardType]
        }
    }
}
