package com.android.mylauncher

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import android.R
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.mylauncher.ui.theme.MyLauncherTheme
import androidx.compose.ui.unit.sp
import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.runtime.mutableStateOf
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.TextField
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import android.graphics.ImageDecoder
import android.provider.MediaStore
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.draw.clipToBounds


@Composable
fun SettingsScreen(wallpaperBitmap: Bitmap?, onPickWallpaper: () -> Unit) {

    var buttonColor by remember { mutableStateOf(Color.Green) }
    Box(modifier = Modifier.fillMaxSize()) {
            Button(
                onClick = { onPickWallpaper() },
                modifier = Modifier.offset(x = 100.dp, y = 20.dp)
                    .size(width = 200.dp, height = 50.dp)
            )
            {
                Text(text = "Change Wallpaper", fontSize = 15.sp, maxLines = 1)
            }
            Text(text = "Icon size", fontSize = 20.sp, maxLines = 1, modifier = Modifier.offset(x = 150.dp, y = 80.dp))

            Button(onClick = { buttonColor = Color.Red},colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor)
                    , shape = RoundedCornerShape(12.dp), modifier = Modifier.offset(x = 100.dp, y = 120.dp)
                 ){

            }






    }
}
