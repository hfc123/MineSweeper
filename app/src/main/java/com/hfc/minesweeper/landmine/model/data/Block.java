package com.hfc.minesweeper.landmine.model.data;

public enum Block {

    COVER(0),
    FLAG(1),
    DOUBT(2),
    MINE(3),
    MINEE(4),
    MINEO(5),
    DOUBT2(6),
    NUM8(7),
    NUM7(8),
    NUM6(9),
    NUM5(10),
    NUM4(11),
    NUM3(12),
    NUM2(13),
    NUM1(14),
    EMPTY(15);

    private int index;

    private Block(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
