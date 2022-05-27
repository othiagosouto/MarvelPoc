package com.soutosss.marvelpoc.shared.koin

import org.koin.core.module.Module

interface KoinModulesProvider {

    fun provides(): Module
}
