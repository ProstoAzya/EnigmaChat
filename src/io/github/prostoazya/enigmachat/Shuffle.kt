package io.github.prostoazya.enigmachat

import java.nio.ByteBuffer
import java.security.MessageDigest
import kotlin.random.Random

object Shuffle {
    fun shuffle(list: MutableList<Char>, seedString: String): MutableList<Char> {
        val seed = sha256Seed(seedString)
        val random = Random(seed)

        for (i in list.size - 1 downTo 1) {
            val j = random.nextInt(i + 1)
            val temp = list[i]
            list[i] = list[j]
            list[j] = temp
        }

        return list
    }

    private fun sha256Seed(input: String): Long {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(input.toByteArray(Charsets.UTF_8))

        return ByteBuffer.wrap(hashBytes).long
    }
}