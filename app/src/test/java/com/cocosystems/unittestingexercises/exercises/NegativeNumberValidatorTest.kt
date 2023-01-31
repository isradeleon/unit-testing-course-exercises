package com.cocosystems.unittestingexercises.exercises

import com.cocosystems.unittestingexercises.exercises.exercise1.NegativeNumberValidator
import org.junit.Before
import org.junit.Test

class NegativeNumberValidatorTest {
    // SUT: System under test
    lateinit var negativeNumberValidator: NegativeNumberValidator

    @Before
    fun setup() {
        negativeNumberValidator =
            NegativeNumberValidator()
    }

    // parameter lower than 0
    @Test
    fun validator_lowerThanZero_trueReturned(){
        val result = negativeNumberValidator.isNegative(-1)
        assert(result)
    }

    // parameter equal to 0
    @Test
    fun validator_equalToZero_falseReturned(){
        val result = negativeNumberValidator.isNegative(0)
        assert(!result)
    }

    // parameter greater than 0
    @Test
    fun validator_greaterThanZero_falseReturned(){
        val result = negativeNumberValidator.isNegative(1)
        assert(!result)
    }
}