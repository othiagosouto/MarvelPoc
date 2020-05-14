package com.soutosss.marvelpoc.shared.koin;

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.koin.core.module.Module
import org.koin.dsl.module

class ModulesInitializerTest {

    @Test
    fun `addAll should add content to modules list`() {
        val modules = listOf(module { })
        ModulesInitializer.addAll(modules)

        assertThat(ModulesInitializer.modules).isEqualTo(modules)
    }

    @Test
    fun `init should initialize modules empty`() {

        assertThat(ModulesInitializer.modules).isEqualTo(emptyList<Module>())
    }
}