package com.example.chatbotapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatbotapp.data.Message
import com.example.chatbotapp.adapter.MessagingAdapter
import com.example.chatbotapp.utils.BotResponse
import com.example.chatbotapp.utils.Constants.OPEN_GOOGLE
import com.example.chatbotapp.utils.Constants.OPEN_SEARCH
import com.example.chatbotapp.utils.Constants.RECEIVE_ID
import com.example.chatbotapp.utils.Constants.SEND_ID
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter:MessagingAdapter
    var messagesList = mutableListOf<Message>()

    private val botList= listOf("Peter","Joe","Bot","Root")
    var btn_send:Button?=null
    var et_message:EditText?=null
    var rv_messages:RecyclerView?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_send=findViewById(R.id.btn_send)
        et_message=findViewById<EditText>(R.id.et_message)
        rv_messages=findViewById<RecyclerView>(R.id.rv_messages)

        recyclerView()
        clickEvents()

        val random = (0..3).random()
        customBotMessage("Hello! Today you're speaking with ${botList[random]}, how may I help?")
    }

    private fun clickEvents() {

        btn_send?.setOnClickListener {
            sendMessage()
        }

        et_message?.setOnClickListener {
            GlobalScope.launch {
                delay(100)

                withContext(Dispatchers.Main) {
                    rv_messages?.scrollToPosition(adapter.itemCount - 1)

                }
            }
        }
    }

    private fun recyclerView() {
        adapter = MessagingAdapter()
        rv_messages?.adapter = adapter
        rv_messages?.layoutManager = LinearLayoutManager(applicationContext)

    }

    override fun onStart() {
        super.onStart()
        GlobalScope.launch {
            delay(100)
            withContext(Dispatchers.Main) {
                rv_messages?.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    private fun sendMessage() {
        val message = et_message?.text.toString()
        val timeStamp = com.example.chatbotapp.utils.Time.timeStamp()

        if (message.isNotEmpty()) {
            messagesList.add(Message(message, SEND_ID, timeStamp))
            et_message?.setText("")

            adapter.insertMessage(Message(message, SEND_ID, timeStamp))
            rv_messages?.scrollToPosition(adapter.itemCount - 1)

            botResponse(message)
        }
    }

    private fun botResponse(message: String) {
        val timeStamp = com.example.chatbotapp.utils.Time.timeStamp()

        GlobalScope.launch {

            delay(1000)

            withContext(Dispatchers.Main) {

                val response = BotResponse.basicResponses(message)


                messagesList.add(Message(response, RECEIVE_ID, timeStamp))


                adapter.insertMessage(Message(response, RECEIVE_ID, timeStamp))


                rv_messages?.scrollToPosition(adapter.itemCount - 1)


                when (response) {
                    OPEN_GOOGLE -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        site.data = Uri.parse("https://www.google.com/")
                        startActivity(site)
                    }
                    OPEN_SEARCH -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        val searchTerm: String? = message.substringAfterLast("search")
                        site.data = Uri.parse("https://www.google.com/search?&q=$searchTerm")
                        startActivity(site)
                    }

                }
            }
        }
    }

    private fun customBotMessage(message: String) {

        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                val timeStamp = com.example.chatbotapp.utils.Time.timeStamp()
                messagesList.add(Message(message, RECEIVE_ID, timeStamp))
                adapter.insertMessage(Message(message, RECEIVE_ID, timeStamp))

                rv_messages?.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }
}