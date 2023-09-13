package com.example.test1

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.test1.ui.theme.Test1Theme
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference
import kotlin.properties.Delegates

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        @JvmStatic
        lateinit var context: WeakReference<Context>

        @JvmStatic
        var orientation : Int = 0

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        context = WeakReference(this)
        orientation = getResources().getConfiguration().orientation
    }
}
