
package com.sendy.tenymalagasy

import android.inputmethodservice.InputMethodService
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout

class TenyKeyboardService : InputMethodService() {

    private lateinit var suggestionsRow: LinearLayout

    override fun onCreateInputView(): View {
        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.setPadding(5, 8, 5, 8)
        root.setBackgroundColor(Color.rgb(220, 225, 232))

        suggestionsRow = LinearLayout(this)
        suggestionsRow.orientation = LinearLayout.HORIZONTAL
        suggestionsRow.gravity = Gravity.CENTER
        root.addView(suggestionsRow)

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
                button.textSize = 16f
                button.setTextColor(Color.BLACK)
                button.setTypeface(null, Typeface.BOLD)
                button.isAllCaps = false
                button.background = GradientDrawable().apply {
                    setColor(Color.WHITE)
                    cornerRadius = 12f
                    setStroke(1, Color.LTGRAY)
                }

                val params = LinearLayout.LayoutParams(
                    0, 52, if (key == "ESPACE") 3f else 1f
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
                    updateSuggestions()
                }

                line.addView(button)
            }
            root.addView(line)
        }

        return root
    }

    private fun updateSuggestions() {
        if (!::suggestionsRow.isInitialized) return

        suggestionsRow.removeAllViews()

        val before = currentInputConnection
            ?.getTextBeforeCursor(100, 0)?.toString() ?: ""

        val word = Regex("[\\p{L}']+$")
            .find(before)?.value ?: ""

        if (word.isEmpty()) return

        MalagasyDictionary.suggest(word).forEach { suggestion ->
            val button = Button(this)
            button.text = suggestion
            button.setTextColor(Color.BLACK)
            button.isAllCaps = false

            button.setOnClickListener {
                currentInputConnection?.deleteSurroundingText(word.length, 0)
                currentInputConnection?.commitText("$suggestion ", 1)
                updateSuggestions()
            }

            suggestionsRow.addView(button)
        }
    }
}
