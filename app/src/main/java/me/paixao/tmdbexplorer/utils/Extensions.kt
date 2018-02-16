package me.paixao.tmdbexplorer.utils

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)

fun <T> MutableList<T>.addAllIfNotIn(elements: Collection<T>) {
    for (elem: T in elements)
        if (!this.contains(elem))
            add(elem)
}