package types.caesar

fun encrypt(text: String, shift: Int): String {
    val result = StringBuilder()

    for (char in text) {
        if (char.isLetter()) {
            val base = if (char.isUpperCase()) 'A' else 'a'
            val shiftedChar = base + ((char - base + shift) % 26 + 26) % 26
            result.append(shiftedChar)
        } else {
            result.append(char)
        }
    }

    return result.toString()
}

fun decrypt(text: String, shift: Int): String {
    val result = StringBuilder()

    for (char in text) {
        if (char.isLetter()) {
            val base = if (char.isUpperCase()) 'A' else 'a'
            val shiftedChar = base + ((char - base - shift) % 26 + 26) % 26
            result.append(shiftedChar)
        } else {
            result.append(char)
        }
    }

    return result.toString()
}