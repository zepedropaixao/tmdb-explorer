package me.paixao.tmdbexplorer.utils

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)

fun <T> MutableList<T>.addAllIfNotIn(elements: Collection<T>) {
    for (elem: T in elements)
        if (!this.contains(elem))
            add(elem)
}

fun <T> LiveData<T>.blockingObserve(): T? {
    var value: T? = null
    val latch = CountDownLatch(1)
    val innerObserver = Observer<T> {
        value = it
        latch.countDown()
    }
    observeForever(innerObserver)
    latch.await(2, TimeUnit.SECONDS)
    return value
}