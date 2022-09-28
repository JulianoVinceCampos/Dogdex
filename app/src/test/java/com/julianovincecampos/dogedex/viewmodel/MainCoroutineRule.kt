package com.julianovincecampos.dogedex.viewmodel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.rules.TestWatcher
import org.junit.runner.Description

//@ExperimentalCoroutinesApi
//class MainCoroutineRule(val dispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()) :
//    TestWatcher(),
//    TestCoroutineScope by TestCoroutineScope(dispatcher) {
//    override fun starting(description: Description?) {
//        super.starting(description)
//        Dispatchers.setMain(dispatcher)
//    }
//
//    override fun finished(description: Description?) {
//        super.finished(description)
//        cleanupTestCoroutines()
//        Dispatchers.resetMain()
//    }
//}

@ExperimentalCoroutinesApi
class MainCoroutineRule(
    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
) : TestWatcher() {

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    fun runBlockingTest(
        block: suspend TestCoroutineScope.() -> Unit
    ): Unit = testDispatcher.runBlockingTest(block)
}
