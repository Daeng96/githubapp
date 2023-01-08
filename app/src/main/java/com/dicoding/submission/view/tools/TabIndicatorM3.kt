package com.dicoding.submission.view.tools

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabPosition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.google.accompanist.pager.ExperimentalPagerApi


object GithubTabRowDefault {

	@Composable
	fun Indicator(
		modifier: Modifier = Modifier,
		height: Dp = 8.dp,
		color: Color =
			MaterialTheme.colorScheme.primary
	) {
		Box(
			modifier
				.fillMaxWidth()
				.height(height)
				.background(color = color, shape = MaterialTheme.shapes.small)
		)
	}

	@OptIn(ExperimentalFoundationApi::class)
	@ExperimentalPagerApi
	fun Modifier.pagerM3TabIndicatorOffset(
		pagerState: PagerState,
		tabPositions: List<TabPosition>,
		pageIndexMapping: (Int) -> Int = { it },
	): Modifier = layout { measurable, constraints ->
		if (tabPositions.isEmpty()) {
			// If there are no pages, nothing to show
			layout(constraints.maxWidth, 0) {}
		} else {
			val currentPage =
				minOf(tabPositions.lastIndex, pageIndexMapping(pagerState.currentPage))

			val currentTab = tabPositions[currentPage]
			val previousTab = tabPositions.getOrNull(currentPage - 1)
			val nextTab = tabPositions.getOrNull(currentPage + 1)
			val fraction = pagerState.currentPageOffsetFraction

			val indicatorWidth = if (fraction > 0 && nextTab != null) {
				lerp(currentTab.width, nextTab.width, fraction).roundToPx()
			} else if (fraction < 0 && previousTab != null) {
				lerp(currentTab.width, previousTab.width, -fraction).roundToPx()
			} else {
				currentTab.width.roundToPx()
			}
			val indicatorOffset = if (fraction > 0 && nextTab != null) {
				lerp(currentTab.left, nextTab.left, fraction).roundToPx()
			} else if (fraction < 0 && previousTab != null) {
				lerp(currentTab.left, previousTab.left, -fraction).roundToPx()
			} else {
				currentTab.left.roundToPx()
			}
			val placeable = measurable.measure(
				Constraints(
					minWidth = indicatorWidth,
					maxWidth = indicatorWidth,
					minHeight = 0,
					maxHeight = constraints.maxHeight
				)
			)

			layout(
				constraints.maxWidth,
				maxOf(placeable.height, constraints.minHeight)
			) {
				placeable.placeRelative(
					indicatorOffset,
					maxOf(constraints.minHeight - placeable.height, 0)
				)
			}
		}
	}

}