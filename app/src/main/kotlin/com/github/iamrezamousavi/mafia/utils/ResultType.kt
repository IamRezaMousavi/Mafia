package com.github.iamrezamousavi.mafia.utils

import java.io.Serializable

sealed class ResultType<out T, out E> : Serializable {

    internal data class Success<out T>(val value: T) : ResultType<T, Nothing>()

    internal data class Error<out E>(val error: E) : ResultType<Nothing, E>()

    val isSuccess: Boolean
        get() = this is Success

    val isError: Boolean
        get() = this is Error

    fun onSuccess(action: (value: T) -> Unit): ResultType<T, E> {
        when (this) {
            is Success -> action(value)
            is Error -> Unit
        }
        return this
    }

    fun onError(action: (error: E) -> Unit): ResultType<T, E> {
        when (this) {
            is Success -> Unit
            is Error -> action(error)
        }
        return this
    }

    override fun toString(): String = when (this) {
        is Success -> "Success($value)"
        is Error -> "Error($error)"
    }

    companion object {

        fun <T> success(value: T): ResultType<T, Nothing> = Success(value)

        fun <E> error(error: E): ResultType<Nothing, E> = Error(error)
    }
}
