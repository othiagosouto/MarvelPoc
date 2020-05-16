package com.soutosss.marvelpoc.shared.koin

import org.koin.core.module.Module

object ModulesInitializer {

    private val _modules = mutableListOf<Module>()
    val modules: List<Module> = _modules

    fun addAll(list: List<Module>) {
        _modules.addAll(list)
    }
}