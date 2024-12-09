package com.example.quizcue.presentation.tools

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.quizcue.R


@Composable
fun ProfileImage(photo: Bitmap?) {
    Image(
        modifier = Modifier
            .padding(top = 15.dp)
            .size(130.dp)
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
                CircleShape
            )
            .clip(CircleShape),
        painter = photo?.let { BitmapPainter(it.asImageBitmap()) }
            ?: painterResource(id = R.drawable.koshka),
        contentDescription = "User Image",
        contentScale = ContentScale.Crop,
        colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0.5f) })
    )
}