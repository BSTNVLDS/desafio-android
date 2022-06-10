package cl.accenture.githubjavapop.model

sealed class ApiState<T, Q> {
    class Success<T, Q>(val value: T) : ApiState<T, Q>()
    class Error<T, Q>(val error: Q) : ApiState<T, Q>()
    class Loading<T, Q> : ApiState<T, Q>()
}