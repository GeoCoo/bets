package com.betson.interviewTest.core_tests.helpers

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.rules.TestWatcher

class CoroutineTestRule constructor(
    private val testDispatcher: TestDispatcher = StandardTestDispatcher(),
    val testScope: TestScope = TestScope(testDispatcher)
) : TestWatcher() {
    fun cancelScopeAndDispatcher() {
        testScope.cancel()
        testDispatcher.cancel()
    }
}

fun CoroutineTestRule.runTest(block: suspend CoroutineScope.() -> Unit): Unit =
    testScope.runTest { block() }

/**
 * This function starts collecting events from the flow.
 * Then cancelAndConsumeRemainingEvents function cancels the flow and consumes any remaining events in the flow, which is important to prevent any possible leaks.
 */
suspend fun <T> Flow<T>.runFlowTest(block: suspend ReceiveTurbine<T>.() -> Unit) {
    test {
        block()
        cancelAndConsumeRemainingEvents()
    }
}

fun <T> T.toFlow(): StateFlow<T> =
    MutableStateFlow(this).asStateFlow()