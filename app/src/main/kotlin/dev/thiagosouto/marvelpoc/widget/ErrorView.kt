package dev.thiagosouto.marvelpoc.widget

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AbstractComposeView

class ErrorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AbstractComposeView(context, attrs, defStyleAttr) {
    private var content: ErrorData? by mutableStateOf(value = null)

    @Composable
    override fun Content() {
        val content = content

        if (content != null) {
            ErrorScreen(
                modifier = Modifier.fillMaxSize(),
                message = content.message,
                image = content.image
            )
        }
    }

    fun bind(content: ErrorData?) {
        this.content = content
    }
}

data class ErrorData(@StringRes val message: Int, @DrawableRes val image: Int)
