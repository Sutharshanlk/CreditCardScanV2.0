package com.example.administrator.creditcardscanv20

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import cards.pay.paycardsrecognizer.sdk.Card
import cards.pay.paycardsrecognizer.sdk.ScanCardIntent
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : BaseActivity() {
    //Credit Card Scan Variables *****************

    var REQUEST_CODE_SCAN_CARD: Int = 1

    val TAG = "CCScan"

    var ccYearAdapter: ArrayAdapter<String>? = null
    var ccMonthAdapter: ArrayAdapter<String>? = null
    var ccTypeAdapter: ArrayAdapter<String>? = null


    private val resultData: String? = null
    var mContext: Context? = null

    var ccMonth = SimpleDateFormat("MMMM")
    var ccYear = SimpleDateFormat("yyyy")
    var spccMonth: Spinner? = null
    var spccYear: Spinner? = null
    var spccType: Spinner? = null
    private var edccHolder: EditText? = null

    private var edccNumber: EditText? = null
    private var edccAmont: EditText? = null
    private var edccReferenceNumber: EditText? = null
    private var btnSave: Button? = null
    private var mCardCameraScan: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mContext = this@MainActivity;

        init()
        loadMonth()
        loadYear()
        loadCardType()

        mCardCameraScan?.setOnClickListener {  scanCard() }
    }

    fun init() {
        spccMonth = findViewById(R.id.spMonthCC) as Spinner;
        spccYear = findViewById(R.id.spYearCC) as Spinner
        spccType = findViewById(R.id.spTypeCC) as Spinner
        edccAmont = findViewById(R.id.edAmount) as EditText
        edccHolder = findViewById(R.id.edCardHolder) as EditText
        edccNumber = findViewById(R.id.edCardNumber) as EditText
        edccReferenceNumber = findViewById(R.id.edPassportNo) as EditText
        btnSave = findViewById(R.id.btnSubmitCard) as Button

        mCardCameraScan = findViewById(R.id.cardCameraScan) as TextView
    }

    private fun loadMonth() {
        val ccmonths: MutableList<String> = ArrayList()
        ccmonths.add(0, "Choose Month")
        for (i in -0..11) {
            val c = Calendar.getInstance()
            c.add(Calendar.MONTH, i)
            ccmonths.add(ccMonth.format(c.time))

        }
        ccMonthAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ccmonths)
        ccMonthAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spccMonth!!.adapter = ccMonthAdapter
    }

    private fun loadYear() {
        val ccyears: MutableList<String> = ArrayList()
        ccyears.add(0, "Choose year")
        for (i in -0..9) {
            val c = Calendar.getInstance()
            c.add(Calendar.YEAR, i)
            ccyears.add(ccYear.format(c.time))
        }

        ccYearAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ccyears)
        ccYearAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spccYear!!.adapter = ccYearAdapter
    }

    private fun loadCardType() {
        val cctype: MutableList<String> = ArrayList()
        cctype.add("Choose Card Type")
        cctype.add("VISA")
        cctype.add("MASTER")
        cctype.add("AMEX")
        cctype.add("UNION PAY")

        ccTypeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cctype)
        ccTypeAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spccType!!.adapter = ccTypeAdapter
    }

    private fun scanCard() {
        val intent = ScanCardIntent.Builder(this).build()
        startActivityForResult(intent, REQUEST_CODE_SCAN_CARD)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var clsValid=clsValidations()

        if (requestCode == REQUEST_CODE_SCAN_CARD) {
            if (resultCode == Activity.RESULT_OK) {
                val card: Card? = data!!.getParcelableExtra(ScanCardIntent.RESULT_PAYCARDS_CARD)
                val resultStr = card!!.cardNumber
                val cardData = """
                    Card number: ${card!!.cardNumberRedacted}
                    Card holder: ${card!!.cardHolderName}
                    Card expiration date: ${card!!.expirationDate}
                    """.trimIndent()
                Log.i(TAG, "Card info: $cardData")
                edccNumber!!.setText(card!!.cardNumber)
                edccHolder!!.setText(card!!.cardHolderName)
                if (edccNumber!!.length() != 0) {
                    val cType: String? =clsValid.cardType(resultStr)
                    spccType!!.setSelection(ccTypeAdapter!!.getPosition(cType))
                }
                if (card!!.expirationDate != null) {
                    val ExparyData =
                        card!!.expirationDate!!.split("/".toRegex()).toTypedArray()
                    val eMonth = ExparyData[0].toInt()

                    val cMonth: String? =clsValid.monthDigit(eMonth)
                    Log.i(TAG, "Card Month: $cMonth")
                    spccMonth!!.setSelection(ccMonthAdapter!!.getPosition(cMonth))
                    val cYear = "20" + ExparyData[1]
                    spccYear!!.setSelection(ccYearAdapter!!.getPosition(cYear))
                    Log.i(TAG, "Card Year: $cYear")
                    Log.i(TAG, "Month of Card: " + ExparyData[0])
                    Log.i(TAG, "Year Of Card: " + ExparyData[1])
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "Scan canceled")
            } else {
                Log.i(TAG, "Scan failed")
            }
        }
    }


}