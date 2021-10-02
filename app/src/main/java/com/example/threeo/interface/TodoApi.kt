package com.example.threeo.`interface`

import com.example.threeo.json.AveData
import com.example.threeo.json.PostData
import retrofit2.Call
import retrofit2.http.*

interface TodoApi {
    @POST("/timeToDo/userLog")
    fun postData(@Body requestBody: PostData): Call<String>

    @GET("/timeTodo/otherUserAverage/{userId}/{period}/{appName}")
    fun otherUseAverage(@Path("userId") userId:String, @Path("period") period:Int, @Path("appName") appName:String): Call<AveData> //시간 반환

    @GET("/api/timeToDo/myLastData/{userId}/{period}")
    fun myLastData(@Query("userId") userId:String, @Query("period") period:Int): Call<Long> //시간 반환


}