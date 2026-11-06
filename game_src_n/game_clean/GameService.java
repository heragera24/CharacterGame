import java.util.*;

public class GameService {

    private List<GameCharacter> characterList = new ArrayList<>();
    private Set<Player> playerSet = new HashSet<>();

    private Map<String, GameCharacter> characterByName = new HashMap<>();
    private Map<String, Player> playerByNickname = new HashMap<>();
    private Map<String, List<GameCharacter>> charactersByGame = new HashMap<>();

    private Queue<BattleRequest> queue = new ArrayDeque<>();
    private int nextRequestId = 1;

    public void addCharacter(GameCharacter c) throws GameException {
        if (characterByName.containsKey(c.getName())) {
            throw new GameException("Персонаж " + c.getName() + " уже существует");
        }
        characterList.add(c);
        characterByName.put(c.getName(), c);
        addToGame(c);
    }

    public GameCharacter findCharacter(String name) throws GameException {
        GameCharacter c = characterByName.get(name);
        if (c == null) {
            throw new GameException("Персонаж " + name + " не найден");
        }
        return c;
    }

    public void changeGame(String name, String newGame) throws GameException {
        GameCharacter c = findCharacter(name);
        removeFromGame(c);
        c.setName(newGame);
        addToGame(c);
    }

    public void removeCharacter(String name) throws GameException {
        GameCharacter c = findCharacter(name);
        characterList.remove(c);
        characterByName.remove(name);
        removeFromGame(c);
        for (Player p : c.getOwners()) {
            p.getCollection().remove(c);
        }
        Iterator<BattleRequest> it = queue.iterator();
        while (it.hasNext()) {
            BattleRequest req = it.next();
            if (req.getAttacker().equals(c) || req.getDefender().equals(c)) {
                it.remove();
            }
        }
    }

    private void addToGame(GameCharacter c) {
        List<GameCharacter> group = charactersByGame.get(c.getGame());
        if (group == null) {
            group = new ArrayList<>();
            charactersByGame.put(c.getGame(), group);
        }
        group.add(c);
    }

    private void removeFromGame(GameCharacter c) {
        List<GameCharacter> group = charactersByGame.get(c.getGame());
        if (group != null) {
            group.remove(c);
            if (group.isEmpty()) {
                charactersByGame.remove(c.getGame());
            }
        }
    }

    public void addPlayer(Player p) throws GameException {

        if (!playerSet.add(p)) {
            throw new GameException("Игрок " + p.getNickname() + " уже существует");
        }
        playerByNickname.put(p.getNickname(), p);
    }

    public Player findPlayer(String nickname) throws GameException {
        Player p = playerByNickname.get(nickname);
        if (p == null) {
            throw new GameException("Игрок " + nickname + " не найден");
        }
        return p;
    }

    public void removePlayer(String nickname) throws GameException {
        Player p = findPlayer(nickname);
        playerSet.remove(p);
        playerByNickname.remove(nickname);
        for (GameCharacter c : p.getCollection()) {
            c.getOwners().remove(p);
        }
        Iterator<BattleRequest> it = queue.iterator();
        while (it.hasNext()) {
            if (it.next().getPlayer().equals(p)) {
                it.remove();
            }
        }
    }

    public void giveCharacter(String nickname, String characterName) throws GameException {
        Player p = findPlayer(nickname);
        GameCharacter c = findCharacter(characterName);
        p.getCollection().add(c);
        c.getOwners().add(p);
    }

    public List<GameCharacter> getCharactersByGame(String game) {
        List<GameCharacter> result = charactersByGame.get(game);
        if (result == null) {
            return new ArrayList<>();
        }
        return result;
    }

    public List<GameCharacter> filterByMinLevel(int minLevel) {
        List<GameCharacter> result = new ArrayList<>();
        for (GameCharacter c : characterList) {
            if (c.getLevel() >= minLevel) {
                result.add(c);
            }
        }
        return result;
    }

    public BattleRequest submitBattle(String nickname, String attackerName, String defenderName)
            throws GameException {
        Player p = findPlayer(nickname);
        GameCharacter attacker = findCharacter(attackerName);
        GameCharacter defender = findCharacter(defenderName);
        if (!p.getCollection().contains(attacker)) {
            throw new GameException(nickname + " не владеет персонажем " + attackerName);
        }
        BattleRequest req = new BattleRequest(nextRequestId++, p, attacker, defender);
        queue.add(req);
        return req;
    }

    public String processNextBattle() {
        BattleRequest req = queue.poll();
        if (req == null) {
            return "Очередь пуста";
        }
        GameCharacter attacker = req.getAttacker();
        GameCharacter defender = req.getDefender();
        if (!attacker.isAlive() || !defender.isAlive()) {
            return "Отказ: в бою могут участвовать только живые (" + req + ")";
        }
        int damage = attacker.calculatePower();
        attacker.attack();
        defender.takeDamage(damage);
        String result = attacker.getName() + " ударил " + defender.getName() + " на " + damage;
        if (!defender.isAlive()) {
            attacker.levelUp();
            result = result + ". " + defender.getName() + " повержен";
        }
        req.getPlayer().getLog().addRecord(result);
        return result;
    }

    public int queueSize() {
        return queue.size();
    }

    public List<GameCharacter> getSorted(Comparator<GameCharacter> comparator) {
        List<GameCharacter> copy = new ArrayList<>(characterList);
        Collections.sort(copy, comparator);
        return copy;
    }

    public List<GameCharacter> getSortedNatural() {
        List<GameCharacter> copy = new ArrayList<>(characterList);
        Collections.sort(copy);
        return copy;
    }

    public void printReport() {
        System.out.println("Персонажей: " + characterList.size() + ", игроков: " + playerSet.size()
                + ", в очереди: " + queue.size());
        for (String game : charactersByGame.keySet()) {
            List<GameCharacter> group = charactersByGame.get(game);
            int alive = 0;
            int totalPower = 0;
            for (GameCharacter c : group) {
                if (c.isAlive()) {
                    alive++;
                }
                totalPower += c.calculatePower();
            }
            System.out.println("  " + game + ": персонажей " + group.size() + ", живых " + alive
                    + ", средняя сила " + (totalPower / group.size()));
        }
    }
}
