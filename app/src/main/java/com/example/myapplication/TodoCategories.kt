package com.example.myapplication

data class TodoCategories(
    val todoCategories: String
)

fun TodoCategoryData() : List<TodoCategories>{
    return listOf(
        TodoCategories(
            "Today"
        ),
        TodoCategories(
            "Scheduled"
        ),
        TodoCategories(
            "All"
        ),
        TodoCategories(
            "Important"
        ),
        TodoCategories(
            "Finished"
        ),
        TodoCategories(
            "Bin"
        )
    )
}
