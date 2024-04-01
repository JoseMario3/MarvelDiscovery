package com.example.marveldiscovery

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONArray
import org.w3c.dom.Text
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    //var resultsArray: JSONArray? = null

    private lateinit var marvelImages: MutableList<String>
    private lateinit var marvelNames: MutableList<String>
    private lateinit var marvelInfo: MutableList<String>
    private lateinit var rvMarvel: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        marvelImages = mutableListOf()
        marvelNames = mutableListOf()
        marvelInfo = mutableListOf()
        rvMarvel = findViewById(R.id.marvel_list)
//        val name = findViewById<TextView>(R.id.name)
//        val pic = findViewById<ImageView>(R.id.image)
//        val desc = findViewById<TextView>(R.id.description)
//        val button = findViewById<Button>(R.id.button)
        getCharacters()
        //getNextCharacter(button, pic, name, desc)
    }

    private fun getCharacters() {
        val client = AsyncHttpClient()
        val timestamp = System.currentTimeMillis()
        val publicKey = "dd3e7d9e4be1dbd16195aaad7d9f0b41"
        val privateKey = "f240c2909f1a1a2e8f0141ec2f3b09206a758048"
        val hash = stringToMd5("$timestamp$privateKey$publicKey")

        val url =
            "https://gateway.marvel.com/v1/public/characters?ts=$timestamp&apikey=$publicKey&hash=$hash"
        client[url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("Marvel", "response successful $json")
                val resArray = json.jsonObject.optJSONObject("data")?.getJSONArray("results")

                if (resArray != null) {
                    for (i in 0 until resArray.length()) {
                        val character = resArray.getJSONObject(i)
                        val thumbnail = character.getJSONObject("thumbnail")
                        val path = thumbnail.getString("path").substring(7)
                        val extension = thumbnail.getString("extension")
                        val imageUrl = "https://$path.$extension"
                        val charName = character?.getString("name")
                        val charDesc = character?.getString("description")
                        marvelImages.add(imageUrl)
                        if (charName != null) marvelNames.add(charName)
                        if (charDesc != null) marvelInfo.add(charDesc)
                    }

                    val adapter = MarvelAdapter(marvelNames, marvelImages, marvelInfo)
                    rvMarvel.adapter = adapter
                    rvMarvel.layoutManager = LinearLayoutManager(this@MainActivity)
                    rvMarvel.addItemDecoration(DividerItemDecoration(this@MainActivity, LinearLayoutManager.VERTICAL))
                }

            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Marvelous Error", errorResponse)
            }
        }]
    }

    private fun stringToMd5(s: String): Any {
        val md = MessageDigest.getInstance("MD5")
        val bigInt = BigInteger(1, md.digest(s.toByteArray(Charsets.UTF_8)))
        return String.format("%032x", bigInt)
    }
}