package com.tomash.androidcontacts.utils

import java.security.SecureRandom

private const val AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
private val rnd = SecureRandom()

@JvmOverloads
fun randomString(len: Int = 7): String {
    val sb = StringBuilder(len)
    for (i in 0 until len) sb.append(AB[rnd.nextInt(AB.length)])
    return sb.toString()
}
