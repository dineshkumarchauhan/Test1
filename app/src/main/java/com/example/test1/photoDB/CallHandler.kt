package com.example.test1.photoDB

import com.example.test1.models.ItemPhoto

interface CallHandler<T> {

    suspend fun sendRequest(queryInterface: QueryInterface): List<ItemPhoto>


    fun loading(){
    }

    fun success(response: T){
    }

    fun error(message: String){
       // showSnackBar(message)
    }

}