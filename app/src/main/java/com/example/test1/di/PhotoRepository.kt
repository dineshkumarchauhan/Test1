package com.example.test1.di

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.example.test1.MainActivity
import com.example.test1.databinding.LoaderBinding
import com.example.test1.photoDB.QueryInterface
import com.example.test1.photoDB.CallHandler
import com.example.test1.utils.hideSoftKeyBoard
import com.example.test1.utils.ioDispatcher
import com.example.test1.utils.mainThread
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PhotoRepository @Inject constructor(private val queryInterface: QueryInterface) {
    var alertDialog: AlertDialog? = null

    private val mainDispatcher by lazy { Dispatchers.Main }

    suspend fun <T> callQuery(
        loader: Boolean = true,
        callHandler: CallHandler<T>
    ) {

        hideSoftKeyBoard()

        val coRoutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            mainThread {
                throwable.message.let {
                    hideLoader()
                    if (it != null) {
                        callHandler.error(it)
                    }
                }
            }
        }

        CoroutineScope(Dispatchers.IO + coRoutineExceptionHandler + Job()).launch {
            flow {
                emit(callHandler.sendRequest(queryInterface = queryInterface))
            }.flowOn(ioDispatcher)
                .onStart {
                    callHandler.loading()
                    withContext(mainDispatcher) {
                        if (loader) MainActivity.context?.get()?.showLoader()
                    }
                }.catch { error ->
                    withContext(mainDispatcher) {
                        hideLoader()
                        error.message?.let { callHandler.error(it) }
                    }
                }.collect { response ->
                    withContext(mainDispatcher) {
                        hideLoader()
                        callHandler.success(response as T)
                    }
                }
        }
    }





    private fun Context.showLoader() {
        if (alertDialog == null) {
            val alert = AlertDialog.Builder(this)
            val binding = LoaderBinding.inflate(LayoutInflater.from(this), null, false)
            alert.setView(binding.root)
            alert.setCancelable(false)
            alertDialog = alert.create()
            alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            if(alertDialog != null) {
                alertDialog?.show()
            }
        }
    }


    /**
     * Hide Loader
     * */
    private fun hideLoader() {
        if(alertDialog != null) {
            alertDialog?.cancel()
            alertDialog = null
        }

    }
}