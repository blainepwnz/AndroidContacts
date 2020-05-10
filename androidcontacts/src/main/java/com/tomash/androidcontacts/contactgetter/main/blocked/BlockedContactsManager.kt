package com.tomash.androidcontacts.contactgetter.main.blocked

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.BlockedNumberContract.BlockedNumbers
import android.support.annotation.RequiresApi

/**
 * Used to get blocked contacts.
 * Can be used only starting from android N.
 * Your app should be default dialer to use this, or it will fail with [SecurityException].
 */
class BlockedContactsManager(private val ctx: Context) {

    /**
     * Gets all blocked contacts.
     * I feel like you are safe to think, that all numbers in that list are unique, but it is handled on android side.
     * In case of error, it can be handled in [onError], and will return empty list.
     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun getBlockedNumbers(onError: (Exception) -> Unit = {}): List<String> {
        return try {
            ctx.contentResolver.query(BlockedNumbers.CONTENT_URI,
                arrayOf(BlockedNumbers.COLUMN_ORIGINAL_NUMBER), null, null, null)?.run {
                val result = mutableListOf<String>()
                while (moveToNext()) {
                    getString(getColumnIndex(BlockedNumbers.COLUMN_ORIGINAL_NUMBER))?.let {
                        result.add(it)
                    }
                }
                result
            } ?: emptyList()
        } catch (exception: Exception) {
            onError(exception)
            emptyList()
        }
    }

    /**
     * Blocks contact.
     * Does not blocks same number two times.
     * In case of error, it can be handled in [onError], and will return empty list.
     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun block(number: String, onError: (Exception) -> Unit = {}) {
        val values = ContentValues()
        values.put(BlockedNumbers.COLUMN_ORIGINAL_NUMBER, number)
        try {
            ctx.contentResolver.insert(BlockedNumbers.CONTENT_URI, values)
        } catch (exception: Exception) {
            onError(exception)
        }
    }

    /**
     * Unblocks contact.
     * Does nothing if this number is not blocked.
     * In case of error, it can be handled in [onError], and will return empty list.
     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun unblock(number: String, onError: (Exception) -> Unit = {}) {
        val values = ContentValues()
        values.put(BlockedNumbers.COLUMN_ORIGINAL_NUMBER, number)
        try {
            ctx.contentResolver.insert(BlockedNumbers.CONTENT_URI, values)?.let { uri ->
                ctx.contentResolver.delete(uri, null, null)
            }
        } catch (exception: Exception) {
            onError(exception)
        }
    }
}