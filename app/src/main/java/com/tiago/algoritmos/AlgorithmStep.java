package com.tiago.algoritmos;

class AlgorithmStep
{
    private String stepDescription;
    private int position1, position2;
    private boolean swap;
    private int barOk = -1;
    private boolean animate;

    public AlgorithmStep(int position1, int position2, boolean swap)
    {
        this.position1 = position1;
        this.position2 = position2;
        this.swap = swap;
        this.animate = true;
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

    public int getPosition1() {
        return position1;
    }

    public int getPosition2() {
        return position2;
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

}
