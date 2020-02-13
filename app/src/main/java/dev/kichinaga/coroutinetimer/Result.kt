package dev.kichinaga.coroutinetimer

sealed class Result<Int> {
    data class Stop(val data: Int) : Result<Int>()
    data class Start(val data: Int) : Result<Int>()
}
