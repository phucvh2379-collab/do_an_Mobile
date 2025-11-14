package com.example.carbontracker.data

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

// Data class for API response
data class SaveUserDataResponse(
    val success: Boolean,
    val insertId: Int? = null,
    val message: String? = null
)

// Retrofit API interface
interface ApiService {
    @POST("save_user_data.php")
    suspend fun saveUserData(@Body userData: UserData): Response<SaveUserDataResponse>
}

// Retrofit client
object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2/severtest/" // Use 10.0.2.2 for emulator, adjust for real device

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

// DBHelper for saving user data
class DBHelper {
    companion object {
        private const val TAG = "DBHelper"
    }

    fun saveUserData(onResult: (Boolean, String?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userData = UserDataState.toUserData()
                Log.d(TAG, "Sending JSON: ${com.google.gson.Gson().toJson(userData)}")
                val response = RetrofitClient.apiService.saveUserData(userData)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result?.success == true) {
                            Log.d(TAG, "Data saved successfully, insertId: ${result.insertId}")
                            onResult(true, null)
                        } else {
                            Log.e(TAG, "API error: ${result?.message}")
                            onResult(false, result?.message ?: "Unknown error")
                        }
                    } else {
                        Log.e(TAG, "HTTP error: ${response.code()} - ${response.message()}")
                        onResult(false, "HTTP error: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error saving user data", e)
                withContext(Dispatchers.Main) {
                    onResult(false, e.message)
                }
            }
        }
    }
}