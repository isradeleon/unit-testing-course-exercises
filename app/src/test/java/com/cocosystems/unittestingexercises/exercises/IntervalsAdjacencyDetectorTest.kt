package com.cocosystems.unittestingexercises.exercises

import com.cocosystems.unittestingexercises.exercises.exercise1.Interval
import com.cocosystems.unittestingexercises.exercises.exercise1.IntervalsAdjacencyDetector
import org.junit.Before
import org.junit.Test

class IntervalsAdjacencyDetectorTest {
    // SUT: System under test
    lateinit var intervalsAdjacencyDetector: IntervalsAdjacencyDetector

    @Before
    fun setup() {
        intervalsAdjacencyDetector =
            IntervalsAdjacencyDetector()
    }

    // interval1 comes before interval2, no adjacency
    @Test
    fun detector_interval1BeforeInterval2_falseReturned() {
        val interval1 =
            Interval(
                1,
                10
            )
        val interval2 =
            Interval(
                11,
                20
            )
        assert(!intervalsAdjacencyDetector.isAdjacent(interval1, interval2))
    }

    // interval1 comes before interval2, there's adjacency
    @Test
    fun detector_interval1BeforeInterval2_trueReturned() {
        val interval1 =
            Interval(
                1,
                10
            )
        val interval2 =
            Interval(
                10,
                20
            )
        assert(intervalsAdjacencyDetector.isAdjacent(interval1, interval2))
    }

    // interval1 and interval2 overlap
    @Test
    fun detector_interval1OverlapsInterval2_falseReturned() {
        val interval1 =
            Interval(
                1,
                11
            )
        val interval2 =
            Interval(
                10,
                20
            )
        assert(!intervalsAdjacencyDetector.isAdjacent(interval1, interval2))
    }

    // interval1 comes after interval2, there's adjacency
    @Test
    fun detector_interval1AfterInterval2_trueReturned() {
        val interval1 =
            Interval(
                10,
                100
            )
        val interval2 =
            Interval(
                1,
                10
            )
        assert(intervalsAdjacencyDetector.isAdjacent(interval1, interval2))
    }

    // interval1 comes after interval2, no adjacency
    @Test
    fun detector_interval1AfterInterval2_falseReturned() {
        val interval1 =
            Interval(
                10,
                100
            )
        val interval2 =
            Interval(
                1,
                9
            )
        assert(!intervalsAdjacencyDetector.isAdjacent(interval1, interval2))
    }

    // both intervals are the same
    @Test
    fun detector_interval1EqualsInterval2_falseReturned() {
        val interval1 =
            Interval(
                1,
                100
            )
        val interval2 =
            Interval(
                1,
                100
            )
        assert(!intervalsAdjacencyDetector.isAdjacent(interval1, interval2))
    }

    /*
    * Always think about both cases where the SUT should and should NOT work!
    * */
}