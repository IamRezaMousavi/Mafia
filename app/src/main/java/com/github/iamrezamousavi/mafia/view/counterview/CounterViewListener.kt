package com.github.iamrezamousavi.mafia.view.counterview

interface CounterViewListener {
    fun onIncrease()
    fun onDecrease()
    fun onValueChanged(value: Int)
}
