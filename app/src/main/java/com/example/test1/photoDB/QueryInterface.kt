package com.example.test1.photoDB
import com.example.test1.models.ItemPhoto


interface QueryInterface {
    suspend fun getQuery(query: String): List<ItemPhoto>
}