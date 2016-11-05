import com.example.warriers.FooFighter;


public class Foo {

    private FooFighter fooFighter;

    private com.example.foes.Fire fireOfFoe;

    public String fight(Fire greatBallsOf, com.example.hole.Fire inTheHole) {
        System.out.println(greatBallsOf);
        System.out.println(inTheHole);
        return fooFighter.fight(fireOfFoe);
    }

}
