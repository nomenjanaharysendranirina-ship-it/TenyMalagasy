
package com.sendy.tenymalagasy

import android.inputmethodservice.InputMethodService
import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout

class TenyKeyboardService : InputMethodService() {

    override fun onCreateInputView(): View {

        val keyboard = LinearLayout(this)
        keyboard.orientation = LinearLayout.VERTICAL
        keyboard.setPadding(4, 8, 4, 8)
        keyboard.setBackgroundColor(Color.LTGRAY)

        val rows = listOf(
            listOf("q","w","e","r","t","y","u","i","o","p"),
            listOf("a","s","d","f","g","h","j","k","l"),
            listOf("z","x","c","v","b","n","m"),
            listOf("?123", ",", "ESPACE", ".", "⌫", "↵")
        )

        for (row in rows) {
            val line = LinearLayout(this)
            line.orientation = LinearLayout.HORIZONTAL
            line.gravity = Gravity.CENTER

            for (key in row) {
                val button = Button(this)
                button.text = if (key == "ESPACE") "Espace" else key
                button.textSize = 14f
                button.setTypeface(null, Typeface.BOLD)

                val params = LinearLayout.LayoutParams(
                    0,
                    52,
                    if (key == "ESPACE") 3f else 1f
                )
                params.setMargins(2, 3, 2, 3)

                button.layoutParams = params

                button.setOnClickListener {
                    val input = currentInputConnection

                    when (key) {
                        "⌫" -> input?.deleteSurroundingText(1, 0)
                        "↵" -> input?.commitText("\n", 1)
                        "ESPACE" -> input?.commitText(" ", 1)
                        "?123" -> { }
                        else -> input?.commitText(key, 1)
                    }
                }

                line.addView(button)
            }

            keyboard.addView(line)
        }

        return keyboard
    }
}
