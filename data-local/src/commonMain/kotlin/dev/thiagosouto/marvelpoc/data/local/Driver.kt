package dev.thiagosouto.marvelpoc.data.local

import app.cash.sqldelight.db.SqlDriver

internal expect class DriverFactory {
    fun createDriver(): SqlDriver
}
