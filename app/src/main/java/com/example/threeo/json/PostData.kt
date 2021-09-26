package com.example.threeo.json

data class PostData(
    val userId: String, //ANDROID_ID
    val period: Int, // 1일(1), 1주(2), 1달(3), 1년(4)
    val updatedTime: Long, //오늘 날짜
    val appList: ArrayList<AppData> //앱 리스트
)