package com.nicolas.hor_scopo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    val horoscopeList: List<Horoscope> = listOf(
        Horoscope("aries", R.string.horoscope_name_aries, R.string.horoscope_date_aries, R.drawable.aries_icon),
        Horoscope("tauro", R.string.horoscope_name_tauro, R.string.horoscope_date_tauro, R.drawable.taurus_icon),
        Horoscope("geminis", R.string.horoscope_name_geminis, R.string.horoscope_date_geminis, R.drawable.gemini_icon),
        Horoscope("cancer", R.string.horoscope_name_cancer, R.string.horoscope_date_cancer, R.drawable.cancer_icon),
        Horoscope("leo", R.string.horoscope_name_leo, R.string.horoscope_date_leo, R.drawable.leo_icon),
        Horoscope("virgo", R.string.horoscope_name_virgo, R.string.horoscope_date_virgo, R.drawable.virgo_icon),
        Horoscope("libra", R.string.horoscope_name_libra, R.string.horoscope_date_libra, R.drawable.libra_icon),
        Horoscope("escorpio", R.string.horoscope_name_escorpio, R.string.horoscope_date_escorpio, R.drawable.scorpio_icon),
        Horoscope("sagitario", R.string.horoscope_name_sagitario, R.string.horoscope_date_sagitario, R.drawable.sagittarius_icon),
        Horoscope("capricornio", R.string.horoscope_name_capricornio, R.string.horoscope_date_capricornio, R.drawable.capricorn_icon),
        Horoscope("acuario", R.string.horoscope_name_acuario, R.string.horoscope_date_acuario, R.drawable.aquarius_icon),
        Horoscope("piscis", R.string.horoscope_name_piscis, R.string.horoscope_date_piscis, R.drawable.pisces_icon)
    )

    lateinit var recyclerView: RecyclerView

    lateinit var adapter: HoroscopeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerView)

        adapter = HoroscopeAdapter(horoscopeList) { position ->
            val horoscope = horoscopeList[position]
            //Navegar al signo clickado
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("HOROSCOPE_ID",horoscope.id)
            startActivity(intent)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

    }
}