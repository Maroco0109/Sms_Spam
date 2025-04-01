package com.maroco.smsspam

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.Telephony
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import kotlinx.coroutines.*
import org.pytorch.IValue
import org.pytorch.Module
import org.pytorch.Tensor
import java.io.File

class InboxFragment : Fragment() {

    private lateinit var listView: ListView
    private val inboxList = mutableListOf<String>()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[Manifest.permission.READ_SMS] == true &&
                permissions[Manifest.permission.RECEIVE_SMS] == true) {
                classifyAllSms()
            } else {
                Toast.makeText(requireContext(), "SMS 권한이 필요합니다", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_inbox, container, false)
        listView = view.findViewById(R.id.inboxListView)

        checkPermission()

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, inboxList)
        listView.adapter = adapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val intent = Intent(requireContext(), SmsDetailActivity::class.java)
            intent.putExtra("message", inboxList[position])
            startActivity(intent)
        }

        return view
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_SMS
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECEIVE_SMS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            classifyAllSms()
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.READ_SMS,
                    Manifest.permission.RECEIVE_SMS
                )
            )
        }
    }

    private fun classifyAllSms() {
        CoroutineScope(Dispatchers.IO).launch {
            val smsList = readSms()
            val model = Module.load(assetFilePath("spam_model.pt"))

            smsList.forEach { sms ->
                val inputTensor = preprocess(sms)
                val output = model.forward(IValue.from(inputTensor)).toTensor()
                val confidence = output.dataAsFloatArray[0]

                if (confidence > 0.7f) {
                    SpamFragment.spamList.add(sms)
                } else {
                    inboxList.add(sms)
                }
            }

            withContext(Dispatchers.Main) {
                (listView.adapter as ArrayAdapter<*>).notifyDataSetChanged()
            }
        }
    }

    private fun readSms(): List<String> {
        val result = mutableListOf<String>()
        val uriSms: Uri = Telephony.Sms.Inbox.CONTENT_URI
        val cursor: Cursor? = requireContext().contentResolver.query(uriSms, null, null, null, null)

        cursor?.let {
            if (cursor.moveToFirst()) {
                do {
                    val address = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
                    val body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY))
                    result.add(body)
                } while (cursor.moveToNext())
            }
            cursor.close()
        }
        return result
    }

    private fun assetFilePath(assetName: String): String {
        val file = File(requireContext().filesDir, assetName)
        if (file.exists() && file.length() > 0) {
            return file.absolutePath
        }
        requireContext().assets.open(assetName).use { inputStream ->
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return file.absolutePath
    }

    private fun preprocess(text: String): Tensor {
        // ★ 샘플 전처리 (토큰화 → 임베딩 → Tensor 변환)
        val inputIds = FloatArray(128) { 0f }
        val tokens = text.take(128).map { it.code.toFloat() } // 예시 (실제 모델용 tokenizer로 대체 필요)
        for (i in tokens.indices) {
            inputIds[i] = tokens[i]
        }
        return Tensor.fromBlob(inputIds, longArrayOf(1, 128))
    }
}
