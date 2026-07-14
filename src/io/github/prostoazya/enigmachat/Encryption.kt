package io.github.prostoazya.enigmachat

object Encryption {
    const val ALPHABET = """"Ъ:Iи%2спью-РИЭХ@щ!HЁ+ж(а4>О,ЛŌq.BaOВlzCU3pbns{`|Tjё'Ūāo<QдDшПĀфv=уūБPЯdhкZгЕКЮтМruФбЩ1fWЖ;СV0k#N58нхяīJōĒыАĪо7УзГR л*ЦчШAēЬе/]рK\Ы9eM[tЗЙ_xвGwESТэ)Fmiъ$~Чц^cНмйyД6}&gYL?X"""

    fun encrypt(input: String, key: String): String {
       var encryptedString = ""

        for (i in 0..<input.length) {
            val inputChar = input[i]
            val keyChar = key[i % key.length]

            if (!ALPHABET.contains(keyChar) || !ALPHABET.contains(inputChar)) {
                encryptedString += inputChar
                continue
            }

            val newIndex = (ALPHABET.indexOf(inputChar) + ALPHABET.indexOf(keyChar)) % ALPHABET.length
            encryptedString += ALPHABET[newIndex]
        }

        return encryptedString
    }

    fun decrypt(input: String, key: String): String {
        var decryptedString = ""

        for (i in 0..<input.length) {
            val inputChar = input[i]
            val keyChar = key[i % key.length]

            if (!ALPHABET.contains(keyChar) || !ALPHABET.contains(inputChar)) {
                decryptedString += inputChar
                continue
            }

            val newIndex = (ALPHABET.indexOf(inputChar) - ALPHABET.indexOf(keyChar) + ALPHABET.length) % ALPHABET.length
            decryptedString += ALPHABET[newIndex]
        }

        return decryptedString
    }
}