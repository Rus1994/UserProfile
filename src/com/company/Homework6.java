package com.company;

import java.util.*;

enum League{
    BEGINNER,
    MIDDLE,
    PRO
}
enum Country{
    RUSSIA,
    USA,
    ENGLAND,
    SPAIN,
    GERMAN
}

interface SoccerPlayerInter {
    //  Получение ФИО игрока
    String getNickName();
    // Рейтинг игрока
    Integer getPoints();
    // Лига игрока
    League getLeague();
    // Страна происхождения
    Country getCountry();
}

interface LeagueManagerInter {
    public void addPlayer(SoccerPlayerInter player);
    public void removePlayer(SoccerPlayerInter player);
    public SoccerPlayerInter getPlayer(String name);
    public SoccerPlayerInter[] getAllPlayers();
    public SoccerPlayerInter[] getPlayers(League league);
    public SoccerPlayerInter[] getPlayers(Country country);
    public void addPoints(String name, int points);
}

class SoccerPlayer implements SoccerPlayerInter{
    private final String nickName;
    private final Country country;
    private League league;
    private Integer points;

    SoccerPlayer(String nickName, Country country, League league, Integer points){
        this.nickName = nickName;
        this.country = country;
        this.league = league;
        this.points = points;
    }

    public void setLeague(League league){
        this.league = league;
    }
    public void addPoints(Integer points){
        this.points += points;
    }

    @Override
    public String getNickName() {
        return nickName;
    }
    @Override
    public Integer getPoints() {
        return points;
    }
    @Override
    public League getLeague() {
        return league;
    }
    @Override
    public Country getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return  "nickName='" + nickName +
                ", country=" + country +
                ", league=" + league +
                ", points=" + points +
                '\n';
    }
}

class LeagueManager implements LeagueManagerInter{
    private Map<String, SoccerPlayer> players = new HashMap<String, SoccerPlayer>();

    @Override
    public void addPlayer(SoccerPlayerInter player) {
        players.put(player.getNickName(), (SoccerPlayer) player);
    }

    @Override
    public void removePlayer(SoccerPlayerInter player) {
        players.remove(player.getNickName());
    }

    @Override
    public SoccerPlayerInter getPlayer(String name) {
        return players.get(name);
    }

    @Override
    public SoccerPlayerInter[] getAllPlayers() {
        return players.values().toArray(new SoccerPlayer[0]);
    }

    @Override
    public SoccerPlayerInter[] getPlayers(League league) {
        SoccerPlayerInter[] playersArray = getAllPlayers();
        ArrayList<SoccerPlayerInter> playersArrayLeague = new ArrayList<SoccerPlayerInter>();

        for (SoccerPlayerInter player: playersArray) {
            if(player.getLeague() == league){
                playersArrayLeague.add(player);
            }
        }
        sortPlayersPoints(playersArrayLeague);
        return playersArrayLeague.toArray(new SoccerPlayerInter[0]);
    }

    @Override
    public SoccerPlayerInter[] getPlayers(Country country) {
        SoccerPlayerInter[] playersArray = getAllPlayers();
        ArrayList<SoccerPlayerInter> playersArrayCountry = new ArrayList<SoccerPlayerInter>();

        for (SoccerPlayerInter player: playersArray) {
            if(player.getCountry() == country){
                playersArrayCountry.add(player);
            }
        }
        sortPlayersPoints(playersArrayCountry);
        return playersArrayCountry.toArray(new SoccerPlayerInter[0]);
    }

    @Override
    public void addPoints(String name, int points) {
        players.get(name).addPoints(points);
    }

    private void sortPlayersPoints(ArrayList<SoccerPlayerInter> playersArray){
        Comparator<SoccerPlayerInter> comparatorByPoints = new Comparator<SoccerPlayerInter>() {
            @Override
            public int compare(SoccerPlayerInter o1, SoccerPlayerInter o2) {
                return o2.getPoints() - o1.getPoints();
            }
        };
        playersArray.sort(comparatorByPoints);
    }
}

public class Homework6 {
    public void run(){
        LeagueManager manager = new LeagueManager();
        for(int i =0; i < 20; i++){
            manager.addPlayer(createRandomPlayer());                       // создаём случайных игроков и добавляем в менеджер
        }
        SoccerPlayerInter[] plArr = manager.getAllPlayers();               // получаем массив всех игроков
        SoccerPlayerInter player = plArr[4];                               // для проверки выбираем одного игрока из массива

        System.out.println(Arrays.toString(plArr));
        System.out.println("");

        for(Country country : Country.values()){
            System.out.println(Arrays.toString(manager.getPlayers(country)));        // выводим отсортированные рейтинги по странам
        }
        System.out.println("");

        for(League league : League.values()){
            System.out.println(Arrays.toString(manager.getPlayers(league)));         // выводим отсортированные рейтинги по лигам
        }

        System.out.println(player);
        manager.addPoints(player.getNickName(), 100);                        // добавляем выбранному игроку 100 очков
        SoccerPlayerInter playerInter = manager.getPlayer(player.getNickName());   // получаем объект игрока по имени
        System.out.println(playerInter);
        manager.removePlayer(player);                                              // удаляем игрока

        System.out.println(Arrays.toString(manager.getAllPlayers()));
    }

    private SoccerPlayer createRandomPlayer(){
        String nickName = "Player_" + generateRandomInt(0, 1000);
        Country country = Country.values()[generateRandomInt(0, Country.values().length)];
        League league = League.values()[generateRandomInt(0, League.values().length)];
        Integer points = generateRandomInt(0, 200);
        SoccerPlayer player = new SoccerPlayer(nickName, country, league, points);
        return player;
    }

    private int generateRandomInt(int min, int max){
        return (int) (Math.random()*(max - min) + min);
    }
}
