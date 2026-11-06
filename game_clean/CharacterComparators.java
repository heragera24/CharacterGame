import java.util.Comparator;

public class CharacterComparators {

    public static class ByPower implements Comparator<GameCharacter> {
        @Override
        public int compare(GameCharacter a, GameCharacter b) {
            return Integer.compare(b.calculatePower(), a.calculatePower());
        }
    }

    public static class ByLevel implements Comparator<GameCharacter> {
        @Override
        public int compare(GameCharacter a, GameCharacter b) {
            return Integer.compare(b.getLevel(), a.getLevel());
        }
    }

    public static class ByGameThenName implements Comparator<GameCharacter> {
        @Override
        public int compare(GameCharacter a, GameCharacter b) {
            int result = a.getGame().compareTo(b.getGame());
            if (result != 0) {
                return result;
            }
            return a.getName().compareTo(b.getName());
        }
    }
}
