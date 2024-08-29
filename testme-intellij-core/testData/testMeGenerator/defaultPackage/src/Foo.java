
import com.example.warriers.Hunter;
import com.example.foes.Fire;
import com.example.foes.Pokemon;

public class Foo{
<caret>
    private Hunter hunter;

    public String fight(Fire withFire,Flames andFlames,Pokemon pokey) {
        return hunter.hunt(pokey);
    }

}
