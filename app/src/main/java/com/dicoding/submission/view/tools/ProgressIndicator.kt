package com.dicoding.submission.view.tools

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.dicoding.submission.R

@Composable
fun BoxScope.ProgressIndicator () {
	CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
}

@Composable
fun BoxScope.ErrorScreen() {
	Image(
		painter = painterResource(id = R.drawable.ic_error_connect),
		contentDescription = "error logo",
		modifier = Modifier.align(Alignment.Center)
	)
}