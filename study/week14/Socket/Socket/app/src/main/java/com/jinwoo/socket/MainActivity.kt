package com.jinwoo.socket

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONException

class MainActivity : AppCompatActivity() {

    lateinit var Text : TextView
    lateinit var lighton_btn: Button
    lateinit var lightoff_btn: Button
    lateinit var socket: Socket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lighton_btn = findViewById(R.id.lighton_button)
        lightoff_btn = findViewById(R.id.lightoff_button)
        Text = findViewById(R.id.message)

        socket = SocketApplication.get()

        socket.connect()

        Text.setText("소켓 생성")



        lighton_btn.setOnClickListener { v ->
            socket.emit("lightOn")
            Text.setText("Light on Emit 성공")
        }

        lightoff_btn.setOnClickListener { v ->
            socket.emit("lightOff")
            Text.setText("Light off Emit 성공")
        }
    }


}
