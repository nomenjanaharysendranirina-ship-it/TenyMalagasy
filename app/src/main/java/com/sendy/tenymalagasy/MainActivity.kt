package com.sendy.tenymalagasy

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import android.widget.LinearLayout
import android.view.Gravity

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.gravity = Gravity.CENTER
        layout.setPadding(30, 30, 30, 30)

        val title = TextView(this)
        title.text = "Tongasoa eto amin'ny Teny Malagasy!"
        title.textSize = 24f
        title.gravity = Gravity.CENTER

        val description = TextView(this)
        description.text = "Kitendry Malagasy marani-tsaina"
        description.textSize = 18f
        description.gravity = Gravity.CENTER

        layout.addView(title)
        layout.addView(description)

        setContentView(layout)
    }
}

