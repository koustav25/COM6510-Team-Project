package com.example.myapplication.function

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
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
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.example.myapplication.R
import java.io.File


object Camera{
    fun checkCameraPermission(context: Context): Boolean {
        return PackageManager.PERMISSION_GRANTED ==
                ActivityCompat.checkSelfPermission(
                    context.applicationContext, android.Manifest.permission.CAMERA
                )
    }

    @SuppressLint("SuspiciousIndentation")
    @Composable
    fun RunCamera(context: Context) {
        var hasCameraPermission by remember {
            mutableStateOf(false)
        }
        if (checkCameraPermission(context)) {
            hasCameraPermission = true
        } else {
            val requestPermissionLaucher =
                rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted: Boolean ->
                    hasCameraPermission = isGranted
                }
            SideEffect {
                requestPermissionLaucher.launch(Manifest.permission.CAMERA)
            }
        }
        var cameraImageUri by remember {
            mutableStateOf<Uri?>(null)
        }
        var cameraIamgeBitmap by remember {
            mutableStateOf<ImageBitmap?>(null)
        }
        val imageFromCameraLaucher = rememberLauncherForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { captured ->
            if (!captured) {
                cameraIamgeBitmap = null
                cameraImageUri = null
            } else {
                cameraIamgeBitmap = getImageBitmap(cameraImageUri,context)?.asImageBitmap()
            }
            Log.i("picture_eg", "$captured $cameraImageUri")
        }
        IconButton(onClick ={
                    if (hasCameraPermission) {
                    cameraIamgeBitmap = null
                    cameraImageUri = FileProvider.getUriForFile(
                        context.applicationContext, context.applicationContext.getPackageName() + ".provider", newImageFile(context)
                    )
                    imageFromCameraLaucher.launch(cameraImageUri)
                    }
                }){
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .size(10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(painter= painterResource(R.drawable.baseline_camera_alt_24), contentDescription = "camera")
                        Text("Camera", fontSize = 8.sp)
                    }
                }
    }

    fun getImageBitmap(uri: Uri?,context: Context): Bitmap? {
        var result: Bitmap? = null
        if(uri != null){
            val contentResolver: ContentResolver = context.getContentResolver()
            result = ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver,uri))
        }
        return result
    }

    private fun newImageFile(context: Context): File {
        val timeMillis = System.currentTimeMillis().toString()
        val storageDir = context.applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("SNAP-$timeMillis",".jpg",storageDir)
    }
}