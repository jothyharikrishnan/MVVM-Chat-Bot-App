package com.example.chatbotapp.utils

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

object Time {

    fun timeStamp():String{
        val timeStamp=Timestamp(System.currentTimeMillis())
        val simpleDateFormat=SimpleDateFormat("HH:mm")
        val time=simpleDateFormat.format(Date(timeStamp.time))

        return time.toString()
    }
}