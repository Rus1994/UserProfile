package com.company;

import java.text.SimpleDateFormat;
import java.util.*;

interface Warrior {
    int attack();

    void takeDamage(int damage);

    boolean isAlive();

    void setSquadName(String name);
}

abstract class AbsWarrior implements Warrior {
    protected int health;
    protected int damage;
    protected String nameSquad;
    protected String nameClass;
    protected int IDwarrior = 0;

    AbsWarrior(String nameSquad) {
        this.nameSquad = nameSquad;
    }

    @Override
    public int attack() {
        return damage;
    }

    @Override
    public void takeDamage(int damage) {
        health -= damage;
    }

    @Override
    public boolean isAlive() {
        return health > 0;
    }

    @Override
    public void setSquadName(String name) {
        nameSquad = name;
    }

    @Override
    public String toString() {
        return nameClass + " №" + IDwarrior + " Отряд: " + nameSquad;
    }
}

class Viking extends AbsWarrior {
    static int counterID = 0;

    Viking(String nameSquad) {
        super(nameSquad);
        counterID++;
        IDwarrior = counterID;
        nameClass = "Викинг";
        health = 100;
        damage = 50;
    }
}

class Knight extends AbsWarrior {
    static int counterID = 0;

    Knight(String nameSquad) {
        super(nameSquad);
        counterID++;
        IDwarrior = counterID;
        nameClass = "Рыцарь";
        health = 110;
        damage = 40;
    }
}

class Squad implements Cloneable{
    private Vector<Warrior> warriorsVector;
    private String nameSquad;
    private int quanVikings;
    private int quanKnights;

    Squad(String nameSquad, int quanVikings, int quanKnights) {
        this.nameSquad = nameSquad;
        createWarriorsList(quanVikings, quanKnights);
    }

    public Squad(Squad obj) {
        this.nameSquad = obj.nameSquad;
        this.quanVikings = obj.quanVikings;
        this.quanKnights = obj.quanKnights;
        createWarriorsList(obj.quanVikings, obj.quanKnights);
    }

    public String getNameSquad() {
        return nameSquad;
    }

    public Warrior getRandomWarrior() {
        Warrior war;
        do {
            int numWar = (int) (Math.random() * warriorsVector.size());
            war = warriorsVector.get(numWar);
        } while (!war.isAlive());
        return war;
    }

    public boolean hasAliveWarriors() {
        for (Warrior war : warriorsVector) {
            if (war.isAlive())
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Отряд: " + nameSquad + "\n";
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Squad(this);
    }

    private void createWarriorsList(int quanVikings, int quanKnights) {
        warriorsVector = new Vector<Warrior>();
        for (int i = 0; i < quanVikings; i++)
            warriorsVector.add(new Viking(nameSquad));
        for (int i = 0; i < quanKnights; i++)
            warriorsVector.add(new Knight(nameSquad));
    }
}

class DateHelper {
    private Date currentTime;
    private GregorianCalendar startCal;
    private GregorianCalendar currentCal;

    DateHelper() {
        startCal = new GregorianCalendar();
        startCal.add(Calendar.YEAR, -1500);
        currentCal = (GregorianCalendar) startCal.clone();
    }

    public String getFormattedStartDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd-MMM-y");
        return formatter.format(startCal.getTime());
    }

    public void skipTime() {
        currentCal.add(Calendar.MINUTE, 45);
    }

    public String getFormattedDiff() {
        currentTime = currentCal.getTime();
        long diffTime = currentCal.getTime().getTime() - startCal.getTime().getTime();
        System.out.println(diffTime);
        currentCal.add(Calendar.YEAR, (-startCal.get(Calendar.YEAR)));
        currentCal.add(Calendar.MONTH, -startCal.get(Calendar.MONTH));
        currentCal.add(Calendar.DAY_OF_MONTH, -startCal.get(Calendar.DAY_OF_MONTH) + 1);
        currentCal.add(Calendar.HOUR_OF_DAY, -startCal.get(Calendar.HOUR_OF_DAY));
        currentCal.add(Calendar.MINUTE, -startCal.get(Calendar.MINUTE));

        int y = currentCal.get(Calendar.YEAR) - 1;
        int m = currentCal.get(Calendar.MONTH);
        int d = currentCal.get(Calendar.DAY_OF_MONTH) - 1;
        int h = currentCal.get(Calendar.HOUR_OF_DAY);
        int min = currentCal.get(Calendar.MINUTE);

        return h + ":" + min + "  " + d + "-" + m + "-" + y;
    }
}

class Battle {
    private Scanner scan;
    private Squad squad1;
    private Squad squad2;
    private int countWar;

    Battle() {
        scan = new Scanner(System.in);
    }

    public void fight() {
        boolean attackFirst = true;
        boolean isFinish = false;
        AbsWarrior war1, war2;
        createSquads();
        DateHelper dataManager = new DateHelper();
        System.out.println(dataManager.getFormattedStartDate());
        while (!isFinish) {
            war1 = (AbsWarrior) squad1.getRandomWarrior();
            war2 = (AbsWarrior) squad2.getRandomWarrior();
            if (attackFirst) {
                war2.takeDamage(war1.attack());
                attackFirst = false;
                if (!squad2.hasAliveWarriors())
                    isFinish = true;
                System.out.println(war1.toString() + " атаковал " + war2.toString());
            } else {
                war1.takeDamage(war2.attack());
                attackFirst = true;
                if (!squad1.hasAliveWarriors())
                    isFinish = true;
                System.out.println(war2.toString() + " атаковал " + war1.toString());
            }
            dataManager.skipTime();
        }
        boolean winnerFirst = !attackFirst;
        Squad winner = winnerFirst ? squad1 : squad2;
        System.out.println("Победитель --> " + winner.getNameSquad());
        System.out.println("Время битвы = " + dataManager.getFormattedDiff());
    }

    private Squad createSquad() {
        System.out.print("Введите название отряда: ");
        String name = scan.nextLine();
        int countVik = 0;
        do {
            if ((countVik > countWar) || (countVik < 0)){
                System.out.println("Количество Викингов не может быть меньше 0 или превышать бойцов в отряде");
            }
            countVik = getIntFromConsole("Введите количество Викингов в отряде1, остальные будут Рыцари: ");
        } while ((countVik > countWar) || (countVik < 0));
        return new Squad(name, countVik, countWar - countVik);
    }

    private Squad getNewSquad(String message) {
        System.out.println(message);
        return createSquad();
    }

    private void createSquads() {
        countWar = 1;
        do {
            if (countWar < 1)
                System.out.println("Количество бойцов не может быть меньше 1");

            countWar = getIntFromConsole("Введите количество бойцов в отряде: ");
        } while (countWar < 1);

        squad1 = getNewSquad("Создание первого отряда");
        squad2 = getNewSquad("Создание второго отряда");
    }

    private int getIntFromConsole(String message) {
        System.out.println(message);
        int num = scan.nextInt();
        scan.nextLine();
        return num;
    }
}

public class Homework4 {
    public void run() {
        Battle battle = new Battle();
        battle.fight();
    }
}
