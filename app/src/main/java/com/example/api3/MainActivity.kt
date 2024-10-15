package com.example.api3

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class Hotel(
    val numero : Int,
    val type : String,
    val nuite : Int,
    val disponible : Boolean,
    val max_personnes : Int,
    val url_image : String
)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val list_view = findViewById<ListView>(R.id.lv)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://apiyes.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        val call = apiService.getRoom()
        call.enqueue(object : Callback<List<Hotel>>{
            override fun onResponse(call: Call<List<Hotel>>, response: Response<List<Hotel>>) {
                if (response.isSuccessful){
                    val Rooms = response.body() ?: emptyList()

                    val Room_str = ArrayList<String>()

                    for (room in Rooms){
                        Room_str.add("${room.numero} - ${room.type} - ${room.nuite} MAD")
                    }

                    val adapter = ArrayAdapter(this@MainActivity , android.R.layout.simple_list_item_1 , Room_str)
                    list_view.adapter = adapter

                    list_view.setOnItemClickListener{parent, view, position, id ->
                        val chmbr = Rooms[position]

                        val imv = findViewById<ImageView>(R.id.img)
                        Glide.with(this@MainActivity)
                            .load(chmbr.url_image)
                            .into(imv)

                        val info_txt = findViewById<TextView>(R.id.info)
                        info_txt.text = "Numero: ${chmbr.numero}\nType: ${chmbr.type}\nNuite: ${chmbr.nuite}\nDisponible: ${chmbr.disponible}\nMax perssones: ${chmbr.max_personnes}"
                    }
                }
                else{
                    Toast.makeText(this@MainActivity, "Echec de recuperer les donnes", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Hotel>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Erreur de connexion", Toast.LENGTH_SHORT).show()
            }
        })

        val button = findViewById<Button>(R.id.add)
        button.setOnClickListener{
            val intent = Intent(this , SecondActivity::class.java)
            startActivity(intent)
        }
    }
}