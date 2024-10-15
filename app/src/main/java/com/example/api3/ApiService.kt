package com.example.api3

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("/HotelAPI/readAll.php")
    fun getRoom():Call<List<Hotel>>

    @POST("/HotelAPI/create.php")  // Replace with your actual API endpoint
    @FormUrlEncoded
    fun addHotel(
        @Field("numero") numero: Int,
        @Field("type") type: String,
        @Field("nuite") nuite: Int,
        @Field("disponible") disponible: Boolean,
        @Field("max_personnes") maxPersonnes: Int,
        @Field("url_image") urlImage: String
    ): Call<AddResponse>
}