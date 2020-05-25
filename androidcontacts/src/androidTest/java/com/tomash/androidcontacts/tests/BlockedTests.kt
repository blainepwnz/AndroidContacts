package com.tomash.androidcontacts.tests

import androidx.test.platform.app.InstrumentationRegistry
import com.tomash.androidcontacts.BaseTest
import com.tomash.androidcontacts.contactgetter.main.blocked.BlockedContactsManager
import com.tomash.androidcontacts.utils.TelecomTestUtils
import com.tomash.androidcontacts.utils.context
import com.tomash.androidcontacts.utils.randomString
import org.junit.Assert
import org.junit.Assert.fail
import org.junit.Test

class BlockedTests : BaseTest() {

    @Test
    fun correctlyBlocksContact() {
        test {
            val number = randomString()
            block(number) {
                fail("Should not fail")
            }
            val blockedNumbers = getBlockedNumbers {
                fail("Should not fail")
            }
            Assert.assertEquals(1, blockedNumbers.size)
            Assert.assertTrue(blockedNumbers.first() == number)
        }
    }

    @Test
    fun throwsExceptionIfNotDefaultDialer() {
        test(setAsDefaultDialer = false) {
            var exception: Exception? = null
            getBlockedNumbers {
                exception = it
            }
            Assert.assertNotNull(exception)
            Assert.assertEquals(exception?.javaClass, SecurityException::class.java)
        }
    }

    @Test
    fun correctlyUnblocksContact() {
        correctlyBlocksContact()
        test(clearAllBlockedContacts = false) {
            val availableNumber = getBlockedNumbers().first()
            unblock(availableNumber)
            val allBlockedNumbers = getBlockedNumbers()
            Assert.assertTrue(allBlockedNumbers.isEmpty())
        }
    }

    @Test
    fun doesNothingIfTriesToUnblockUnknownNumber() {
        test {
            val number = randomString()
            block(number)
            unblock(number + "123") {
                fail("Should not fail")
            }
            Assert.assertEquals(1, getBlockedNumbers().size)
            Assert.assertTrue(getBlockedNumbers().first() == number)
        }
    }

    @Test
    fun doesNotAddSameNumberTwoTimes() {
        test {
            val number = randomString()
            block(number)
            block(number)
            Assert.assertEquals(1, getBlockedNumbers().size)
        }
    }

    private fun test(setAsDefaultDialer: Boolean = true,
        clearAllBlockedContacts: Boolean = true,
        testFunc: BlockedContactsManager.() -> Unit) {
        val blockedContactsManager = BlockedContactsManager(context)
        if (setAsDefaultDialer) {
            TelecomTestUtils.setDefaultDialer(InstrumentationRegistry.getInstrumentation(), context.packageName)
            if (clearAllBlockedContacts) {
                blockedContactsManager.getBlockedNumbers().forEach {
                    blockedContactsManager.unblock(it)
                }
            }
        } else {
            TelecomTestUtils.setDefaultDialer(InstrumentationRegistry.getInstrumentation(), context.packageName + "1234")
        }
        blockedContactsManager.testFunc()
    }
}