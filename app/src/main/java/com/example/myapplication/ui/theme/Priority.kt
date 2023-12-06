package com.example.myapplication.ui.theme

enum class Priority {
    NULL, LOW, STANDARD, HIGH
}

data class PriorityTodos(
    val priorityName: String,
    val priority: Priority
)

fun PriorityTodosData(): List<PriorityTodos>{
    return listOf(
        PriorityTodos(priorityName = "Select a priority",
            priority = Priority.NULL
        ),
        PriorityTodos(priorityName = "HIGH",
            priority = Priority.HIGH
        ),
        PriorityTodos(priorityName = "STANDARD",
            priority = Priority.STANDARD
        ),
        PriorityTodos(priorityName = "LOW",
            priority = Priority.LOW
        )

    )
}