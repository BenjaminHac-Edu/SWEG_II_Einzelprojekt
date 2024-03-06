package com.example.sweg_ii_einzelprojekt

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.Socket

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val matrNumberField = findViewById<TextView>(R.id.tfMatrNumber)
        val button = findViewById<Button>(R.id.btnSend)


        button.setOnClickListener {
            val resultTextView = findViewById<TextView>(R.id.tvAnswerServer)
            Thread {
                try {
                    Socket("se2-submission.aau.at", 20080).use { socket ->
                        val output = PrintWriter(BufferedWriter(OutputStreamWriter(socket.getOutputStream())), true)
                        val input = BufferedReader(InputStreamReader(socket.getInputStream()))

                        output.println(matrNumberField.text.toString())

                        val response = input.readLine()
                        runOnUiThread {
                            resultTextView.text = response
                        }
                    }
                } catch (e: IOException) {
                    e.message?.let { it1 -> Log.e("NetworkTest", it1) }
                }
            }.start()
        }
    }
}