package com.maroco.smsspam

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.Telephony
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class InboxFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val inboxList = mutableListOf<Sms>()
    private var permissionGranted = false

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[Manifest.permission.READ_SMS] == true &&
                permissions[Manifest.permission.RECEIVE_SMS] == true) {
                permissionGranted = true
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
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, mutableListOf())
        listView.adapter = adapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val intent = Intent(requireContext(), SmsDetailActivity::class.java)
            intent.putExtra("message", inboxList[position].body)
            intent.putExtra("modelResults", HashMap(inboxList[position].modelResults))
            startActivity(intent)
        }

        checkPermission()
        return view
    }

    override fun onResume() {
        super.onResume()
        if (permissionGranted) {
            permissionGranted = false
            classifyAllSmsPlaceholder()
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED
        ) {
            permissionGranted = true
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.READ_SMS,
                    Manifest.permission.RECEIVE_SMS
                )
            )
        }
    }

    private fun classifyAllSmsPlaceholder() {
        inboxList.clear()
        val smsList = readSms()
        smsList.forEach { smsText ->
            val results = mutableMapOf(
                "KoBERT" to 0.2f,
                "KoELECTRA" to 0.5f,
                "KoBigBird" to 0.3f,
                "KoRoBERTa" to 0.1f
            )
            val sms = Sms(body = smsText, modelResults = results)
            val isSpam = results.values.any { it > 0.7f }
            if (isSpam) {
                SpamFragment.spamList.add(sms)
            } else {
                inboxList.add(sms)
            }
        }
        adapter.clear()
        adapter.addAll(inboxList.map { it.body })
        adapter.notifyDataSetChanged()
    }

    private fun readSms(): List<String> {
        val result = mutableListOf<String>()
        val uriSms: Uri = Telephony.Sms.Inbox.CONTENT_URI
        val cursor: Cursor? = requireContext().contentResolver.query(uriSms, null, null, null, null)

        cursor?.let {
            val bodyIndex = cursor.getColumnIndex(Telephony.Sms.BODY)
            while (cursor.moveToNext()) {
                val body = if (bodyIndex != -1) cursor.getString(bodyIndex) else ""
                if (body.isNotBlank()) result.add(body)
            }
            cursor.close()
        }
        return result
    }
}
