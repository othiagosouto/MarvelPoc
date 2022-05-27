package com.soutosss.marvelpoc.shared.koin

import org.koin.core.module.Module

interface KoinInitializer {

    fun createKoinModules(): Module
}