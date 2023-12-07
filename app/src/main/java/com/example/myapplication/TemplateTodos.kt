package com.example.myapplication

import java.time.LocalDate
import java.time.LocalTime

data class TemplateTodos(
    val taskName: String,
    var todoTitle: String,
    var todoDescription: String,
    var date: String,
    var time: String,
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
            LocalTime.now().toString(),
            false,
            false,
            "null",
            "null",
            false,
            false,
            TemplateSubtaskTodosDataNull()
        ),
        TemplateTodos(
            "Build a PC",
            "Need to build a custom PC",
            "I need to buy certain parts to custom build a PC",
            LocalDate.now().toString(),
            LocalTime.now().toString(),
            false,
            false,
            "null",
            "null",
            false,
            false,
            TemplateSubtaskTodosData1()
        ),
        TemplateTodos(
            "Buy Groceries",
            "Buy groceries from Tesco",
            "List of grocery items to buy from Tesco",
            LocalDate.now().toString(),
            LocalTime.now().toString(),
            false,
            false,
            "null",
            "null",
            false,
            false,
            TemplateSubtaskTodosData2()
        ),
        TemplateTodos(
            "Dinner for 3",
            "Make dinner for 3 people",
            "Things to remember while making dinner for 3 people",
            LocalDate.now().toString(),
            LocalTime.now().toString(),
            false,
            false,
            "null",
            "null",
            false,
            false,
            TemplateSubtaskTodosData3()
        ),
        TemplateTodos(
            "Visit London",
            "Visit London",
            "Things to do while visiting London",
            LocalDate.now().toString(),
            LocalTime.now().toString(),
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
            "Buy a motherboard",
            "null",
            "null",
            false
        ),
        TemplateSubtaskTodos(
            "Buy Intel Core-I5 12400F",
            "null",
            "null",
            false
        ),
        TemplateSubtaskTodos(
            "Buy Nvidia RTX 4090 Ti",
            "null",
            "null",
            false
        ),
        TemplateSubtaskTodos(
            "Buy 64GB DDR5 RAM",
            "null",
            "null",
            false
        ),
        TemplateSubtaskTodos(
            "Buy 600W PSU",
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
            "Onions 3Kg",
            "null",
            "null",
            false
        ),
        TemplateSubtaskTodos(
            "Carrots 1Kg",
            "null",
            "null",
            false
        ),
        TemplateSubtaskTodos(
            "Tomatoes 2Kg",
            "null",
            "null",
            false
        ),
        TemplateSubtaskTodos(
            "Baby Mushrooms 200gm",
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
            "Make Butter chicken",
            "null",
            "null",
            false
        ),
        TemplateSubtaskTodos(
            "Make 20 Nan rotis",
            "null",
            "null",
            false
        ),
        TemplateSubtaskTodos(
            "Check for guest specific allergies",
            "null",
            "null",
            false
        ),
        TemplateSubtaskTodos(
            "Serve hot",
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
            "Visit the British Museum",
            "null",
            "null",
            false
        ),
        TemplateSubtaskTodos(
            "Bask in the glory of the London eye at Night",
            "null",
            "null",
            false
        ),
        TemplateSubtaskTodos(
            "Click a selfie on the Tower bridge",
            "null",
            "null",
            false
        ),
        TemplateSubtaskTodos(
            "Relax in Hyde park",
            "null",
            "null",
            false
        )
    )
}