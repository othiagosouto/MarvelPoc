package dev.thiagosouto.marvelpoc.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

class Dispatchers {
    val io = Dispatchers.IO
    val main = Dispatchers.Main
    val default = Dispatchers.Default
}
