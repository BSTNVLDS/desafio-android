package cl.accenture.githubjavapop.model

import java.util.*

sealed class ApiState<T> {
    class  Success<T>(val value :T):ApiState<T>()
    class Error<T>(val error:Throwable):ApiState<T>()
    class Loading<T>:ApiState<T>()
}