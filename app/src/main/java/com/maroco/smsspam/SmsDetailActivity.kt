package com.maroco.smsspam

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView

class SmsDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sms_detail)

        val messageText = findViewById<TextView>(R.id.messageContent)
        val resultText = findViewById<TextView>(R.id.spamResult)
        val checkButton = findViewById<Button>(R.id.checkButton)

        val smsMessage = intent.getStringExtra("message")
        messageText.text = smsMessage

        checkButton.setOnClickListener {
            // 예시: 추론 결과 표시
            resultText.text = """
                KoBERT : SPAM (99.8%)
                KoELECTRA : SPAM (99.7%)
                KoBigBird : SPAM (98.6%)
                KoRoberta : SPAM (99.2%)
            """.trimIndent()
        }
    }
}
