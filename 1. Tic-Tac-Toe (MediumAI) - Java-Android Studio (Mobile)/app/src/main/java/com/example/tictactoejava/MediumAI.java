package com.example.tictactoejava;

public class MediumAI {
    private String mFlag;
    private int mMoveNumber;
    private int mGameControl;
    private int[] mArrayMoves = new int[9];
    private RandomGroup mRandomNumber;

    public String getFlag() {
        return mFlag;
    }

    public void setFlag(String flag) {
        this.mFlag = flag;
    }

    public int getMoveNumber() {
        return mMoveNumber;
    }

    public void setMoveNumber(int moveNumber) {
        this.mMoveNumber = moveNumber;
    }

    public int getGameControl() {
        return mGameControl;
    }

    public void setGameControl(int gameControl) {
        this.mGameControl = gameControl;
    }

    public int[] getArrayMoves() {
        return mArrayMoves;
    }

    public void setArrayMoves(int[] arrayMoves) {
        this.mArrayMoves = arrayMoves;
    }

    public RandomGroup getRandomGroup() {
        return mRandomNumber;
    }

    public void setRandomGroup(RandomGroup randomNumber) {
        this.mRandomNumber = randomNumber;
    }

    public MediumAI() {
        for (int i = 0; i < mArrayMoves.length; i++) {
            mArrayMoves[i] = 0;
        }
        mFlag = "X";
        mMoveNumber = 0;
        mGameControl = 0;
        mRandomNumber = new RandomGroup();
    }

    public void InitializeData() {
        for (int i = 0; i < mArrayMoves.length; i++) {
            mArrayMoves[i] = 0;
        }
        mFlag = "X";
        mMoveNumber = 0;
        mGameControl = 0;
        mRandomNumber = new RandomGroup();
    }

    private void ShowUserMove(int index) {
        mArrayMoves[index] = 1;
        mMoveNumber++;
        mFlag = "0";
    }

    private void ShowNPCMove(int index) {
        mArrayMoves[index] = 3;
        mMoveNumber++;
        mFlag = "X";
    }

    private int GenerateRandomNPCMove() {
        int index;
        do {
            index = getRandomGroup().getRandomNumber(0, 8);
            if (mArrayMoves[index] == 0 && index < mArrayMoves.length) {
                return index;
            }
        } while (true);
    }

    public void PlayUser(int index) {
        if (mArrayMoves[index] == 0 && mFlag == "X") {
            ShowUserMove(index);
        }
    }

    private int HeuristicNumericalMethod() {
        int index = 0;
        if (mArrayMoves[0] + mArrayMoves[1] + mArrayMoves[2] == 6) {
            if (mArrayMoves[0] == 0) {
                index = 0;
            } else if (mArrayMoves[1] == 0) {
                index = 1;
            } else if (mArrayMoves[2] == 0) {
                index = 2;
            }
            return index;
        } else if (mArrayMoves[3] + mArrayMoves[4] + mArrayMoves[5] == 6) {
            if (mArrayMoves[3] == 0) {
                index = 3;
            } else if (mArrayMoves[4] == 0) {
                index = 4;
            } else if (mArrayMoves[5] == 0) {
                index = 5;
            }
            return index;
        } else if (mArrayMoves[6] + mArrayMoves[7] + mArrayMoves[8] == 6) {
            if (mArrayMoves[6] == 0) {
                index = 6;
            } else if (mArrayMoves[7] == 0) {
                index = 7;
            } else if (mArrayMoves[8] == 0) {
                index = 8;
            }
            return index;
        } else if (mArrayMoves[0] + mArrayMoves[3] + mArrayMoves[6] == 6) {
            if (mArrayMoves[0] == 0) {
                index = 0;
            } else if (mArrayMoves[3] == 0) {
                index = 3;
            } else if (mArrayMoves[6] == 0) {
                index = 6;
            }
            return index;
        } else if (mArrayMoves[1] + mArrayMoves[4] + mArrayMoves[7] == 6) {
            if (mArrayMoves[1] == 0) {
                index = 1;
            } else if (mArrayMoves[4] == 0) {
                index = 4;
            } else if (mArrayMoves[7] == 0) {
                index = 7;
            }
            return index;
        } else if (mArrayMoves[2] + mArrayMoves[5] + mArrayMoves[8] == 6) {
            if (mArrayMoves[2] == 0) {
                index = 2;
            } else if (mArrayMoves[5] == 0) {
                index = 5;
            } else if (mArrayMoves[8] == 0) {
                index = 8;
            }
            return index;
        } else if (mArrayMoves[0] + mArrayMoves[4] + mArrayMoves[8] == 6) {
            if (mArrayMoves[0] == 0) {
                index = 0;
            } else if (mArrayMoves[4] == 0) {
                index = 4;
            } else if (mArrayMoves[8] == 0) {
                index = 8;
            }
            return index;
        } else if (mArrayMoves[2] + mArrayMoves[4] + mArrayMoves[6] == 6) {
            if (mArrayMoves[2] == 0) {
                index = 2;
            } else if (mArrayMoves[4] == 0) {
                index = 4;
            } else if (mArrayMoves[6] == 0) {
                index = 6;
            }
            return index;
        } else if (mArrayMoves[0] + mArrayMoves[1] + mArrayMoves[2] == 2) {
            if (mArrayMoves[0] == 0) {
                index = 0;
            } else if (mArrayMoves[1] == 0) {
                index = 1;
            } else if (mArrayMoves[2] == 0) {
                index = 2;
            }
            return index;
        } else if (mArrayMoves[3] + mArrayMoves[4] + mArrayMoves[5] == 2) {
            if (mArrayMoves[3] == 0) {
                index = 3;
            } else if (mArrayMoves[4] == 0) {
                index = 4;
            } else if (mArrayMoves[5] == 0) {
                index = 5;
            }
            return index;
        } else if (mArrayMoves[6] + mArrayMoves[7] + mArrayMoves[8] == 2) {
            if (mArrayMoves[6] == 0) {
                index = 6;
            } else if (mArrayMoves[7] == 0) {
                index = 7;
            } else if (mArrayMoves[8] == 0) {
                index = 8;
            }
            return index;
        } else if (mArrayMoves[0] + mArrayMoves[3] + mArrayMoves[6] == 2) {
            if (mArrayMoves[0] == 0) {
                index = 0;
            } else if (mArrayMoves[3] == 0) {
                index = 3;
            } else if (mArrayMoves[6] == 0) {
                index = 6;
            }
            return index;
        } else if (mArrayMoves[1] + mArrayMoves[4] + mArrayMoves[7] == 2) {
            if (mArrayMoves[1] == 0) {
                index = 1;
            } else if (mArrayMoves[4] == 0) {
                index = 4;
            } else if (mArrayMoves[7] == 0) {
                index = 7;
            }
            return index;
        } else if (mArrayMoves[2] + mArrayMoves[5] + mArrayMoves[8] == 2) {
            if (mArrayMoves[2] == 0) {
                index = 2;
            } else if (mArrayMoves[5] == 0) {
                index = 5;
            } else if (mArrayMoves[8] == 0) {
                index = 8;
            }
            return index;
        } else if (mArrayMoves[0] + mArrayMoves[4] + mArrayMoves[8] == 2) {
            if (mArrayMoves[0] == 0) {
                index = 0;
            } else if (mArrayMoves[4] == 0) {
                index = 4;
            } else if (mArrayMoves[8] == 0) {
                index = 8;
            }
            return index;
        } else if (mArrayMoves[2] + mArrayMoves[4] + mArrayMoves[6] == 2) {
            if (mArrayMoves[2] == 0) {
                index = 2;
            } else if (mArrayMoves[4] == 0) {
                index = 4;
            } else if (mArrayMoves[6] == 0) {
                index = 6;
            }
            return index;
        } else {
            index = GenerateRandomNPCMove();
            return index;
        }
    }

    public int PlayNPC() {
        int index = -1;
        if (mGameControl == 0) {
            if (mArrayMoves[4] == 0) {
                index = 4;
                ShowNPCMove(index);
                return index;
            } else {
                index = HeuristicNumericalMethod();
                ShowNPCMove(index);
                return index;
            }
        }
        return -1;
    }
}
