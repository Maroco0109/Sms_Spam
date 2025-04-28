package com.maroco.smsspam

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Telephony

class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val bundle: Bundle? = intent.extras
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            for (smsMessage in messages) {
                val messageBody = smsMessage.messageBody
                val sender = smsMessage.originatingAddress ?: "Unknown"

                // 수신한 SMS 메시지 형식: "보낸이 : 내용"
                val receivedText = "$sender : $messageBody"

                // 하드코딩된 임시 분류 (0.1f, 0.2f, 0.15f) 설정
                val fakeResults = hashMapOf(
                    "KoBERT" to 0.1f,
                    "KoELECTRA" to 0.2f,
                    "KoRoBERTa" to 0.15f
                )

                // 여기서는 그냥 새로 Intent를 띄우는 게 아니라
                // SpamFragment 스팸 리스트에 추가하거나 (지금은 하드코딩 기반이니 무시 가능)
                // 새 Activity를 띄울 수도 있음

                // 참고: 현재는 하드코딩 기반이라 별도 작업 필요 없음
            }
        }
    }
}
