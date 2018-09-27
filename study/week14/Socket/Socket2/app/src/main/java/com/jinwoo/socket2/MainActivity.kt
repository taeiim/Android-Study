package com.jinwoo.socket2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import io.socket.client.Socket
import io.socket.emitter.Emitter

class MainActivity : AppCompatActivity() {

    lateinit var Text : TextView
    lateinit var Receive_Text: TextView
    lateinit var socket: Socket
    var data: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Text = findViewById(R.id.message)
        Receive_Text = findViewById(R.id.receive_Text)

        socket = SocketApplication.get()

        socket.connect()

        socket.on("lightOn",light_on)
        socket.on("lightOff", light_off)
    }

    var light_on = Emitter.Listener { args ->
        runOnUiThread({
            Text.setText("소켓 on 성공")
            data = args[0].toString()
            Receive_Text.setText(data)
        })
    }

    var light_off = Emitter.Listener { args ->
        runOnUiThread({
            Text.setText("소켓 on 성공")
            data = args[0].toString()
            Receive_Text.setText(data)
        })
    }

}
