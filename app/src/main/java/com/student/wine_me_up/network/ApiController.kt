package com.student.wine_me_up.network

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.student.wine_me_up.utilities.BaseMethods.convertWineEntriesToWine
import com.student.wine_me_up.models.LatestWineScoreResponse
import com.student.wine_me_up.wine_repo.WineDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class ApiController {

    private val baseApiUrl = "https://api.globalwinescore.com/"
    private val token = "af61b2f3892e86a85931114852772cac6397e987"

    companion object{
        val isNetworkDone = MutableLiveData<Boolean>()
        val _isNetworkDone: LiveData<Boolean>
        get () = isNetworkDone
    }

    private fun networkClient(): Retrofit {
        val gson: Gson = GsonBuilder().setLenient().create()

        return Retrofit.Builder()
            .baseUrl(baseApiUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    }

    fun makeCall(context: Context) {
        val wineApi: INetworkManager = networkClient().create(INetworkManager::class.java)

        val call = wineApi.getLatest("Token $token")
        isNetworkDone.postValue(false)
        call.enqueue(object : Callback<LatestWineScoreResponse> {
            override fun onResponse(
                call: Call<LatestWineScoreResponse>,
                response: Response<LatestWineScoreResponse>
            ) {
                if (response.code() == 200) {
                    Toast.makeText(context, "Network Call Successful", Toast.LENGTH_LONG).show()
                    val data = response.body()?.results

                    GlobalScope.launch {
                        data?.let {
                            for (wine in it) {
                                WineDatabase.getInstance(context).wineDao()
                                    .saveWine(
                                        convertWineEntriesToWine(
                                            wine
                                        )
                                    )
                            }
                        }
                    }
                    Toast.makeText(context, "Saving to DB Successful", Toast.LENGTH_LONG).show()
                    isNetworkDone.postValue(true)
                }
            }

            override fun onFailure(call: Call<LatestWineScoreResponse>, t: Throwable) {
                Toast.makeText(context, "${t.message}", Toast.LENGTH_LONG).show()
                t.printStackTrace()
                // Unable to resolve host "api.globalwinescore.com"
            }
        })
    }
}
