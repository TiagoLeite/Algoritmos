package com.tiago.algoritmos;

class AlgorithmStep
{
    private String stepDescription;
    private int arrValue1, arrValue2, codeLine, i, j;
    private boolean swap;
    private int barOk = -1;
    private boolean animate;

    public AlgorithmStep(int arrValue1, int arrValue2, boolean swap)
    {
        this.arrValue1 = arrValue1;
        this.arrValue2 = arrValue2;
        this.swap = swap;
        this.animate = true;
        this.codeLine = -1;
    }

    public AlgorithmStep(int i, int j)
    {
        this.arrValue1 = -1;
        this.arrValue2 = -1;
        this.animate = true;
        this.codeLine = -1;
        this.i = i;
        this.j = j;
    }

    public AlgorithmStep(String stepDescription)
    {
        this.stepDescription = stepDescription;
    }

    public String getStepDescription() {
        return stepDescription;
    }

    public void setStepDescription(String stateDescription) {
        this.stepDescription = stateDescription;
    }

    public int getArrValue1() {
        return arrValue1;
    }

    public int getArrValue2() {
        return arrValue2;
    }

    public boolean isSwap() {
        return swap;
    }

    public int getBarOk() {
        return barOk;
    }

    public void setBarOk(int barOk) {
        this.barOk = barOk;
    }

    public boolean getAnimate()
    {
        return this.animate;
    }

    public int getCodeLine() {
        return codeLine;
    }

    public void setCodeLine(int codeLine) {
        this.codeLine = codeLine;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }
}
