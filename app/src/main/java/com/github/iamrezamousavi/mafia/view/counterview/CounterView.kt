package com.github.iamrezamousavi.mafia.view.counterview

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.iamrezamousavi.mafia.R
import com.github.iamrezamousavi.mafia.databinding.ViewCounterBinding

class CounterView @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null, defStyle: Int = 0
) : ConstraintLayout(context, attributeSet, defStyle) {

    private var listener: CounterViewListener? = null
    private val binding = ViewCounterBinding.inflate(LayoutInflater.from(context), this, true)
    private var currentValue: Int = 0

    private var _minValue: Int = 0
    private var _maxValue: Int = Int.MAX_VALUE
    private var _isReadOnly: Boolean = false

    var minValue: Int
        get() = _minValue
        set(value) {
            if (value >= currentValue) {
                binding.counterET.text =
                    Editable.Factory.getInstance().newEditable(value.toString())
                currentValue = value
                _minValue = value
            } else {
                _minValue = value
            }
        }

    var maxValue: Int
        get() = _maxValue
        set(value) {
            if (value < currentValue) {
                binding.counterET.text =
                    Editable.Factory.getInstance().newEditable(value.toString())
                currentValue = value
                _maxValue = value
            } else {
                _maxValue = value
            }
        }

    var value: Int
        get() = currentValue
        set(value) {
            currentValue = value
            binding.counterET.text = Editable.Factory.getInstance().newEditable(value.toString())
        }

    var isReadOnly: Boolean
        get() = _isReadOnly
        set(value) {
            isReadOnly(value)
        }

    init {
        Log.d("TAG", "ROLE: counterview is started")

        val a = context.obtainStyledAttributes(
            attributeSet, R.styleable.CounterView, defStyle, 0
        )

        minValue = a.getInteger(
            R.styleable.CounterView_minValue, 0
        )

        maxValue = a.getInteger(
            R.styleable.CounterView_maxValue, Int.MAX_VALUE
        )

        value = a.getInteger(
            R.styleable.CounterView_value, 0
        )

        /*decrease*/
        binding.minusButton.setOnClickListener {
            hideKeyboard()
            if (minValue >= currentValue) {
                //do nothing
            } else {
                doDec()
                listener?.onDecrease()
            }
        }

        /*increase*/
        binding.plusButton.setOnClickListener {
            hideKeyboard()
            if (maxValue <= currentValue) {
                //do  nothing
            } else {
                doInc()
                listener?.onIncrease()
            }
        }

        /*make edit text cursor visible when clicked*/
        binding.counterET.setOnClickListener {
            if (_isReadOnly.not()) {
                binding.counterET.isCursorVisible = true
            }
        }

        binding.counterET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                currentValue = if (s.toString().isNotEmpty() || s.toString() != "") {
                    val value = Integer.parseInt(s.toString())
                    listener?.onValueChanged(value)
                    value
                } else {
                    0
                }
            }

            override fun afterTextChanged(s: Editable?) {
                val value = s.toString().toIntOrNull()
                if (s.toString().isEmpty()) {
                    //do nothing
                } else if (value!! < minValue && s.toString().isBlank().not()) {
                    binding.counterET.text =
                        Editable.Factory.getInstance().newEditable(minValue.toString())
                    currentValue = minValue
                    Toast.makeText(context, "Min value is $minValue", Toast.LENGTH_SHORT).show()
                } else if (value > maxValue && s.toString().isBlank().not()) {
                    binding.counterET.text =
                        Editable.Factory.getInstance().newEditable(maxValue.toString())
                    currentValue = maxValue
                    Toast.makeText(context, "Max value is $maxValue", Toast.LENGTH_SHORT).show()
                }
            }
        })

        Log.d("TAG", "ROLE: Counter View is ok")

        /*TypedArrays are heavyweight objects that should be recycled immediately
         after all the attributes you need have been extracted.*/
        a.recycle()
    }

    private fun doInc() {
        binding.counterET.isCursorVisible = false // hide cursor if it's visible
        val increasedValue: Int = currentValue.inc()
        value = increasedValue
    }

    private fun doDec() {
        binding.counterET.isCursorVisible = false  // hide cursor if it's visible
        val decreasedValue: Int = currentValue.dec()
        value = decreasedValue
    }

    fun setCounterListener(listener: CounterViewListener) {
        this.listener = listener
    }

    private fun isReadOnly(isReadOnly: Boolean): Boolean {
        return if (isReadOnly) { // if user wants read only, then set edittext enabled to false
            binding.counterET.apply {
                isFocusableInTouchMode = false
                isCursorVisible = false
                inputType = InputType.TYPE_NULL
            }
            true
        } else { // else set enabled to true
            binding.counterET.apply {
                isFocusableInTouchMode = true
                isCursorVisible = true
            }
            false
        }
    }
}