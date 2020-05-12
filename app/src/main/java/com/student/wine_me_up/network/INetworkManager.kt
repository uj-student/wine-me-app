package com.student.wine_me_up.network

import com.student.wine_me_up.models.LatestWineScoreResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface INetworkManager {

    @GET("globalwinescores/latest/")
    @Headers("Content-Type: application/json")
    fun getLatest(
        @Header("Authorization") token: String,
        @Query("limit") limit: Int = 500,
        @Query("offset") offset: Int = 500
    ): Call<LatestWineScoreResponse>
}