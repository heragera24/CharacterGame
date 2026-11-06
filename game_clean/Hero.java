public class Hero extends GameCharacter {
    private String specialAbility;

    public Hero(String name, String game, int level, int health, int attackPower, boolean isAlive, String specialAbility) {
        super(name, game, level, health, attackPower, isAlive);
        this.specialAbility = specialAbility;
    }

    public String getSpecialAbility() {
        return specialAbility;
    }

    public void setSpecialAbility(String specialAbility) {
        this.specialAbility = specialAbility;
    }

    public void useSpecialAbility() {
        System.out.println(getName() + " uses special ability: " + specialAbility + "!");
    }

    @Override
    public void performSpecialAction() {
        useSpecialAbility();
    }

    @Override
    public int calculatePower() {
        return getLevel() * getAttackPower() + 50;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Role: Hero, Special Ability: " + specialAbility);
    }
}
