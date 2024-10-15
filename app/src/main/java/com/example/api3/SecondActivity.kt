package com.example.api3

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class AddResponse(
    val status: Int,
    val status_message: String
)

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_second)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // Find the EditText fields by their IDs
        val editTextNumero = findViewById<EditText>(R.id.numero)
        val editTextType = findViewById<EditText>(R.id.type)
        val editTextPrix = findViewById<EditText>(R.id.prix)
        val editTextDisponible = findViewById<EditText>(R.id.disponible)
        val editTextMaxPersonnes = findViewById<EditText>(R.id.max_personnes)
        val editTextUrlImage = findViewById<EditText>(R.id.url_image)
        val buttonAddHotel = findViewById<Button>(R.id.Add_btn)

        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://apiyes.net/") // Replace with your API URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        buttonAddHotel.setOnClickListener {
            // Get the input values from the EditText fields
            val numeroStr = editTextNumero.text.toString().trim()
            val typeStr = editTextType.text.toString().trim()
            val prixStr = editTextPrix.text.toString().trim()
            val disponibleStr = editTextDisponible.text.toString().trim()
            val maxPersonnesStr = editTextMaxPersonnes.text.toString().trim()
            val urlImageStr = editTextUrlImage.text.toString().trim()

            // Validate input
            if (numeroStr.isEmpty() || typeStr.isEmpty() || prixStr.isEmpty() ||
                disponibleStr.isEmpty() || maxPersonnesStr.isEmpty() || urlImageStr.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                // Parse input values
                val numero = numeroStr.toInt()
                val prix = prixStr.toInt()
                val disponible = disponibleStr.toBoolean()
                val maxPersonnes = maxPersonnesStr.toInt()

                // Make the API call with all the fields
                apiService.addHotel(numero, typeStr, prix, disponible, maxPersonnes, urlImageStr)
                    .enqueue(object : Callback<AddResponse> {
                        override fun onResponse(call: Call<AddResponse>, response: Response<AddResponse>) {
                            if (response.isSuccessful) {
                                val addResponse = response.body()
                                if (addResponse != null) {
                                    Toast.makeText(applicationContext, addResponse.status_message, Toast.LENGTH_LONG).show()
                                    if (addResponse.status == 1) {
                                        finish() // Close the activity or reset fields
                                    }
                                } else {
                                    Toast.makeText(applicationContext, "Error: Empty response from server", Toast.LENGTH_LONG).show()
                                }
                            } else {
                                Toast.makeText(applicationContext, "Failed to add hotel. Server returned error code: ${response.code()}", Toast.LENGTH_LONG).show()
                            }
                        }

                        override fun onFailure(call: Call<AddResponse>, t: Throwable) {
                            Toast.makeText(applicationContext, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                        }
                    })
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
