import java.util.HashSet;
import java.util.Set;

public abstract class GameCharacter implements Combatant, Upgradable, Comparable<GameCharacter> {
    private String name;
    private String game;
    private int level;
    private int health;
    private int attackPower;
    private boolean isAlive;

    private Set<Player> owners = new HashSet<>();

    public GameCharacter() {
        this.name = "Unknown";
        this.game = "Unknown Game";
        this.level = 1;
        this.health = 100;
        this.attackPower = 10;
        this.isAlive = true;
    }

    public GameCharacter(String name, String game, int level, int health, int attackPower, boolean isAlive) {
        this.name = name;
        this.game = game;
        this.level = level;
        this.health = health;
        this.attackPower = attackPower;
        this.isAlive = isAlive;
    }

    public abstract void performSpecialAction();
    public abstract int calculatePower();

    public String getName() {
        return name;
    }
    public int getLevel(){return level;}

    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        }
    }

    public String getGame() {
        return game;
    }

    public void setLevel(int level) {
        if (level > 0) {
            this.level = level;
        }
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
        if (this.health <= 0) {
            this.health = 0;
            this.isAlive = false;
        } else {
            this.isAlive = true;
        }
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(int attackPower) {
        if (attackPower >= 0) {
            this.attackPower = attackPower;
        }
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public Set<Player> getOwners() {
        return owners;
    }

    @Override
    public void attack() {
        System.out.println(name + " attacks with power " + attackPower + "!");
    }

    @Override
    public void takeDamage(int damage) {
        if (damage > 0) {
            setHealth(this.health - damage);
            System.out.println(name + " took " + damage + " damage! Remaining health: " + health);
        }
    }

    @Override
    public void levelUp() {
        this.level++;
        this.attackPower += 5;
        System.out.println(name + " leveled up to level " + level + "!");
    }

    @Override
    public void heal(int amount) {
        if (amount > 0 && isAlive) {
            setHealth(this.health + amount);
            System.out.println(name + " healed for " + amount + " HP. Current health: " + health);
        }
    }

    public void displayInfo() {
        System.out.println("Name: " + name + ", Game: " + game + ", Level: " + level +
                ", Health: " + health + ", Attack: " + attackPower + ", Alive: " + isAlive);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameCharacter other = (GameCharacter) o;
        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public int compareTo(GameCharacter other) {
        return this.name.compareTo(other.name);
    }
}
