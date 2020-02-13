package dev.kichinaga.coroutinetimer

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

sealed class TimerState() {
    object Start: TimerState()
    object Stop: TimerState()
}