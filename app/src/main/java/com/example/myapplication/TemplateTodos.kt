package com.example.myapplication

import java.time.LocalDate

data class TemplateTodos(
    val taskName: String,
    var todoTitle: String,
    var todoDescription: String,
    var date: String,
    var isFavorite: Boolean,
    var isImportant: Boolean,
    var scheduledDate: String,
    var scheduledTime: String,
    var isFinished: Boolean,
    var isDeleted: Boolean,
    var subTaskDetails: List<TemplateSubtaskTodos>
)

fun TemplateTodosData() : List<TemplateTodos>{
    return listOf(
        TemplateTodos(
            "Select a task",
            "",
            "",
            LocalDate.now().toString(),
            false,
            false,
            "null",
            "null",
            false,
            false,
            TemplateSubtaskTodosDataNull()
        ),
        TemplateTodos(
            "Task 1",
            "Template Todo1",
            "This is template Todo description",
            LocalDate.now().toString(),
            false,
            false,
            "null",
            "null",
            false,
            false,
            TemplateSubtaskTodosData1()
        ),
        TemplateTodos(
            "Task 2",
            "Template Todo2",
            "This is template Todo2 description",
            LocalDate.now().toString(),
            false,
            false,
            "null",
            "null",
            false,
            false,
            TemplateSubtaskTodosData2()
        ),
        TemplateTodos(
            "Task 3",
            "Template Todo3",
            "This is template Todo3 description",
            LocalDate.now().toString(),
            false,
            false,
            "null",
            "null",
            false,
            false,
            TemplateSubtaskTodosData3()
        ),
        TemplateTodos(
            "Task 4",
            "Template Todo4",
            "This is template Todo4 description",
            LocalDate.now().toString(),
            false,
            false,
            "null",
            "null",
            false,
            false,
            TemplateSubtaskTodosData4()
        )
    )
}


data class TemplateSubtaskTodos(
    var subtaskTitle: String,
    var subtaskScheduledDate: String,
    var subtaskScheduledTime: String,
    var isSubtaskCompleted: Boolean
)

fun TemplateSubtaskTodosDataNull() : List<TemplateSubtaskTodos>{
    return listOf(
        TemplateSubtaskTodos(
            "No subtasks",
            "null",
            "null",
            false
        )
    )
}

fun TemplateSubtaskTodosData1() : List<TemplateSubtaskTodos>{
    return listOf(
        TemplateSubtaskTodos(
            "Subtask 1 of task 1",
            "null",
            "null",
            false
        ),
        TemplateSubtaskTodos(
            "Subtask 2 of task 1",
            "null",
            "null",
            false
        ),
        TemplateSubtaskTodos(
            "Subtask 3 of task 1",
            "null",
            "null",
            false
        ),
        TemplateSubtaskTodos(
            "Subtask 4 of task 1",
            "null",
            "null",
            false
        )
    )
}

//2
fun TemplateSubtaskTodosData2() : List<TemplateSubtaskTodos>{
    return listOf(
        TemplateSubtaskTodos(
            "Subtask 1 of task 2",
            "null",
            "null",
            false
        ),
        TemplateSubtaskTodos(
            "Subtask 2 of task 2",
            "null",
            "null",
            false
        ),
        TemplateSubtaskTodos(
            "Subtask 3 of task 2",
            "null",
            "null",
            false
        ),
        TemplateSubtaskTodos(
            "Subtask 4 of task 2",
            "null",
            "null",
            false
        )
    )
}

//3
fun TemplateSubtaskTodosData3() : List<TemplateSubtaskTodos>{
    return listOf(
        TemplateSubtaskTodos(
            "Subtask 1 of task 3",
            "null",
            "null",
            false
        ),
        TemplateSubtaskTodos(
            "Subtask 2 of task 3",
            "null",
            "null",
            false
        ),
        TemplateSubtaskTodos(
            "Subtask 3 of task 3",
            "null",
            "null",
            false
        ),
        TemplateSubtaskTodos(
            "Subtask 4 of task 4",
            "null",
            "null",
            false
        )
    )
}

//4
fun TemplateSubtaskTodosData4() : List<TemplateSubtaskTodos>{
    return listOf(
        TemplateSubtaskTodos(
            "Subtask 1 of task 4",
            "null",
            "null",
            false
        ),
        TemplateSubtaskTodos(
            "Subtask 2 of task 4",
            "null",
            "null",
            false
        ),
        TemplateSubtaskTodos(
            "Subtask 3 of task 4",
            "null",
            "null",
            false
        ),
        TemplateSubtaskTodos(
            "Subtask 4 of task 4",
            "null",
            "null",
            false
        )
    )
}