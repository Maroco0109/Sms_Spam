package com.maroco.smsspam

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView

class SpamFragment : Fragment() {

    private lateinit var listView: ListView
    private val spamList = listOf(
        "BZ-NETMED - Sale Begins!",
        "VM-PAYTMB - Education Pvt",
        "AD-AIRSEP - Hellotune Subscription"
    )

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_spam, container, false)
        listView = view.findViewById(R.id.spamListView)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, spamList)
        listView.adapter = adapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val intent = Intent(requireContext(), SmsDetailActivity::class.java)
            intent.putExtra("message", spamList[position])
            startActivity(intent)
        }

        return view
    }
}
