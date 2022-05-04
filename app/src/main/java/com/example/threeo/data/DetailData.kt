package com.example.threeo.data

import java.io.Serializable

//상세정보 클래스
data class DetailData(
    var realTime: Long,
    var customTime: Int,
    var appName: String,
    var packageName: String,
    var dateType: Boolean,
    var standardType: Int): Serializable {
}