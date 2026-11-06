public class BattleRequest {
    private int id;
    private Player player;
    private GameCharacter attacker;
    private GameCharacter defender;

    public BattleRequest(int id, Player player, GameCharacter attacker, GameCharacter defender) {
        this.id = id;
        this.player = player;
        this.attacker = attacker;
        this.defender = defender;
    }

    public int getId() { return id; }
    public Player getPlayer() { return player; }
    public GameCharacter getAttacker() { return attacker; }
    public GameCharacter getDefender() { return defender; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BattleRequest other = (BattleRequest) o;
        return id == other.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "Заявка #" + id + ": " + player.getNickname() + " (" + attacker.getName()
                + " -> " + defender.getName() + ")";
    }
}
