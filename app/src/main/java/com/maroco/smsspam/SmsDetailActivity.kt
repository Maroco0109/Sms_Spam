package com.maroco.smsspam

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class SmsDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sms_detail)

        val message = intent.getStringExtra("message") ?: ""
        val modelResults = intent.getSerializableExtra("modelResults") as? HashMap<String, Float> ?: hashMapOf()

        findViewById<TextView>(R.id.smsTextView).text = message

        val models = listOf("KoBERT", "KoELECTRA", "KoRoBERTa")
        for (model in models) {
            val resId = resources.getIdentifier("btn$model", "id", packageName)
            val btn = findViewById<Button>(resId)
            btn.setOnClickListener {
                val prob = modelResults[model]?.times(100)?.let { "%.1f".format(it) } ?: "결과 없음"
                AlertDialog.Builder(this)
                    .setTitle("$model 결과")
                    .setMessage("이 메시지는 $prob% 확률로 스팸입니다.")
                    .setPositiveButton("확인", null)
                    .show()
            }
        }
    }
}
