package com.maroco.smsspam

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView

class InboxFragment : Fragment() {

    private lateinit var listView: ListView
    private val smsList = listOf(
        "VK-COURTS - Court case info",
        "BV-AMULHO - GATE 2020 Waiting",
        "VK-MININB - Visit Saras Mela"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_inbox, container, false)
        listView = view.findViewById(R.id.inboxListView)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, smsList)
        listView.adapter = adapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val intent = Intent(requireContext(), SmsDetailActivity::class.java)
            intent.putExtra("message", smsList[position])
            startActivity(intent)
        }

        return view
    }
}