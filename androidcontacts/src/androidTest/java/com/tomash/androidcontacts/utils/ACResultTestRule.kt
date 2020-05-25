package com.tomash.androidcontacts.utils

import com.tomash.androidcontacts.contactgetter.acresult.ACResult
import junit.framework.TestCase.fail
import org.junit.Assert
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class ACResultTestRule : TestRule {

    private var uncalledCallbacks = 0

    override fun apply(base: Statement?, description: Description?) = object : Statement() {
        override fun evaluate() {
            base?.evaluate()
            if (uncalledCallbacks != 0) {
                fail("Remaining null callbacks = $uncalledCallbacks")
            }
        }
    }

    fun shouldComplete(): ACResult<*, *>.() -> Unit {
        uncalledCallbacks = uncalledCallbacks.inc()
        return {
            onCompleted {
                uncalledCallbacks = uncalledCallbacks.dec()
            }
            onFailure {
                Assert.fail("Should not call on failure")
            }
        }
    }

    fun shouldFail(): ACResult<*, *>.() -> Unit {
        uncalledCallbacks = uncalledCallbacks.inc()
        return {
            onFailure {
                uncalledCallbacks = uncalledCallbacks.dec()
            }
            onCompleted {
                Assert.fail("Should not call on completed")
            }
        }
    }

    fun <R, E> shouldFailWithError(expectedError: E): ACResult<R, E>.() -> Unit =
        wrap(
            {
                onFailure {
                    expectedError isEqual it
                }
            },
            shouldFail()
        )

    fun <R, E> shouldGetResult(expectedResult: R): ACResult<R, E>.() -> Unit = {
        onResult {
            expectedResult isEqual it
        }
    }

    fun shouldHaveNoResults(): ACResult<*, *>.() -> Unit = {
        onResult {
            Assert.fail("Should not call onResult, value = $it")
        }
    }
}

fun <R, E> wrap(vararg functions: ACResult<R, E>.() -> Unit): ACResult<R, E>.() -> Unit = {
    functions.forEach { it() }
}
