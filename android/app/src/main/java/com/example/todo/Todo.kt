package com.example.todo

data class Todo(
    val id: Int,
    val text: String,
    var completed: Boolean = false
)
