package com.nicolas.hor_scopo.activities

import android.content.Intent
import android.content.pm.PackageInstaller
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nicolas.hor_scopo.data.Horoscope
import com.nicolas.hor_scopo.R
import com.nicolas.hor_scopo.utils.SessionManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.w3c.dom.Text
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class DetailActivity : AppCompatActivity() {

    lateinit var session: SessionManager

    lateinit var horoscope: Horoscope
    var isFavorite = false
    lateinit var favoriteMenuItem: MenuItem

    lateinit var signImageView: ImageView
    lateinit var nameTextView: TextView
    lateinit var datesTextView: TextView
    lateinit var predictionTextView: TextView
    lateinit var bottomNavigationView: MenuItem



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        session = SessionManager(this)

        signImageView = findViewById(R.id.signImageView)
        nameTextView = findViewById(R.id.nameTextView)
        datesTextView = findViewById(R.id.datesTextView)
        predictionTextView = findViewById(R.id.predictionTextView)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        val id = intent.getStringExtra("HOROSCOPE_ID")!!

        horoscope = Horoscope.getByID(id)

        supportActionBar?.setTitle(horoscope.name)
        supportActionBar?.setSubtitle(horoscope.dates)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        nameTextView.setText(horoscope.name)
        datesTextView.setText(horoscope.dates)
        signImageView.setImageResource(horoscope.sign)

        //Preguntar si el horosocopo es favorito
        isFavorite = session.isFavorite(id)

        getHoroscopePrediction()

        CoroutineScope(Dispatchers.IO).launch {
            val prediction = getHoroscopePrediction()
            CoroutineScope(Dispatchers.Main).launch {
             predictionTextView.text = prediction
            }
        }
        bottomNavigationView.setOnMenuItemClickListener { item ->
            when(item.itemId){
                R.id.menu_daily -> {

                }
            }
        }

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_detail_menu, menu)
        //cambiar icono del menu
        favoriteMenuItem = menu.findItem(R.id.menu_favorite)
        setFavoriteIcon()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection.
        return when (item.itemId) {

            android.R.id.home -> {
                finish()
                true
            }

            R.id.menu_favorite -> {
                //Se pregunta si el horoscopo es favorito o no para guardarlo en sesion o eliminarlo
                if (isFavorite){
                    session.setFavorite("")
                } else {
                    session.setFavorite(horoscope.id)
                }
                isFavorite = !isFavorite
                invalidateOptionsMenu()
                //cambiar el icono del menu
                true
            }

            R.id.menu_share -> {
                //Menu comparti
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my horoscope: ${getString(horoscope.name)}")
                sendIntent.type = "text/plain"

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    fun setFavoriteIcon () {
        if (isFavorite){
         //le asigno corazon relleno
            favoriteMenuItem.setIcon(R.drawable.ic_favorite_select_24px)
        } else {
            //le asigno corazon vacio
            favoriteMenuItem.setIcon(R.drawable.ic_favorite_24px)
        }
    }

    fun getHoroscopePrediction(period: String = "") {
        CoroutineScope(Dispatchers.IO).launch {

        val urlGetRequest = URL("https://freehoroscopeapi.com/api/v1/get-horoscope/$period?sign=${horoscope.id}")

        val apiConnection = urlGetRequest.openConnection() as HttpURLConnection

        apiConnection.setRequestMethod("GET")

        try {
            val responseCode = apiConnection.getResponseCode()

            if (responseCode == HttpURLConnection.HTTP_OK){
                val response = readImputStream(apiConnection.getInputStream())
                Log.i("API REST", response)

                result = JSONObject(response).getJSONObject("data").getString("horoscope")
                CoroutineScope((Dispatchers.Main).launch {

                }
            } else {
                Log.w("API REST", "StatusCode: $responseCode")
                null
            }
            } catch (e: Exception){
                Log.e("API REST", e.localizedMessage, e)
            } finally {
                apiConnection.disconnect()
            }
            return result
        }
    fun readImputStream(inputStream: InputStream): String {
        val `in` = BufferedReader(InputStreamReader(inputStream))
        val response = StringBuffer()
        var inputLine: String? = null

        while ((`in`.readLine().also { inputLine = it }) != null) {
            response.append(inputLine)
        }
        `in`.close()
        return response.toString()
    } }
}

