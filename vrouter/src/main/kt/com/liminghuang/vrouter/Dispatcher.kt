package com.liminghuang.vrouter

import android.util.Log
import java.util.*
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * ProjectName: AOP
 * Description: 路由[Call]分发器.
 * CreateDate: 2020/7/11 8:17 PM
 * @author: <a href="mailto:liming.huang@tuya.com">colin</a>
 * @version: 3.19.0
 * @since: 3.19.0
 */
class Dispatcher internal constructor() {
    init {
        // 初始化块中的代码实际上会成为主构造函数的一部分
        Log.d(TAG, "Dispatcher init.")
    }

    companion object {
        const val TAG: String = "Dispatcher"
    }

    /** 并发请求最大值.  */
    @get:Synchronized
    var maxRequests = 64
        set(maxRequests) {
            require(maxRequests >= 1) { "max < 1: $maxRequests" }
            // 注意这里使用的是内敛函数synchronized来实现的对代码块加锁
            synchronized(this) {
                // 幕后字段
                field = maxRequests
            }
            promoteAndExecute()
        }

    /** 同个域名服务的并发请求最大值.  */
    @get:Synchronized
    var maxRequestsPerHost = 5
        set(maxRequestsPerHost) {
            require(maxRequestsPerHost >= 1) { "max < 1: $maxRequestsPerHost" }
            synchronized(this) {
                field = maxRequestsPerHost
            }
            promoteAndExecute()
        }

    @set:Synchronized
    @get:Synchronized
    var idleCallback: Runnable? = null

    private val sThreadFactory: ThreadFactory = object : ThreadFactory {
        private val seq = AtomicInteger(0)

        override fun newThread(r: Runnable): Thread {
            return Thread(r, "VRouter Dispatcher-" + seq.getAndIncrement().toString())
        }
    }.apply { Log.d(TAG, "sThreadFactory created.") }

    private var executorServiceOrNull: ExecutorService? = null

    @get:Synchronized
    // 告诉编译器生成的字段的名称
    @get:JvmName("executorService")
    val executorService: ExecutorService
        get() {
            if (executorServiceOrNull == null) {
                // 执行请求任务的线程池：无核心线程，线程池量是无界（Integer.Max_VALUE可认定为无界），线程空闲保留60s后无任务则销毁，采用同步队列（无存储功能），总结这是一个CachedThreadPool
                executorServiceOrNull = ThreadPoolExecutor(0, Int.MAX_VALUE, 60, TimeUnit.SECONDS,
                        SynchronousQueue(), sThreadFactory)
            }
            return executorServiceOrNull!!
        }

    /** Ready async calls in the order they'll be run. */
    private val readyAsyncCalls = ArrayDeque<RealCall.AsyncCall>()

    /** Running asynchronous calls. Includes canceled calls that haven't finished yet. */
    private val runningAsyncCalls = ArrayDeque<RealCall.AsyncCall>()

    /** Running synchronous calls. Includes canceled calls that haven't finished yet. */
    private val runningSyncCalls = ArrayDeque<RealCall>()

    /**
     * 次构造函数，且每个次构造函数必须要委托给主构造函数.
     */
    constructor(executorService: ExecutorService) : this() {
        this.executorServiceOrNull = executorService
    }

    /**
     * 异步执行的任务入队处理
     */
    internal fun enqueue(call: RealCall.AsyncCall) {
        synchronized(this) {
            readyAsyncCalls.add(call)
        }
        promoteAndExecute()
    }

    private fun findExistingCallWithHost(host: String): RealCall.AsyncCall? {
        for (existingCall in runningAsyncCalls) {
            if (existingCall.host == host) return existingCall
        }
        for (existingCall in readyAsyncCalls) {
            if (existingCall.host == host) return existingCall
        }
        return null
    }

    /**
     * Promotes eligible calls from [readyAsyncCalls] to [runningAsyncCalls] and runs them on the
     * executor service. Must not be called with synchronization because executing calls can call
     * into user code.
     *
     * @return true if the dispatcher is currently running calls.
     */
    private fun promoteAndExecute(): Boolean {
        this.assertThreadDoesntHoldLock()

        val executableCalls = mutableListOf<RealCall.AsyncCall>()
        val isRunning: Boolean
        synchronized(this) {
            val i = readyAsyncCalls.iterator()
            while (i.hasNext()) {
                val asyncCall = i.next()
                // 异步任务并发总量未超过maxRequests,（默认64，可自定义），并且同域名的异步请求未超过maxRequestsPerHost（默认5，可自定义），则直接执行。否则暂存到readyAsyncCalls队列
                if (runningAsyncCalls.size >= this.maxRequests) break // Max capacity.
                if (asyncCall.callsPerHost.get() >= this.maxRequestsPerHost) continue // Host max capacity.

                i.remove()
                asyncCall.callsPerHost.incrementAndGet()
                executableCalls.add(asyncCall)
                runningAsyncCalls.add(asyncCall)
            }
            // 执行中的请求（不管同步或者异步）数量
            isRunning = runningCallsCount() > 0
        }

        for (i in 0 until executableCalls.size) {
            val asyncCall = executableCalls[i]
            asyncCall.executeOn(executorService)
        }

        return isRunning
    }

    /** Used by [Call.execute] to signal it is in-flight. */
    @Synchronized
    internal fun executed(call: RealCall) {
        runningSyncCalls.add(call)
    }

    /** Used by [AsyncCall.run] to signal completion. */
    internal fun finished(call: RealCall.AsyncCall) {
        call.callsPerHost.decrementAndGet()
        finished(runningAsyncCalls, call)
    }

    /** Used by [Call.execute] to signal completion. */
    internal fun finished(call: RealCall) {
        finished(runningSyncCalls, call)
    }

    private fun <T> finished(calls: Deque<T>, call: T) {
        val idleCallback: Runnable?
        synchronized(this) {
            if (!calls.remove(call)) throw AssertionError("Call wasn't in-flight!")
            idleCallback = this.idleCallback
        }

        val isRunning = promoteAndExecute()

        // 所有同步或异步请求已执行完成，标记空闲，执行空闲任务。有点messageQueue空闲任务的意思
        if (!isRunning && idleCallback != null) {
            idleCallback.run()
        }
    }

    /** Returns a snapshot of the calls currently awaiting execution. */
    @Synchronized
    fun queuedCalls(): List<Call> {
        return Collections.unmodifiableList(readyAsyncCalls.map { it.call })
    }

    /** Returns a snapshot of the calls currently being executed. */
    @Synchronized
    fun runningCalls(): List<Call> {
        return Collections.unmodifiableList(runningSyncCalls + runningAsyncCalls.map { it.call })
    }

    @Synchronized
    fun queuedCallsCount(): Int = readyAsyncCalls.size

    @Synchronized
    fun runningCallsCount(): Int = runningAsyncCalls.size + runningSyncCalls.size

    @JvmName("-deprecated_executorService")
    @Deprecated(
            message = "moved to val",
            replaceWith = ReplaceWith(expression = "executorService"),
            level = DeprecationLevel.ERROR)
    fun executorService(): ExecutorService = executorService
}