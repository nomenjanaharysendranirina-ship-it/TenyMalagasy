package com.sendy.tenymalagasy

object MalagasyDictionary {

    val words = listOf(
        "salama",
        "manao",
        "ahoana",
        "ianao",
        "izaho",
        "isika",
        "izy",
        "ianareo",
        "tsara",
        "marina",
        "misaotra",
        "azafady",
        "veloma",
        "andro",
        "alina",
        "maraina",
        "hariva",
        "rano",
        "sakafo",
        "trano",
        "sekoly",
        "fianarana",
        "boky",
        "olona",
        "fianakaviana",
        "namana",
        "fitiavana",
        "fahasalamana",
        "Madagasikara",
        "malagasy"
    )

    fun suggest(input: String): List<String> {
        if (input.isBlank()) return emptyList()

        return words.filter {
            it.startsWith(input, ignoreCase = true)
        }.take(5)
    }
}

