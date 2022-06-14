package de.moekadu.filepickertest

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import androidx.fragment.app.replace

class MainActivity : AppCompatActivity() {

    private val readFile = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        Log.v("FilePickerTest", "MainActivity.readFile : uri ? $uri")
        if (uri != null) {
            val fileContent = contentResolver?.openInputStream(uri)?.use { stream ->
                stream.reader().use {
                    it.readText()
                }
            }
            textView?.text = fileContent
        } else {
            textView?.text = "Failed to read file!"
        }
    }

    private var textView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.text)

        val button = findViewById<Button>(R.id.button)
        button?.setOnClickListener {
            readFile.launch(arrayOf("text/plain"))
        }

    }
}