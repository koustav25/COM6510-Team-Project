import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme

@Composable
fun Settings() {
    var uiThemeSwitch by remember { mutableStateOf(false) }
    var themeText by remember { mutableStateOf("Light Mode") }

    if(uiThemeSwitch){
        MyApplicationTheme(true){

        }
    }
    else{
        MyApplicationTheme(false){

        }
    }
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Text(themeText)
            Switch(
                checked = uiThemeSwitch,
                onCheckedChange = {
                    uiThemeSwitch = it
                    themeText = if (it) "Dark Mode" else "Light Mode"
                })
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun Preview(){
    Settings()
}
