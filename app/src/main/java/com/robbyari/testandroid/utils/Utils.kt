package com.robbyari.testandroid.utils

object Utils {
    fun getLastName(path: String): String {
        return path.split(" / ").last()
    }
}