package com.dicoding.submission.view.tools

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dicoding.submission.R

@Composable
fun BoxScope.ProgressIndicator() {
	CircularProgressIndicator(
		modifier = Modifier.align(Alignment.Center),
		color = MaterialTheme.colorScheme.primary
	)
}

@Composable
fun ErrorScreen(msg: String, modifier: Modifier) {
		Column(
			modifier = modifier,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Image(
				painter = painterResource(id = R.drawable.ic_error_connect),
				contentDescription = "error logo",
				modifier = Modifier.size(100.dp)
			)
			Text(
				text = msg,
				color = MaterialTheme.colorScheme.error,
				textAlign = TextAlign.Center
			)
		}
}