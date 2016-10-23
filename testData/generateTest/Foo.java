import com.example.services.FooFighter;
import com.example.wepons.Fire;
<caret>
public class Foo{

    private FooFighter fooFighter;

    public String fight(Fire withFire,String someFoe) {
        return fooFighter.fight(withFire);
    }

}
