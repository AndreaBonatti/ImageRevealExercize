package com.andreabonatti92.imagerevealexercize

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // bmp
            val marvelSnap = ImageBitmap.imageResource(id = R.drawable.marvel_snap)

            // circle data
            var circleCenter by remember {
                mutableStateOf(Offset.Zero)
            }
            var oldCircleCenter by remember {
                mutableStateOf(Offset.Zero)
            }
            val radius = 200f

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(true) {
                        // handle pointer gestures
                        detectDragGestures(
                            // the take hte last dragged position as new center of the circle
                            onDragEnd = {
                                oldCircleCenter = circleCenter
                            }
                        ) { change, _ ->
                            circleCenter = oldCircleCenter + change.position
                        }
                    }
            ) {
                // draw bmp
                val bmpHeight =
                    ((marvelSnap.height.toFloat() / marvelSnap.width.toFloat()) * size.width).roundToInt()
                drawImage(
                    image = marvelSnap,
                    dstOffset = IntOffset(0, center.y.roundToInt() - bmpHeight / 2),
                    dstSize = IntSize(
                        size.width.roundToInt(),
                        bmpHeight
                    ),
                    // BW Filter
                    colorFilter = ColorFilter.tint(Color.Black, BlendMode.Color)
                )

                // draw circle
                val circlePath = Path().apply {
                    addArc(
                        oval = Rect(center = circleCenter, radius = radius),
                        startAngleDegrees = 0f,
                        sweepAngleDegrees = 360f
                    )
                }
                // clip with intersect to reveal the image color
                clipPath(path = circlePath, clipOp = ClipOp.Intersect) {
                    drawImage(
                        image = marvelSnap,
                        dstSize = IntSize(
                            size.width.roundToInt(),
                            bmpHeight
                        ),
                        dstOffset = IntOffset(0, center.y.roundToInt() - bmpHeight / 2),
                    )
                }
            }
        }
    }
}