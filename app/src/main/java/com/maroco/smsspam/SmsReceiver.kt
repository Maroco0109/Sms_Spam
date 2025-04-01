package com.maroco.smsspam

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import android.widget.Toast

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION == intent.action) {
            val bundle: Bundle? = intent.extras
            val msgs = bundle?.get("pdus") as? Array<*>
            val format = bundle?.getString("format")
            msgs?.forEach {
                val msg = Telephony.Sms.Intents.getMessagesFromIntent(intent)[0]
                val messageBody = msg.messageBody
                val sender = msg.originatingAddress

                // 💥 여기서 간단한 스팸 기준 예시
                if (messageBody.contains("SALE") || messageBody.contains("SUBSCRIPTION")) {
                    SpamFragment.spamList.add("$sender : $messageBody")
                    Toast.makeText(context, "스팸 메시지 감지됨", Toast.LENGTH_SHORT).show()
                }

                Log.d("SMS_RECEIVED", "$sender : $messageBody")
            }
        }
    }
}