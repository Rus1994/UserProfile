package com.company;

import java.util.Vector;

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
        health = 70;
        damage = 70;
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
        health = 100;
        damage = 65;
    }
}
class Squad
{
    Vector<Warrior> warriorsList;
    public Warrior getRandomWarrior(){
        int numWar = (int) Math.random()*warriorsList.size();
        return warriorsList.get(numWar);
    }
    public boolean hasAliveWarriors(){
        return warriorsList.size() > 0;
    }
    String nameSquad;

    @Override
    public String toString() {
        return "Отряд: " + nameSquad + "\n" + "Осталось бойцов: " + warriorsList.size();
    }
}
class DateHelper
{
    public String getFormattedStartDate(){
        return "";
    }
    public void skipTime(){}
    public String getFormattedDiff()
    {
        return "";
    }
}
class Battle
{

}

public class Homework4 {
    static void run()
    {
        Viking war1 = new Viking("red");
        Knight war2 = new Knight("green");
        Viking war3 = new Viking("green");
        System.out.println(war1.toString());
        System.out.println(war2.toString());
        System.out.println(war3.toString());
    }
}
