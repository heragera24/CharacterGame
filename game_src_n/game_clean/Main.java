import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    static void printList(String title, List<GameCharacter> list) {
        System.out.println("\n--- " + title + " ---");
        if (list.isEmpty()) {
            System.out.println("(пусто)");
        }
        for (GameCharacter c : list) {
            c.displayInfo();
        }
    }

    static String ask(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    static int askInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(ask(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Нужно ввести число. Попробуйте снова.");
            }
        }
    }

    static boolean askBool(String prompt) {
        while (true) {
            String s = ask(prompt).toLowerCase();
            if (s.equals("true") || s.equals("да") || s.equals("1") || s.equals("y")) return true;
            if (s.equals("false") || s.equals("нет") || s.equals("0") || s.equals("n")) return false;
            System.out.println("Введите true/false (или да/нет).");
        }
    }

    public static void main(String[] args) {
        GameService service = new GameService();

        boolean running = true;
        while (running) {
            System.out.println("\n========== ГЛАВНОЕ МЕНЮ ==========");
            System.out.println("1. Управление персонажами");
            System.out.println("2. Управление игроками");
            System.out.println("3. Бои");
            System.out.println("4. Просмотр и сортировки");
            System.out.println("5. Отчёты");
            System.out.println("0. Выход");
            System.out.print("Выбор: ");

            switch (ask("")) {
                case "1": characterMenu(service); break;
                case "2": playerMenu(service); break;
                case "3": battleMenu(service); break;
                case "4": viewMenu(service); break;
                case "5": reportMenu(service); break;
                case "0": running = false; System.out.println("Выход."); break;
                default: System.out.println("Неверный выбор.");
            }
        }
        scanner.close();
    }

    // ================= ПЕРСОНАЖИ =================
    static void characterMenu(GameService service) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- УПРАВЛЕНИЕ ПЕРСОНАЖАМИ ---");
            System.out.println("1. Добавить героя");
            System.out.println("2. Добавить босса");
            System.out.println("3. Найти персонажа по имени");
            System.out.println("4. Изменить игру персонажа");
            System.out.println("5. Удалить персонажа");
            System.out.println("6. Применить урон/лечение/повышение уровня");
            System.out.println("0. Назад");
            System.out.print("Выбор: ");

            try {
                switch (ask("")) {
                    case "1": {
                        String name = ask("Имя: ");
                        String game = ask("Игра: ");
                        int level = askInt("Уровень: ");
                        int health = askInt("Здоровье: ");
                        int attack = askInt("Сила атаки: ");
                        boolean alive = askBool("Живой (true/false): ");
                        String ability = ask("Способность: ");
                        service.addCharacter(new Hero(name, game, level, health, attack, alive, ability));
                        System.out.println("Герой добавлен.");
                        break;
                    }
                    case "2": {
                        String name = ask("Имя: ");
                        String game = ask("Игра: ");
                        int level = askInt("Уровень: ");
                        int health = askInt("Здоровье: ");
                        int attack = askInt("Сила атаки: ");
                        boolean alive = askBool("Живой (true/false): ");
                        String ult = ask("Ультимейт: ");
                        service.addCharacter(new Boss(name, game, level, health, attack, alive, ult));
                        System.out.println("Босс добавлен.");
                        break;
                    }
                    case "3": {
                        service.findCharacter(ask("Имя персонажа: ")).displayInfo();
                        break;
                    }
                    case "4": {
                        String name = ask("Имя персонажа: ");
                        String newGame = ask("Новая игра: ");
                        service.changeGame(name, newGame);
                        System.out.println("Игра изменена.");
                        break;
                    }
                    case "5": {
                        service.removeCharacter(ask("Имя персонажа: "));
                        System.out.println("Персонаж удалён.");
                        break;
                    }
                    case "6": {
                        GameCharacter g = service.findCharacter(ask("Имя персонажа: "));
                        System.out.println("1. Урон  2. Лечение  3. Повысить уровень");
                        switch (ask("Действие: ")) {
                            case "1": g.takeDamage(askInt("Урон: ")); break;
                            case "2": g.heal(askInt("Лечение: ")); break;
                            case "3": g.levelUp(); break;
                            default: System.out.println("Отмена.");
                        }
                        break;
                    }
                    case "0": back = true; break;
                    default: System.out.println("Неверный выбор.");
                }
            } catch (GameException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    // ================= ИГРОКИ =================
    static void playerMenu(GameService service) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- УПРАВЛЕНИЕ ИГРОКАМИ ---");
            System.out.println("1. Добавить игрока");
            System.out.println("2. Выдать персонажа игроку");
            System.out.println("3. Удалить игрока");
            System.out.println("4. Показать журнал игрока");
            System.out.println("5. Показать коллекцию игрока");
            System.out.println("0. Назад");
            System.out.print("Выбор: ");

            try {
                switch (ask("")) {
                    case "1": {
                        service.addPlayer(new Player(ask("Никнейм: ")));
                        System.out.println("Игрок добавлен.");
                        break;
                    }
                    case "2": {
                        String nick = ask("Никнейм игрока: ");
                        String charName = ask("Имя персонажа: ");
                        service.giveCharacter(nick, charName);
                        System.out.println("Персонаж выдан.");
                        break;
                    }
                    case "3": {
                        service.removePlayer(ask("Никнейм игрока: "));
                        System.out.println("Игрок удалён.");
                        break;
                    }
                    case "4": {
                        String nick = ask("Никнейм игрока: ");
                        System.out.println("Журнал " + nick + ": "
                                + service.findPlayer(nick).getLog().getRecords());
                        break;
                    }
                    case "5": {
                        String nick = ask("Никнейм игрока: ");
                        printList("Коллекция " + nick,
                                new java.util.ArrayList<>(service.findPlayer(nick).getCollection()));
                        break;
                    }
                    case "0": back = true; break;
                    default: System.out.println("Неверный выбор.");
                }
            } catch (GameException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    // ================= БОИ =================
    static void battleMenu(GameService service) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- БОИ ---");
            System.out.println("1. Подать заявку на бой");
            System.out.println("2. Обработать следующую заявку");
            System.out.println("3. Обработать все заявки");
            System.out.println("4. Показать размер очереди");
            System.out.println("0. Назад");
            System.out.print("Выбор: ");

            try {
                switch (ask("")) {
                    case "1": {
                        String nick = ask("Никнейм игрока: ");
                        String attacker = ask("Атакующий: ");
                        String defender = ask("Защитник: ");
                        System.out.println(service.submitBattle(nick, attacker, defender));
                        break;
                    }
                    case "2": {
                        System.out.println(">> " + service.processNextBattle());
                        break;
                    }
                    case "3": {
                        if (service.queueSize() == 0) {
                            System.out.println("Очередь пуста.");
                        }
                        while (service.queueSize() > 0) {
                            System.out.println(">> " + service.processNextBattle());
                        }
                        break;
                    }
                    case "4": {
                        System.out.println("В очереди: " + service.queueSize());
                        break;
                    }
                    case "0": back = true; break;
                    default: System.out.println("Неверный выбор.");
                }
            } catch (GameException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    // ================= ПРОСМОТР И СОРТИРОВКИ =================
    static void viewMenu(GameService service) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- ПРОСМОТР И СОРТИРОВКИ ---");
            System.out.println("1. Все персонажи (естественный порядок, по имени)");
            System.out.println("2. Сортировка по силе (убывание)");
            System.out.println("3. Сортировка по уровню (убывание)");
            System.out.println("4. Сортировка по игре, затем по имени");
            System.out.println("5. Персонажи по игре");
            System.out.println("6. Фильтр по минимальному уровню");
            System.out.println("0. Назад");
            System.out.print("Выбор: ");

            switch (ask("")) {
                case "1":
                    printList("Естественный порядок (по имени)", service.getSortedNatural());
                    break;
                case "2":
                    printList("По силе", service.getSorted(new CharacterComparators.ByPower()));
                    break;
                case "3":
                    printList("По уровню", service.getSorted(new CharacterComparators.ByLevel()));
                    break;
                case "4":
                    printList("По игре, потом по имени",
                            service.getSorted(new CharacterComparators.ByGameThenName()));
                    break;
                case "5": {
                    String game = ask("Игра: ");
                    printList(game + " (индекс по игре)", service.getCharactersByGame(game));
                    break;
                }
                case "6": {
                    int min = askInt("Минимальный уровень: ");
                    printList("Уровень >= " + min, service.filterByMinLevel(min));
                    break;
                }
                case "0": back = true; break;
                default: System.out.println("Неверный выбор.");
            }
        }
    }

    // ================= ОТЧЁТЫ =================
    static void reportMenu(GameService service) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- ОТЧЁТЫ ---");
            System.out.println("1. Общий отчёт");
            System.out.println("2. Журнал игрока");
            System.out.println("0. Назад");
            System.out.print("Выбор: ");

            try {
                switch (ask("")) {
                    case "1":
                        System.out.println("\n--- Отчёт ---");
                        service.printReport();
                        break;
                    case "2": {
                        String nick = ask("Никнейм игрока: ");
                        System.out.println("Журнал " + nick + ": "
                                + service.findPlayer(nick).getLog().getRecords());
                        break;
                    }
                    case "0": back = true; break;
                    default: System.out.println("Неверный выбор.");
                }
            } catch (GameException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }
}