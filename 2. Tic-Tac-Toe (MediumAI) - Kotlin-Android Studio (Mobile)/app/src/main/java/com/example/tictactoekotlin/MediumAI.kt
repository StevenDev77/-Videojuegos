package com.example.tictactoekotlin

class MediumAI {
    private var mFlag: String = "X"
    private var mMoveNumber: Int = 0
    private var mGameControl: Int = 0
    private var mArrayMoves: IntArray = IntArray(9)
    private var mRandomNumber: RandomGroup = RandomGroup()

    init {
        for (i in mArrayMoves.indices) {
            mArrayMoves[i] = 0
        }
        mFlag = "X"
        mMoveNumber = 0
        mGameControl = 0
        mRandomNumber = RandomGroup()
    }

    fun getFlag(): String {
        return mFlag
    }

    fun setFlag(flag: String) {
        this.mFlag = flag
    }

    fun getMoveNumber(): Int {
        return mMoveNumber
    }

    fun setMoveNumber(moveNumber: Int) {
        this.mMoveNumber = moveNumber
    }

    fun getGameControl(): Int {
        return mGameControl
    }

    fun setGameControl(gameControl: Int) {
        this.mGameControl = gameControl
    }

    fun getArrayMoves(): IntArray {
        return mArrayMoves
    }

    fun setArrayMoves(arrayMoves: IntArray) {
        this.mArrayMoves = arrayMoves
    }

    fun getRandomGroup(): RandomGroup {
        return mRandomNumber
    }

    fun setRandomGroup(randomNumber: RandomGroup) {
        this.mRandomNumber = randomNumber
    }

    fun InitializeData() {
        for (i in mArrayMoves.indices) {
            mArrayMoves[i] = 0
        }
        mFlag = "X"
        mMoveNumber = 0
        mGameControl = 0
        mRandomNumber = RandomGroup()
    }

    private fun ShowUserMove(index: Int) {
        mArrayMoves[index] = 1
        mMoveNumber++
        mFlag = "0"
    }

    private fun ShowNPCMove(index: Int) {
        mArrayMoves[index] = 3
        mMoveNumber++
        mFlag = "X"
    }

    fun PlayUser(index: Int) {
        if (mArrayMoves[index] == 0 && mFlag == "X") {
            ShowUserMove(index)
        }
    }

    private fun GenerateRandomNPCMove(): Int {
        var index: Int
        do {
            index = getRandomGroup().getRandomNumber(0, 8)
            if (mArrayMoves[index] == 0 && index < mArrayMoves.size) {
                return index
            }
        } while (true)
    }

    private fun HeuristicNumericalMethod(): Int {
        var index = 0
        if (mArrayMoves[0] + mArrayMoves[1] + mArrayMoves[2] == 6) {
            if (mArrayMoves[0] == 0) {
                index = 0
            } else if (mArrayMoves[1] == 0) {
                index = 1
            } else if (mArrayMoves[2] == 0) {
                index = 2
            }
            return index
        } else if (mArrayMoves[3] + mArrayMoves[4] + mArrayMoves[5] == 6) {
            if (mArrayMoves[3] == 0) {
                index = 3
            } else if (mArrayMoves[4] == 0) {
                index = 4
            } else if (mArrayMoves[5] == 0) {
                index = 5
            }
            return index
        } else if (mArrayMoves[6] + mArrayMoves[7] + mArrayMoves[8] == 6) {
            if (mArrayMoves[6] == 0) {
                index = 6
            } else if (mArrayMoves[7] == 0) {
                index = 7
            } else if (mArrayMoves[8] == 0) {
                index = 8
            }
            return index
        } else if (mArrayMoves[0] + mArrayMoves[3] + mArrayMoves[6] == 6) {
            if (mArrayMoves[0] == 0) {
                index = 0
            } else if (mArrayMoves[3] == 0) {
                index = 3
            } else if (mArrayMoves[6] == 0) {
                index = 6
            }
            return index
        } else if (mArrayMoves[1] + mArrayMoves[4] + mArrayMoves[7] == 6) {
            if (mArrayMoves[1] == 0) {
                index = 1
            } else if (mArrayMoves[4] == 0) {
                index = 4
            } else if (mArrayMoves[7] == 0) {
                index = 7
            }
            return index
        } else if (mArrayMoves[2] + mArrayMoves[5] + mArrayMoves[8] == 6) {
            if (mArrayMoves[2] == 0) {
                index = 2
            } else if (mArrayMoves[5] == 0) {
                index = 5
            } else if (mArrayMoves[8] == 0) {
                index = 8
            }
            return index
        } else if (mArrayMoves[0] + mArrayMoves[4] + mArrayMoves[8] == 6) {
            if (mArrayMoves[0] == 0) {
                index = 0
            } else if (mArrayMoves[4] == 0) {
                index = 4
            } else if (mArrayMoves[8] == 0) {
                index = 8
            }
            return index
        } else if (mArrayMoves[2] + mArrayMoves[4] + mArrayMoves[6] == 6) {
            if (mArrayMoves[2] == 0) {
                index = 2
            } else if (mArrayMoves[4] == 0) {
                index = 4
            } else if (mArrayMoves[6] == 0) {
                index = 6
            }
            return index
        } else if (mArrayMoves[0] + mArrayMoves[1] + mArrayMoves[2] == 2) {
            if (mArrayMoves[0] == 0) {
                index = 0
            } else if (mArrayMoves[1] == 0) {
                index = 1
            } else if (mArrayMoves[2] == 0) {
                index = 2
            }
            return index
        } else if (mArrayMoves[3] + mArrayMoves[4] + mArrayMoves[5] == 2) {
            if (mArrayMoves[3] == 0) {
                index = 3
            } else if (mArrayMoves[4] == 0) {
                index = 4
            } else if (mArrayMoves[5] == 0) {
                index = 5
            }
            return index
        } else if (mArrayMoves[6] + mArrayMoves[7] + mArrayMoves[8] == 2) {
            if (mArrayMoves[6] == 0) {
                index = 6
            } else if (mArrayMoves[7] == 0) {
                index = 7
            } else if (mArrayMoves[8] == 0) {
                index = 8
            }
            return index
        } else if (mArrayMoves[0] + mArrayMoves[3] + mArrayMoves[6] == 2) {
            if (mArrayMoves[0] == 0) {
                index = 0
            } else if (mArrayMoves[3] == 0) {
                index = 3
            } else if (mArrayMoves[6] == 0) {
                index = 6
            }
            return index
        } else if (mArrayMoves[1] + mArrayMoves[4] + mArrayMoves[7] == 2) {
            if (mArrayMoves[1] == 0) {
                index = 1
            } else if (mArrayMoves[4] == 0) {
                index = 4
            } else if (mArrayMoves[7] == 0) {
                index = 7
            }
            return index
        } else if (mArrayMoves[2] + mArrayMoves[5] + mArrayMoves[8] == 2) {
            if (mArrayMoves[2] == 0) {
                index = 2
            } else if (mArrayMoves[5] == 0) {
                index = 5
            } else if (mArrayMoves[8] == 0) {
                index = 8
            }
            return index
        } else if (mArrayMoves[0] + mArrayMoves[4] + mArrayMoves[8] == 2) {
            if (mArrayMoves[0] == 0) {
                index = 0
            } else if (mArrayMoves[4] == 0) {
                index = 4
            } else if (mArrayMoves[8] == 0) {
                index = 8
            }
            return index
        } else if (mArrayMoves[2] + mArrayMoves[4] + mArrayMoves[6] == 2) {
            if (mArrayMoves[2] == 0) {
                index = 2
            } else if (mArrayMoves[4] == 0) {
                index = 4
            } else if (mArrayMoves[6] == 0) {
                index = 6
            }
            return index
        } else {
            index = GenerateRandomNPCMove()
            return index
        }
    }

    fun PlayNPC(): Int {
        if (mGameControl == 0) {
            if (mArrayMoves[4] == 0) {
                val index = 4
                ShowNPCMove(index)
                return index
            } else {
                val index = HeuristicNumericalMethod()
                ShowNPCMove(index)
                return index
            }
        }
        return -1
    }
}
