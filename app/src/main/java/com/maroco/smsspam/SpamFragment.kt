package com.maroco.smsspam

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment

class SpamFragment : Fragment() {

    companion object {
        val spamList = mutableListOf<Sms>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_spam, container, false)
        val listView: ListView = view.findViewById(R.id.spamListView)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, spamList.map { it.body })
        listView.adapter = adapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val intent = Intent(requireContext(), SmsDetailActivity::class.java)
            intent.putExtra("message", spamList[position].body)
            intent.putExtra("modelResults", HashMap(spamList[position].modelResults))
            startActivity(intent)
        }

        return view
    }
}