package com.example.tictactoejava;

import java.util.Random;

public class RandomGroup {
    private int mLowerLimit;
    private int mUpperLimit;
    private int mRandomNumber;

    public RandomGroup() {
        mLowerLimit = 0;
        mUpperLimit = 0;
        mRandomNumber = 0;
    }

    public int getLowerLimit() {
        return mLowerLimit;
    }

    public void setLowerLimit(int lowerLimit) {
        this.mLowerLimit = lowerLimit;
    }

    public int getUpperLimit() {
        return mUpperLimit;
    }

    public void setUpperLimit(int upperLimit) {
        this.mUpperLimit = upperLimit;
    }

    public int getRandomNumber() {
        return mRandomNumber;
    }

    public void setRandomNumber(int randomNumber) {
        this.mRandomNumber = randomNumber;
    }

    public int getRandomNumber(int lowerLimit, int upperLimit) {
        Random number = new Random();
        mLowerLimit = lowerLimit;
        mUpperLimit = upperLimit;
        if (mLowerLimit > mUpperLimit) {
            throw new IllegalArgumentException("El inicio no puede exceder del terminal.");
        }
        long range = (long) mUpperLimit - (long) mLowerLimit + 1;
        long fraccion = (long) (range * number.nextDouble());
        mRandomNumber = (int) (fraccion + mLowerLimit);
        return mRandomNumber;
    }

    public void PrintMessage(String message) {
        System.out.println(message);
    }
}
