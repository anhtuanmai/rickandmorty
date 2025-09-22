package demo.at.ram.presentation.toolkit

import androidx.test.platform.app.InstrumentationRegistry

object RamInstrumentation {
    fun getString(id: Int): String {
        return InstrumentationRegistry.getInstrumentation().targetContext.resources.getString(id)
    }
}