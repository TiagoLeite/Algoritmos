package com.tiago.algoritmos;

class AlgorithmStep
{
    private String stateDescripton;
    private int position1, position2;
    private boolean swap;
    private int barOk;


    public AlgorithmStep(int position1, int position2, boolean swap)
    {
        this.position1 = position1;
        this.position2 = position2;
        this.swap = swap;
        this.barOk = -1;
    }

    public String getStateDescripton() {
        return stateDescripton;
    }

    public void setStepDescripton(String stateDescripton) {
        this.stateDescripton = stateDescripton;
    }

    public int getPosition1() {
        return position1;
    }

    public void setPosition1(int position1) {
        this.position1 = position1;
    }

    public int getPosition2() {
        return position2;
    }

    public void setPosition2(int position2) {
        this.position2 = position2;
    }

    public boolean isSwap() {
        return swap;
    }

    public void setSwap(boolean swap) {
        this.swap = swap;
    }

    public int getBarOk() {
        return barOk;
    }

    public void setBarOk(int barOk) {
        this.barOk = barOk;
    }
}
