package com.soutosss.marvelpoc.home

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import io.mockk.every
import io.mockk.mockk

fun provideLifecycleState(state: Lifecycle.State): LifecycleOwner {
    val owner = mockk<LifecycleOwner>(relaxed = true)
    val lifecycle = LifecycleRegistry(owner)
    lifecycle.currentState = state
    every { owner.lifecycle } returns lifecycle
    return owner
}
