package com.myapplication.astonfour

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    lateinit var btn_change_style_one:Button
    lateinit var btn_change_style_two:Button
    lateinit var custom_view_watch:WatchCustomView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_change_style_one = findViewById(R.id.btn_chenge_style_one)
        btn_change_style_two = findViewById(R.id.btn_change_style_two)
        custom_view_watch = findViewById(R.id.watchCustomView)
        btn_change_style_one.setOnClickListener {
            custom_view_watch.changeColorStyleOne()
        }
        btn_change_style_two.setOnClickListener {
            custom_view_watch.changeColorStyleTwo()
        }

    }
}