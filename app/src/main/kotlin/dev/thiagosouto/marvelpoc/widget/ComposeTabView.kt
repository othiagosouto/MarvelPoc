package dev.thiagosouto.marvelpoc.widget

import android.content.Context
import android.util.AttributeSet
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Divider
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabPosition
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.max


@OptIn(ExperimentalPagerApi::class)
class ComposeTabView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AbstractComposeView(context, attrs, defStyleAttr) {

    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    @Composable
    override fun Content() {
        val pagerState = rememberPagerState(initialPage = 0)
        Column {

            ScrollableTabRow(
                divider = {Divider(color = Color.Yellow, thickness = 4.dp)},
                // Our selected tab is our current page
                selectedTabIndex = pagerState.currentPage,
                edgePadding = 0.dp,
                // Override the indicator, using the provided pagerTabIndicatorOffset modifier
                indicator = { tabPositions ->
                    Row(verticalAlignment = Alignment.Bottom) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .wrapContentSize(Alignment.BottomStart)
                                .fillMaxHeight()
                                .applyWeekShape(tabPositions, pagerState)
                                .background(Color.White)
                                .border(
                                    border = BorderStroke(4.dp, Color.Yellow),
                                    shape = WeekShape()
                                )
                        )
                    }
                }
            ) {
                // Add tabs for all of our pages
                (0..4).map { "Tab $it" }.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title, color = Color.Black) },
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                pagerState.scrollToPage(index)
                            }
                        },
                    )

                }
            }
            HorizontalPager(
                count = 5,
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 32.dp),
                modifier = Modifier
                    .fillMaxSize(1f)
            ) { page ->
                Text("Page $page", color = Color.Black)
            }
        }
    }

    @Composable
    fun Modifier.applyWeekShape(tabPositions: List<TabPosition>, pagerState: PagerState): Modifier {
        val targetIndicatorOffset: Dp
        val indicatorWidth: Dp

        val currentTab =
            tabPositions[minOf(tabPositions.lastIndex, pagerState.currentPage)]
        val targetPage = pagerState.targetPage
        val targetTab = tabPositions.getOrNull(targetPage)

        if (targetTab != null) {
            // The distance between the target and current page. If the pager is animating over many
            // items this could be > 1
            val targetDistance = (targetPage - pagerState.currentPage).absoluteValue
            // Our normalized fraction over the target distance
            val fraction =
                (pagerState.currentPageOffset / max(targetDistance, 1)).absoluteValue

            targetIndicatorOffset = lerp(currentTab.left, targetTab.left, fraction)
            indicatorWidth = lerp(currentTab.width, targetTab.width, fraction)
        } else {
            // Otherwise we just use the current tab/page
            targetIndicatorOffset = currentTab.left
            indicatorWidth = currentTab.width
        }
        return offset(x = targetIndicatorOffset).width(indicatorWidth)
    }
}

class WeekShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {

        val weekShape = Path().apply {
            moveTo(x = 0f, y = size.height)
            lineTo(x = 0f, y = 0f)
            lineTo(x = size.width, y = 0f)
            lineTo(x = size.width, y = size.height)
        }
        return Outline.Generic(path = weekShape)
    }
}