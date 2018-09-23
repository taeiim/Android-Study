package com.jinwoo.socket

import android.app.Application
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

class SocketApplication: Application() {

    companion object {
        private lateinit var socket : Socket
        fun get(): Socket {
            try {
                socket = IO.socket("http://10.156.147.161:1212")
            } catch (e: URISyntaxException) {
                e.printStackTrace();
            }
            return socket
        }
    }
}