package com.example.linkpreiew

import android.os.Bundle
import android.util.Log
import android.view.View.OnTouchListener
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.kedia.ogparser.OpenGraphCacheProvider
import com.kedia.ogparser.OpenGraphCallback
import com.kedia.ogparser.OpenGraphParser
import com.kedia.ogparser.OpenGraphResult
import java.util.regex.Pattern


fun extractLink(inputString: String): String? {
    val pattern = Pattern.compile("\\b(https?|ftp)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*" +
            "[-a-zA-Z0-9+&@#/%=~_|]", Pattern.CASE_INSENSITIVE)
    val matcher = pattern.matcher(inputString)

    if (matcher.find()) {
        return matcher.group()
    }

    return null
}



class MainActivity : AppCompatActivity() , OpenGraphCallback{

    lateinit var tview:EditText

    lateinit var btn:Button
    lateinit var webview:WebView

    private val openGraphParser by lazy {
        OpenGraphParser(
            listener = this,
            showNullOnEmpty = true,
            cacheProvider = OpenGraphCacheProvider(this)
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tview=findViewById<EditText>(R.id.tview)

//        val html =
//            "<iframe width=\"560\" height=\"315\" src=\"https://developer.android.com\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"

        webview=findViewById(R.id.webview)
//        webview.loadData(html, "text/html", null);

        webview.setOnTouchListener(OnTouchListener { v, event -> true })
        webview.loadUrl("https://drive.google.com/file/d/1pu8VYtA2VxM3V6fP6R_Z3CngEuMv07ox");

        btn=findViewById(R.id.button)
        btn.setOnClickListener {
//            openGraphParser.parse(firstLink!!)
            val inputString = tview.text?.toString() ?: ""

            val firstLink = extractLink(inputString)

            if (firstLink != null) {
                println("First link found: $firstLink")

            } else {
                println("No link found.")
            }

            openGraphParser.parse(firstLink!!)
        }

    }

    override fun onError(error: String) {
        Log.e("TAG!!!!", "$error")

    }

    override fun onPostResponse(openGraphResult: OpenGraphResult) {

        Log.e("TAG!!!!", "response $openGraphResult")
            Glide.with(this).load(openGraphResult.image).into(findViewById(R.id.imgTest))


    }



}