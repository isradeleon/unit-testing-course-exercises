package com.cocosystems.unittestingexercises.exercises

import com.cocosystems.unittestingexercises.exercises.exercise1.StringDuplicator
import org.junit.Before
import org.junit.Test

class StringDuplicatorTest {
    // SUT: System under test
    lateinit var stringDuplicator: StringDuplicator

    @Before
    fun setup() {
        stringDuplicator =
            StringDuplicator()
    }

    // parameter is an empty string
    @Test
    fun duplicator_emptyString_emptyStringReturned() {
        val result = stringDuplicator.duplicate("")
        assert(result == "")
    }

    // parameter is an actual string
    @Test
    fun duplicator_actualString_duplicatedStringReturned() {
        val result = stringDuplicator.duplicate("123")
        assert(result == "123123")
    }

    // parameter is a blank space
    @Test
    fun duplicator_blankString_duplicatedBlankStringReturned() {
        val result = stringDuplicator.duplicate(" ")
        assert(result == "  ")
    }
}