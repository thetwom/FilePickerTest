package de.moekadu.filepickertest

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

class MainFragment : Fragment() {

    private class FileReaderContract : ActivityResultContract<String, Uri?>() {
        override fun createIntent(context: Context, input: String): Intent {
            return Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "text/plain"
                // default path
                // putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return intent?.data
        }
    }

    private var textView: TextView? = null

    private val readFileCustom = registerForActivityResult(FileReaderContract()) { uri ->
        readContentFromUriAndSetText(uri)
    }

    private val readFileDefault = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        readContentFromUriAndSetText(uri)
    }

    private fun readContentFromUriAndSetText(uri: Uri?) {
        if (uri != null) {
            val fileContent = requireContext().contentResolver?.openInputStream(uri)?.use { stream ->
                stream.reader().use {
                    it.readText()
                }
            }
            textView?.text = fileContent
        } else {
            textView?.text = "Failed to read file!"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        textView = view.findViewById(R.id.text)

        val buttonDefault = view.findViewById<Button>(R.id.button_default)
        buttonDefault?.setOnClickListener {
            readFileDefault.launch(arrayOf("text/plain"))
        }

        val buttonCustom = view.findViewById<Button>(R.id.button_custom)
        buttonCustom?.setOnClickListener {
            readFileCustom.launch("")
        }

        val buttonClear = view.findViewById<Button>(R.id.button_clear)
        buttonClear?.setOnClickListener {
            textView?.text = "No text"
        }
        return view
    }
}