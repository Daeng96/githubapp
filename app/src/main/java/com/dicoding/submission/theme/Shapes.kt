package com.dicoding.submission.theme

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val TopShape = Shapes().medium.copy(
	topStart = CornerSize(16.dp),
	topEnd = CornerSize(16.dp),
	bottomStart = CornerSize(0.dp),
	bottomEnd = CornerSize(0.dp),
)
val BottomShape = Shapes().medium.copy(
	topStart = CornerSize(0),
	topEnd = CornerSize(0),
	bottomStart = CornerSize(10),
	bottomEnd = CornerSize(10)
)