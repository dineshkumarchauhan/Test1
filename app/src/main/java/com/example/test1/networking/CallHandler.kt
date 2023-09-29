package com.demo.networking

import com.example.test1.utils.showSnackBar


fun interface CallHandler<T> {


    suspend fun sendRequest(apiInterface: ApiInterface): T


    fun loading(){
    }

    fun success(response: T){
    }


    fun error(message: String){
        showSnackBar(message)
    }

}