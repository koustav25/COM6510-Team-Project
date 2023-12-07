package com.example.myapplication.function

import android.content.ContentResolver
import android.content.Context
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R


object Gallery {
    @Composable
    fun OpenGallery(context: Context, onImageSelected: (String?) -> Unit) {
        var pickedImageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
        val imageFromGalleryLaucher = rememberLauncherForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri: Uri? ->
            if (uri == null) {
                pickedImageBitmap = null
            } else {
                val contentResolver: ContentResolver = context.contentResolver
                pickedImageBitmap = ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(contentResolver, uri)
                ).asImageBitmap()
            }
            onImageSelected(uri.toString())
        }
        Column {
            IconButton(onClick = {
                imageFromGalleryLaucher.launch(
                    PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .size(10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_insert_photo_24),
                        contentDescription = "Search"
                    )
                    Text("Gallery", fontSize = 8.sp)
                }
            }
            pickedImageBitmap?.let{ imageBitmap ->
                Image(imageBitmap, null)
            }
        }
    }
}