package com.example.threeo.`interface`

import com.example.threeo.json.PostData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface TodoApi {
    @POST("/api/timeToDo/postData")
    fun postData(@Body requestBody: PostData): Call<String>

    @GET("/api/timeToDo/otherUserData/{appName}/{period}")
    fun otherUserData(@Query("appName") appName:String, @Query("period") period:Int): Call<Long> //시간 반환

    @GET("/api/timeToDo/myLastData/{userId}/{period}")
    fun myLastData(@Query("userId") userId:String, @Query("period") period:Int): Call<Long> //시간 반환


}