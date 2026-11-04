package com.example.tictactoekotlin

import kotlin.random.Random

class RandomGroup {
    private var mLowerLimit: Int = 0
    private var mUpperLimit: Int = 0
    private var mRandomNumber: Int = 0

    fun getLowerLimit(): Int {
        return mLowerLimit
    }

    fun setLowerLimit(lowerLimit: Int) {
        this.mLowerLimit = lowerLimit
    }

    fun getUpperLimit(): Int {
        return mUpperLimit
    }

    fun setUpperLimit(upperLimit: Int) {
        this.mUpperLimit = upperLimit
    }

    fun getRandomNumber(): Int {
        return mRandomNumber
    }

    fun setRandomNumber(randomNumber: Int) {
        this.mRandomNumber = randomNumber
    }

    fun getRandomNumber(lowerLimit: Int, upperLimit: Int): Int {
        mLowerLimit = lowerLimit
        mUpperLimit = upperLimit
        if (mLowerLimit > mUpperLimit) {
            throw IllegalArgumentException("El inicio no puede exceder del terminal.")
        }
        val range = mUpperLimit.toLong() - mLowerLimit.toLong() + 1
        val fraction = (range * Random.nextDouble()).toLong()
        mRandomNumber = (fraction + mLowerLimit).toInt()
        return mRandomNumber
    }

    fun PrintMessage(message: String) {
        println(message)
    }
}
