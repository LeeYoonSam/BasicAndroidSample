package com.ys.basicandroid.domain

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    object Loading : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            is Loading -> "Loading"
        }
    }
}

/**
 * Returns `true` if this instance represents a successful outcome.
 * In this case [isError] returns `false`.
 */
val Result<*>.isSuccess: Boolean get() = this is Result.Success && data != null

/**
 * Returns `true` if this instance represents a failed outcome.
 * In this case [isSuccess] returns `false`.
 */
val Result<*>.isError: Boolean get() = this is Result.Error

/**
 * if [Result.Success.data] exists return it or fallback
 */
inline fun <T> Result<T>.successOr(fallback: T): T {
    return (this as? Result.Success<T>)?.data ?: fallback
}

/**
 * Returns the encapsulated value if this instance represents [success][Result.Success] or the
 * [defaultValue] if it is [failure][Result.isFailure].
 */
inline fun <R, T : R> Result<T>.getOrDefault(defaultValue: R): R {
    return (this as? Result.Success<T>)?.data ?: defaultValue
}

/**
 * Returns [Result.data] or null
 */
val <T> Result<T>.data: T?
    get() = (this as? Result.Success)?.data

inline fun <R, T> Result<T>.map(transform: (T) -> R): Result<R> {
    return when (this) {
        is Result.Success -> Result.Success(transform(data))
        is Result.Error -> Result.Error(exception)
        Result.Loading -> Result.Loading
    }
}

inline fun <R, T> Result<T>.mapCatching(transform: (T) -> R): Result<R> {
    return when (this) {
        is Result.Success -> {
            try {
                Result.Success(transform(data))
            } catch (e: Throwable) {
                Result.Error(e)
            }
        }
        is Result.Error -> Result.Error(exception)
        Result.Loading -> Result.Loading
    }
}