package com.tomash.androidcontacts.contactgetter.acresult

/**
 * Result of doing some contact related work
 * Main contract is:
 * If everything goes fine - [onCompleted] is called
 * If something errors - [onFailure] is called
 * In any case [doFinally] is called
 * For any result [onResult] is called
 * So you are guarantied to get or [onCompleted] or [onFailure]
 */
class ACResult<Result, Exception> {
    private var doFinally: () -> Unit = {}
    private var onResult: (Result) -> Unit = {}
    private var onCompleted: () -> Unit = { doFinally() }
    private var onFailure: (Exception) -> Unit = { doFinally() }

    /**
     * Can be called multiple times with all received results
     * It could be called if exception occurred and operation was done partially
     */
    fun onResult(func: (Result) -> Unit) {
        onResult = func
    }

    /**
     * Called when some kind of exception is happened during operation
     */
    fun onFailure(func: (Exception) -> Unit) {
        onFailure = {
            func(it)
            doFinally()
        }
    }

    /**
     * Called when operation is completed fully successfully
     */
    fun onCompleted(func: () -> Unit) {
        onCompleted = {
            func()
            doFinally()
        }
    }

    /**
     * Called anyway when operation is finished
     */
    fun doFinally(func: () -> Unit) {
        doFinally = func
    }

    private fun result(data: Result) {
        onResult(data)
    }

    private fun failure(data: Exception) {
        onFailure(data)
    }

    private fun completed() {
        onCompleted()
    }

    companion object {
        internal fun <Result, Exception> (ACResult<Result, Exception>.() -> Unit).failure(data: Exception) =
            ACResult<Result, Exception>().apply(this).failure(data)

        internal fun <Result, Exception> (ACResult<Result, Exception>.() -> Unit).result(data: Result) =
            ACResult<Result, Exception>().apply(this).result(data)

        internal fun <Result, Exception> (ACResult<Result, Exception>.() -> Unit).completed() =
            ACResult<Result, Exception>().apply(this).completed()
    }
}
