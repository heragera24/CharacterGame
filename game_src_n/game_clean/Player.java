import java.util.HashSet;
import java.util.Set;

public class Player {
    private String nickname;

    private BattleLog log = new BattleLog();

    private Set<GameCharacter> collection = new HashSet<>();

    public Player(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() { return nickname; }
    public BattleLog getLog() { return log; }
    public Set<GameCharacter> getCollection() { return collection; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player other = (Player) o;
        return nickname.equals(other.nickname);
    }

    @Override
    public int hashCode() {
        return nickname.hashCode();
    }

    @Override
    public String toString() {
        return "Игрок " + nickname;
    }
}
