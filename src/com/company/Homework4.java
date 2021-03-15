package com.company;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

interface Warrior
{
    int attack();
    void takeDamage(int damage);
    boolean isAlive();
    void setSquadName(String name);
}
abstract class AbsWarrior implements Warrior
{
    protected int health;
    protected int damage;
    protected String nameSquad;
    protected String nameClass;
    protected int IDwarrior = 0;

    AbsWarrior(String _nameSquad){
        nameSquad = _nameSquad;
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
class Viking extends AbsWarrior
{
    static int counterID = 0;
    Viking(String _nameSquad){
        super(_nameSquad);
        counterID++;
        IDwarrior = counterID;
        nameClass = "Викинг";
        health = 100;
        damage = 50;
    }
}
class Knight extends  AbsWarrior
{
    static int counterID = 0;
    Knight(String _nameSquad){
        super(_nameSquad);
        counterID++;
        IDwarrior = counterID;
        nameClass = "Рыцарь";
        health = 110;
        damage = 40;
    }
}
class Squad
{
    Vector<Warrior> warriorsList;
    String nameSquad;
    int quanVikings;
    int quanKnights;
    private void createWarriorsList(int _quanVikings, int _quanKnights)
    {
        warriorsList = new Vector<Warrior>();
        for(int i = 0; i < _quanVikings; i++)
            warriorsList.add(new Viking(nameSquad));
        for(int i = 0; i < _quanKnights; i++)
            warriorsList.add(new Knight(nameSquad));
    }
    Squad(String _nameSquad, int _quanVikings, int _quanKnights)
    {
        nameSquad = _nameSquad;
        createWarriorsList(_quanVikings, _quanKnights);
    }
    public Squad(Squad obj)
    {
        this.nameSquad = obj.nameSquad;
        this.quanVikings = obj.quanVikings;
        this.quanKnights = obj.quanKnights;
        createWarriorsList(obj.quanVikings, obj.quanKnights);
    }
    public Warrior getRandomWarrior(){
        Warrior war;
        do {
            int numWar = (int) (Math.random() * warriorsList.size());
            war = warriorsList.get(numWar);
        }while(!war.isAlive());
        return war;
    }
    public boolean hasAliveWarriors(){
        for (Warrior war : warriorsList) {
            if(war.isAlive())
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
}
class DateHelper
{
    Date startTime;
    Date currentTime;
    GregorianCalendar startCal;
    GregorianCalendar currentCal;
    DateHelper()
    {
        startCal = new GregorianCalendar();
        startCal.add(Calendar.YEAR, -1500);
        currentCal = (GregorianCalendar) startCal.clone();
    }
    public String getFormattedStartDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd-MMM-y");
        return formatter.format(startCal.getTime());
    }
    public void skipTime(){
        currentCal.add(Calendar.MINUTE, 45);
    }
    public String getFormattedDiff()
    {
        currentTime = currentCal.getTime();
        long diffTime = currentCal.getTime().getTime() - startCal.getTime().getTime();
        System.out.println(diffTime);
        currentCal.add(Calendar.YEAR,(-startCal.get(Calendar.YEAR)));
        currentCal.add(Calendar.MONTH,-startCal.get(Calendar.MONTH));
        currentCal.add(Calendar.DAY_OF_MONTH,-startCal.get(Calendar.DAY_OF_MONTH)+1);
        currentCal.add(Calendar.HOUR_OF_DAY,-startCal.get(Calendar.HOUR_OF_DAY));
        currentCal.add(Calendar.MINUTE,-startCal.get(Calendar.MINUTE));

        int y = currentCal.get(Calendar.YEAR)-1;
        int m = currentCal.get(Calendar.MONTH);
        int d = currentCal.get(Calendar.DAY_OF_MONTH)-1;
        int h = currentCal.get(Calendar.HOUR_OF_DAY);
        int min = currentCal.get(Calendar.MINUTE);

        return h + ":" + min + "  " + d + "-" + m + "-" + y;
    }
}
class Battle
{
    Scanner scan;
    Squad squad1;
    Squad squad2;
    int countWar;
    Battle()
    {
        scan = new Scanner(System.in);
    }
    private Squad createSquad()
    {
        System.out.print("Введите название отряда: ");
        String name = scan.nextLine();
        int countVik = 0;
        do {
            if((countVik > countWar) || (countVik < 0))
                System.out.println("Количество Викингов не может быть меньше 0 или превышать бойцов в отряде");

            System.out.print("Введите количество Викингов в отряде1, остальные будут Рыцари: ");
            countVik = scan.nextInt();
            scan.nextLine();
        }while((countVik > countWar) || (countVik < 0));
        return new Squad(name, countVik, countWar-countVik);
    }
    public void createSquads()
    {
        countWar = 1;
        do {
            if(countWar < 1)
                System.out.println("Количество бойцов не может быть меньше 1");

            System.out.print("Введите количество бойцов в отряде: ");
            countWar = scan.nextInt();
            scan.nextLine();
        }while(countWar < 1);

        System.out.println("Создание первого отряда");
        squad1 = createSquad();
        System.out.println("Создание второго отряда");
        squad2 = createSquad();
    }
    public void fight()
    {
        boolean attackFirst = true;
        boolean isFinish = false;
        AbsWarrior war1, war2;
        createSquads();
        DateHelper dataManag = new DateHelper();
        System.out.println(dataManag.getFormattedStartDate());
        while(!isFinish)
        {
            war1 = (AbsWarrior) squad1.getRandomWarrior();
            war2 = (AbsWarrior) squad2.getRandomWarrior();
            if(attackFirst)
            {
                war2.takeDamage(war1.attack());
                attackFirst = false;
                if(!squad2.hasAliveWarriors())
                    isFinish = true;
                System.out.println(war1.toString() + " атаковал " + war2.toString());
            }
            else
            {
                war1.takeDamage(war2.attack());
                attackFirst = true;
                if(!squad1.hasAliveWarriors())
                    isFinish = true;
                System.out.println(war2.toString() + " атаковал " + war1.toString());
            }
            dataManag.skipTime();
        }
        boolean winnerFirst = !attackFirst;
        Squad winner = winnerFirst ? squad1 : squad2;
        System.out.println("Победитель --> " + winner.nameSquad);
        System.out.println("Время битвы = " + dataManag.getFormattedDiff());
    }
}

public class Homework4 {
    static void run()
    {
        Battle battle = new Battle();
        battle.fight();
//        GregorianCalendar cal1 = new GregorianCalendar();
//        GregorianCalendar cal2 = new GregorianCalendar();
//        cal2.add(Calendar.MINUTE, 45);
//
//        cal2.add(Calendar.YEAR,(-cal2.get(Calendar.YEAR)));
//        cal2.add(Calendar.MONTH,-cal1.get(Calendar.MONTH));
//        cal2.add(Calendar.DAY_OF_MONTH,-cal1.get(Calendar.DAY_OF_MONTH)+1);
//        cal2.add(Calendar.HOUR_OF_DAY,-cal1.get(Calendar.HOUR_OF_DAY));
//        cal2.add(Calendar.MINUTE,-cal1.get(Calendar.MINUTE));
//
//        int y = cal2.get(Calendar.YEAR)-1;
//        int m = cal2.get(Calendar.MONTH);
//        int d = cal2.get(Calendar.DAY_OF_MONTH)-1;
//        int h = cal2.get(Calendar.HOUR_OF_DAY);
//        int min = cal2.get(Calendar.MINUTE);
//        System.out.println(h + ":" + min + "  " + d + "-" + m + "-" + y);
    }
}
