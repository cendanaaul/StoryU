package com.cencen.storyu.data

sealed class Libraries<out R>private constructor() {
    class Success<out T>(val data: T): Libraries<T>()
    class Error(val error: String): Libraries<Nothing>()
    data object Loading: Libraries<Nothing>()
}
