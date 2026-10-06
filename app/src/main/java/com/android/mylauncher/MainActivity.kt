package com.android.mylauncher

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
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyLauncherTheme {
                Greeting()
            }
        }
    }
}
data class AppInfo(val name: String, val icon: Drawable, val packageName: String)
@Composable
fun Greeting() {
    var showsettings by remember { mutableStateOf(false) }

        val context = LocalContext.current
        val pm = context.packageManager
        val prefs = context.getSharedPreferences("launcher", Context.MODE_PRIVATE)
        var searchText by remember { mutableStateOf("") }
        var wallpaperUri by remember {
            mutableStateOf(
                prefs.getString("wallpapers", null)?.let { Uri.parse(it) })
        }
        val wallpaperBitmap = remember(wallpaperUri) {
            wallpaperUri?.let { uri ->
                runCatching {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap(
                            ImageDecoder.createSource(
                                context.contentResolver,
                                uri
                            )
                        )
                    } else {
                        null
                    }
                }.getOrNull()
            }
        }

        val wallpaperPicker = rememberLauncherForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) { uri ->
            wallpaperUri = uri

            uri?.let {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                prefs.edit()
                    .putString("wallpapers", it.toString())
                    .apply()
            }
        }
    val apps = remember {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        pm.queryIntentActivities(
            intent,
            PackageManager.MATCH_ALL
        )
    }
    val applist = apps.map {
        AppInfo(
            it.loadLabel(pm).toString(),
            icon = it.loadIcon(pm),
            packageName = it.activityInfo.packageName
        )
    }.sortedBy { it.name.lowercase() }
    val Filteredapps = remember(searchText, applist) {
        applist.filter {
            it.name.contains(
                searchText,
                ignoreCase = true
            )
        }
    }

    if (showsettings) {
        SettingsScreen(wallpaperBitmap = wallpaperBitmap, onPickWallpaper = {
            wallpaperPicker.launch(arrayOf("image/*"))
        })
    }else{
        Box(modifier = Modifier.fillMaxSize()) {
            wallpaperBitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Button(
                onClick = {
                    showsettings = true
                }, modifier = Modifier
                    .offset(x = 300.dp, y = 10.dp)
                    .size(width = 50.dp, height = 50.dp)


            ) {
                Text(text = "⚙️", fontSize = 20.sp, modifier = Modifier.offset(x = -20.dp))
            }
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("Search Apps here") },
                modifier = Modifier
                    .offset(x = 5.dp, y = 10.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .size(width = 300.dp, height = 50.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                modifier = Modifier
                    .padding(top = 90.dp),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(Filteredapps) { app ->
                    val bitmap = remember(app.packageName) {
                        drawableToBitmap(app.icon).asImageBitmap()
                    }
                    var offset by remember { mutableStateOf(Offset.Zero) }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            val launchIntent = pm.getLaunchIntentForPackage(app.packageName)
                            launchIntent?.let { context.startActivity(it) }
                        }) {
                        Image(
                            bitmap = bitmap,
                            contentDescription = app.name,
                            modifier = Modifier.size(60.dp).clip(RoundedCornerShape(123.dp))
                        )
                        Text(text = app.name, fontSize = 12.sp, maxLines = 2)
                    }


                }
            }
        }
    }


    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        val width = if (drawable.intrinsicWidth > 0) drawable.intrinsicWidth else 100
        val height = if (drawable.intrinsicHeight > 0) drawable.intrinsicHeight else 100
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyLauncherTheme {
        Greeting()
    }
}
