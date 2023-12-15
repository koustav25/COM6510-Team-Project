package com.example.myapplication.ui.theme

enum class Priority {
    NULL, LOW, STANDARD, HIGH
}

data class PriorityTodos(
    val priorityName: String,
    val priority: Priority
)

//Priority categories with Name and Enum value
fun PriorityTodosData(): List<PriorityTodos>{
    return listOf(
        PriorityTodos(priorityName = "STANDARD",
            priority = Priority.STANDARD
        ),
        PriorityTodos(priorityName = "LOW",
            priority = Priority.LOW
        ),
        PriorityTodos(priorityName = "HIGH",
            priority = Priority.HIGH
        )

    )
}