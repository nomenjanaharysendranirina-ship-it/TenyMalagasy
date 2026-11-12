
package com.sendy.tenymalagasy

import android.inputmethodservice.InputMethodService
import android.graphics.Color
import android.view.View
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout

class TenyKeyboardService : InputMethodService() {

    private lateinit var suggestionsRow: LinearLayout

    override fun onCreateInputView(): View {
        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.setPadding(4, 8, 4, 8)
        root.setBackgroundColor(Color.LTGRAY)

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
                button.textSize = 14f

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

        updateSuggestions()
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

        val suggestions = MalagasyDictionary.suggest(word)

        for (suggestion in suggestions) {
            val button = Button(this)
            button.text = suggestion
            button.textSize = 13f

            button.setOnClickListener {
                val input = currentInputConnection
                input?.deleteSurroundingText(word.length, 0)
                input?.commitText("$suggestion ", 1)
                updateSuggestions()
            }

            suggestionsRow.addView(button)
        }
    }
}
