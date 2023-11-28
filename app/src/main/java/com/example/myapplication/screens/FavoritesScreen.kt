import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.screens.Todos
import com.example.myapplication.todoViewModels.TodoViewModel

@Composable
fun Favorites(){
    val todoViewModel: TodoViewModel = viewModel()
    LazyColumn(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxSize())
    {
        item {
            if (todoViewModel.isFavoritesEmpty()) {
                Text(text = "No Favorite todos")
            } else {
                Todos(todo = todoViewModel.favoriteTodos, todoViewModel)
            }
        }
    }
}