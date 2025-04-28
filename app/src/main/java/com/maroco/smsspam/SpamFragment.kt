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

    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>

    private val spamMessages = listOf(
        "★대출 한도 조회★ 지금 바로 확인하세요!",
        "[광고] 신규회원 10만원 지급!",
        "지금 클릭하고 경품 받아가세요!",
        "무료 쿠폰 받기! 한정 이벤트 중!"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_spam, container, false)

        listView = view.findViewById(R.id.spamListView)
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, spamMessages)
        listView.adapter = adapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val intent = Intent(requireContext(), SmsDetailActivity::class.java)
            intent.putExtra("message", spamMessages[position])
            intent.putExtra("modelResults", hashMapOf(
                "KoBERT" to 0.9f,
                "KoELECTRA" to 0.85f,
                "KoRoBERTa" to 0.92f
            ))
            startActivity(intent)
        }

        return view
    }
}
