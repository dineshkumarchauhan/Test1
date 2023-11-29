package com.demo.networking


import com.example.test1.models.ItemContributors
import com.example.test1.models.Items
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    @GET(Items)
    suspend fun getItems(
        @Query("q") query: String
    ): Response<Items>


//    @GET("repos/{name}/"+Contributors)

    @GET("repos/{login}/{name}/"+Contributors)
    suspend fun getContributors(
        @Path("login") filter: String,
        @Path("name") filter2: String,
    ): Response<ItemContributors>



}