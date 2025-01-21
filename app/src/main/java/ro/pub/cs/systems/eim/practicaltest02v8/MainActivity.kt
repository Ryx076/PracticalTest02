package ro.pub.cs.systems.eim.practicaltest02v8

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.app.Activity
import android.content.Intent
import ro.pub.cs.systems.eim.practicaltest02v8.R
import ro.pub.cs.systems.eim.practicaltest02v8.BitcoinData
import ro.pub.cs.systems.eim.practicaltest02v8.Presenter

class MainActivity : Activity(), Presenter.BitcoinView {

    private lateinit var getButton: Button
    private lateinit var nextActivityButton: Button
    private lateinit var currencyEditText: EditText
    private lateinit var resultTextView: TextView
    private lateinit var presenter: Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practical_test02v8_main)
        
        getButton = findViewById(R.id.get_button)
        nextActivityButton = findViewById(R.id.navigate_button)
        currencyEditText = findViewById(R.id.currency_edittext)
        resultTextView = findViewById(R.id.result_textview)
        presenter = Presenter(this)

        getButton.setOnClickListener {
            val currency = currencyEditText.text.toString()
            presenter.getBitcoinInfo(currency)
        }

        nextActivityButton.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBitcoinInfoSuccess(time: String, rate: String) {
        resultTextView.text = "Time: $time\nRate: $rate"
        Log.d(Constants.TAG_RESULT, "Time: $time, Rate: $rate")
    }

    override fun onBitcoinInfoError(message: String) {
        resultTextView.text = "Error: $message"
        Log.e(Constants.TAG_RESULT, message)
    }
}