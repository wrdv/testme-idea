package com.example.beans;

/** Test input class*/
public class Spiral {
    String moon;
    private int rings;

    public void setMoon(String moon) {
        this.moon = moon;
    }

    public void setRings(int rings) {
        this.rings = rings;
    }

    public static class Circle{
        String aCircleInASpiral;

        public Circle(String aCircleInASpiral) {
            this.aCircleInASpiral = aCircleInASpiral;
        }
    }
    public static class Wheel{
        String neverEnding;
        private boolean orBeginning;

        public Wheel(String neverEnding,boolean orBeginning) {
            this.neverEnding = neverEnding;
            this.orBeginning = orBeginning;
        }
        public class WheelWithinAWheel{
            String everSpinningReel;

            public WheelWithinAWheel(String everSpinningReel) {
                this.everSpinningReel = everSpinningReel;
            }
        }
    }

    @Override
    public String toString() {
        return "Spiral{" +
                "moon='" + moon + '\'' +
                ", rings=" + rings +
                '}';
    }
}
