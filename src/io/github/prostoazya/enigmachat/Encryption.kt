package io.github.prostoazya.enigmachat

object Encryption {
    const val ALPHABET = """"Ъ:Iи%2спью-РИЭХ@щ!HЁ+ж(а4>О,ЛŌq.BaOВlzCU3pbns{`|Tjё'Ūāo<QдDшПĀфv=уūБPЯdhкZгЕКЮтМruФбЩ1fWЖ;СV0k#N58нхяīJōĒыАĪо7УзГR л*ЦчШAēЬе/]рK\Ы9eM[tЗЙ_xвGwESТэ)Fmiъ$~Чц^cНмйyД6}&gYL?X"""

    fun encrypt(input: String, key: String): String {
        val fullKey: String = key.repeat(input.length / key.length) + key.dropLast(key.length - (input.length % key.length))
        var encryptedString = ""

        for (i in 0..<input.length) {
            if (!ALPHABET.contains(fullKey[i]) || !ALPHABET.contains(input[i])) {
                encryptedString += input[i]
                continue
            }
            encryptedString += ALPHABET[(ALPHABET.indexOf(input[i]) + ALPHABET.indexOf(fullKey[i])) % ALPHABET.length]
        }

        return encryptedString
    }

    fun decrypt(input: String, key: String): String {
        val fullKey: String = key.repeat(input.length / key.length) + key.dropLast(key.length - (input.length % key.length))
        var decryptedString = ""

        for (i in 0..<input.length) {
            if (!ALPHABET.contains(fullKey[i]) || !ALPHABET.contains(input[i])) {
                decryptedString += input[i]
                continue
            }
            decryptedString += ALPHABET[(ALPHABET.indexOf(input[i]) - ALPHABET.indexOf(fullKey[i]) + ALPHABET.length) % ALPHABET.length]
        }

        return decryptedString
    }
}