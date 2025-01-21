package ro.pub.cs.systems.eim.practicaltest02v8

import android.util.Log
import ro.pub.cs.systems.eim.practicaltest02v8.BitcoinData
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class Presenter(private val view: BitcoinView) {

    private val client = OkHttpClient()
    private val gson = Gson()
    private var lastFetchTime: Long = 0
    private var cachedBitcoinData: BitcoinData? = null
    private val CACHE_DURATION = 5 * 1000

    interface BitcoinView {
        fun onBitcoinInfoSuccess(time: String, rate: String)
        fun onBitcoinInfoError(message: String)
    }

    fun getBitcoinInfo(currency: String) {
        val currentTime = System.currentTimeMillis()
        
        if (cachedBitcoinData != null && (currentTime - lastFetchTime) < CACHE_DURATION) {
            // Use cached data
            cachedBitcoinData?.let { data ->
                val time = data.time?.updated ?: ""
                val rate = data.bpi?.USD?.rate ?: ""
                CoroutineScope(Dispatchers.Main).launch {
                    view.onBitcoinInfoSuccess(time, rate)
                }
            }
            Log.d(Constants.TAG_RESULT, "Using cached data")
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val url = "http://api.coindesk.com/v1/bpi/currentprice/$currency.json"

            val request = Request.Builder()
                .url(url)
                .build()

            try {
                val response = client.newCall(request).execute()

                if (!response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        view.onBitcoinInfoError("Unexpected response: ${response.code}")
                    }
                    return@launch
                }

                val bitcoinData = gson.fromJson(response.body?.string(), BitcoinData::class.java)
                cachedBitcoinData = bitcoinData
                lastFetchTime = currentTime
                
                val time = bitcoinData.time?.updated ?: ""
                val rate = bitcoinData.bpi?.USD?.rate ?: ""
                
                withContext(Dispatchers.Main) {
                    view.onBitcoinInfoSuccess(time, rate)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    view.onBitcoinInfoError("Error: ${e.message}")
                }
            }
        }
    }
}