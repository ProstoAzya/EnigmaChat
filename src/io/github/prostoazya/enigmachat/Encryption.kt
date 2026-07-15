package io.github.prostoazya.enigmachat

object Encryption {
    const val BASE_ALPHABET = """ !"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{|}~ĀāĒēĪīŌōŪūЁАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмнопрстуфхцчшщъыьэюяё"""

    var alphabet: String = BASE_ALPHABET
    private set

    fun updateAlphabet(shuffleKey: String) {
        if (shuffleKey.isEmpty()) {
            alphabet = BASE_ALPHABET
            return
        }

        val charList = Shuffle.shuffle(BASE_ALPHABET.map { it }.toMutableList(), shuffleKey)

        alphabet = charList.joinToString("")
    }

    fun encrypt(input: String, key: String): String {
       var encryptedString = ""

        for (i in 0..<input.length) {
            val inputChar = input[i]
            val keyChar = key[i % key.length]

            if (!alphabet.contains(keyChar) || !alphabet.contains(inputChar)) {
                encryptedString += inputChar
                continue
            }

            val newIndex = (alphabet.indexOf(inputChar) + alphabet.indexOf(keyChar)) % alphabet.length
            encryptedString += alphabet[newIndex]
        }

        return encryptedString
    }

    fun decrypt(input: String, key: String): String {
        var decryptedString = ""

        for (i in 0..<input.length) {
            val inputChar = input[i]
            val keyChar = key[i % key.length]

            if (!alphabet.contains(keyChar) || !alphabet.contains(inputChar)) {
                decryptedString += inputChar
                continue
            }

            val newIndex = (alphabet.indexOf(inputChar) - alphabet.indexOf(keyChar) + alphabet.length) % alphabet.length
            decryptedString += alphabet[newIndex]
        }

        return decryptedString
    }
}