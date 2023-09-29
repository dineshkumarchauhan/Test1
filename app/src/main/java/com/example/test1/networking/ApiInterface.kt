package com.demo.networking


import com.demo.model.ItemProducts
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    @GET(Products)
    suspend fun getProducts(
    ): Response<ItemProducts>

}