package com.ys.basicandroid.utils.ext

fun Boolean?.orFalse(): Boolean = this ?: false

inline fun Long?.orZero(): Long = this ?: 0L
inline fun Double?.orZero(): Double = this ?: 0.0
inline fun Int?.orZero(): Int = this ?: 0
inline fun Float?.orZero(): Float = this ?: 0.0f

inline fun Long?.orDefault(default: Long): Long = this ?: default
inline fun Double?.orDefault(default: Double): Double = this ?: default
inline fun Int?.orDefault(default: Int): Int = this ?: default
inline fun Float?.orDefault(default: Float): Float = this ?: default