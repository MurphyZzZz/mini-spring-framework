package container.fixture;

import com.thoughtworks.fusheng.integration.junit5.FuShengTest;
import org.junit.jupiter.api.Test;

@FuShengTest
public class ExampleTest {
    int minWager;
    String players;
    int pot = 0;

    @Test
    void name() {

    }

    public void newGame(String players){
        this.players = players;
    }

    public void setMinWager(int minWager) {
        this.minWager = minWager;
    }

    public void bet() {
        this.pot += minWager;
    }

    public void raise(int wager) {
        this.pot += (minWager + wager);
    }

    public int getPot(){
        return this.pot;
    }
}
