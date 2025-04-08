package com.maroco.smsspam

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Build


@Suppress("UNCHECKED_CAST")
class SmsDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sms_detail)

        val message = intent.getStringExtra("message") ?: ""
        val modelResults = intent.getSerializableExtra("modelResults") as? HashMap<String, Float> ?: hashMapOf()

        findViewById<TextView>(R.id.smsTextView).text = message

        val models = listOf("KoBERT", "KoELECTRA", "KoBigBird", "KoRoBERTa")
        for (model in models) {
            val btnId = when (model) {
                "KoBERT" -> R.id.btnKoBERT
                "KoELECTRA" -> R.id.btnKoELECTRA
                "KoBigBird" -> R.id.btnKoBigBird
                "KoRoBERTa" -> R.id.btnKoRoBERTa
                else -> continue
            }

            val btn = findViewById<Button>(btnId)
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