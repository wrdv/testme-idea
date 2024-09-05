import com.example.foes.Pokemon;


/** Test input class*/
public class HunterImpl implements Hunter {
    @Override
    public DeadOrAlive hunt(Pokemon pokey) {
        return new DeadOrAlive();
    }
}
