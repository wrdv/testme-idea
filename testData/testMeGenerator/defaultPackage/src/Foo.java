
import com.example.warriers.FooFighter;
import com.example.foes.Fire;
<caret>
public class Foo{

    private FooFighter fooFighter;

    public String fight(Fire withFire,Flames andFlames,String foeName) {
        return fooFighter.fight(withFire);
    }

}
