public class Boss extends GameCharacter {
    private String ultimateMove;

    public Boss(String name, String game, int level, int health, int attackPower, boolean isAlive, String ultimateMove) {
        super(name, game, level, health, attackPower, isAlive);
        this.ultimateMove = ultimateMove;
    }

    public String getUltimateMove() {
        return ultimateMove;
    }

    public void setUltimateMove(String ultimateMove) {
        this.ultimateMove = ultimateMove;
    }

    @Override
    public void performSpecialAction() {
        System.out.println(getName() + " triggers ultimate boss move: " + ultimateMove + "!");
    }

    @Override
    public int calculatePower() {
        return getLevel() * getAttackPower() * 2;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Role: Boss, Ultimate Move: " + ultimateMove);
    }
}
