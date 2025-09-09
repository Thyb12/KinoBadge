package fr.thibaut.kinobadge

data class Result<T> (
    val success: T? = null,
    val error: String? = null
)
