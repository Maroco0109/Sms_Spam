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

class InboxFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>

    private val inboxMessages = listOf(
        "안녕하세요. 고객님 주문이 완료되었습니다.",
        "이번 주 할인 행사 안내드립니다!",
        "친구가 보낸 메시지입니다. 확인해주세요.",
        "안녕하세요. 홍길동입니다."
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_inbox, container, false)

        listView = view.findViewById(R.id.inboxListView)
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, inboxMessages)
        listView.adapter = adapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val intent = Intent(requireContext(), SmsDetailActivity::class.java)
            intent.putExtra("message", inboxMessages[position])
            intent.putExtra("modelResults", hashMapOf(
                "KoBERT" to 0.1f,
                "KoELECTRA" to 0.2f,
                "KoRoBERTa" to 0.15f
            ))
            startActivity(intent)
        }

        return view
    }
}
