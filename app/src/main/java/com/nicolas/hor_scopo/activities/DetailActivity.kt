package com.nicolas.hor_scopo.activities

import android.content.pm.PackageInstaller
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nicolas.hor_scopo.data.Horoscope
import com.nicolas.hor_scopo.R
import com.nicolas.hor_scopo.utils.SessionManager

class DetailActivity : AppCompatActivity() {

    lateinit var session: SessionManager

    lateinit var horoscope: Horoscope
    var isFavorite = false
    lateinit var favoriteMenuItem: MenuItem

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

        val id = intent.getStringExtra("HOROSCOPE_ID")!!

        horoscope = Horoscope.getByID(id)

        supportActionBar?.setTitle(horoscope.name)
        supportActionBar?.setSubtitle(horoscope.dates)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Preguntar si el horosocopo es favorito
        isFavorite = session.isFavorite(id)

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
                //cambiar el icono del menu
                true
            }

            R.id.menu_share -> {
                //Menu compartir
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
}