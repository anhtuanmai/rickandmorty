package demo.at.ram.presentation.ui.log

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import demo.at.ram.presentation.BuildConfig
import timber.log.Timber

class Ref(var value: Int)

@Suppress("NOTHING_TO_INLINE")
@Composable
inline fun LogCompositions(msg: String = "") {
    if (BuildConfig.DEBUG) {
        val ref = remember { Ref(0) }
        SideEffect { ref.value++ }
        Timber.d("Compositions: $msg ${ref.value}")
    }
}