package dev.thiagosouto.marvelpoc.widget

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.thiagosouto.marvelpoc.R

@Composable
internal fun ErrorScreen(
    modifier: Modifier = Modifier,
    @StringRes message: Int,
    @DrawableRes image: Int
) {
    Column(
        modifier = modifier.testTag("error-container"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier
                .height(100.dp)
                .wrapContentWidth()
                .testTag(ErrorScreenTestTags.IMAGE),
            painter = painterResource(image),
            contentDescription = ""
        )
        Spacer(
            modifier = Modifier.height(
                dimensionResource(id = dev.thiagosouto.marvelpoc.design.R.dimen.spacing_small)
            )
        )
        Text(
            modifier = Modifier.testTag(ErrorScreenTestTags.MESSAGE),
            text = stringResource(id = message)
        )
    }
}

@Preview
@Composable
private fun ErrorScreenPreview() {
    ErrorScreen(
        modifier = Modifier.fillMaxSize(),
        message = R.string.home_error_loading,
        image = dev.thiagosouto.marvelpoc.design.R.drawable.thanos
    )
}

internal object ErrorScreenTestTags {
    const val IMAGE = "error-image"
    const val MESSAGE = "error-message"
}
