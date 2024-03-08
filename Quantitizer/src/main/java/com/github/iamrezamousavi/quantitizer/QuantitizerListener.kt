package com.github.iamrezamousavi.quantitizer

interface QuantitizerListener {
    fun onIncrease()
    fun onDecrease()
    fun onValueChanged(value: Int)
}
