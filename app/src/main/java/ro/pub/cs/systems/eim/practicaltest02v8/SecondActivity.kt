package ro.pub.cs.systems.eim.practicaltest02v8

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.EditText
import android.widget.Button
import android.app.Activity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class SecondActivity : Activity() {

    private val client = OkHttpClient()
    private lateinit var resultTextView: TextView
    private lateinit var number1EditText: EditText
    private lateinit var number2EditText: EditText
    private lateinit var addButton: Button
    private lateinit var subtractButton: Button
    private lateinit var multiplyButton: Button
    private lateinit var divideButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        
        resultTextView = findViewById(R.id.result_textview)
        number1EditText = findViewById(R.id.number1_edittext)
        number2EditText = findViewById(R.id.number2_edittext)
        addButton = findViewById(R.id.add_button)
        subtractButton = findViewById(R.id.subtract_button)
        multiplyButton = findViewById(R.id.multiply_button)
        divideButton = findViewById(R.id.divide_button)

        addButton.setOnClickListener { calculate("plus") }
        subtractButton.setOnClickListener { calculate("minus") }
        multiplyButton.setOnClickListener { calculate("times") }
        divideButton.setOnClickListener { calculate("divide") }
    }

    private fun calculate(operation: String) {
        val t1 = number1EditText.text.toString()
        val t2 = number2EditText.text.toString()
        
        if (t1.isBlank() || t2.isBlank()) {
            resultTextView.text = "Please enter both numbers"
            return
        }

        getDataFromServer(operation, t1, t2)
    }

    private fun getDataFromServer(operation: String, t1: String, t2: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val request = Request.Builder()
                .url("http://10.0.2.2:8080?operation=$operation&t1=$t1&t2=$t2")
                .build()

            try {
                val response = client.newCall(request).execute()
                val responseData = response.body?.string()

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && responseData != null) {
                        resultTextView.text = responseData
                        Log.d(Constants.TAG_RESULT, "Server response: $responseData")
                    } else {
                        resultTextView.text = "Error: ${response.code}"
                        Log.e(Constants.TAG_RESULT, "Server error: ${response.code}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    resultTextView.text = "Error: ${e.message}"
                    Log.e(Constants.TAG_RESULT, "Connection error: ${e.message}")
                }
            }
        }
    }
}