package com.company;

import java.util.concurrent.locks.ReentrantLock;

class CardSynch{
    private Card card;
    String state;
    CardSynch(Card card){
        this.card = card;
        state = "DECREMENT";
    }

    public synchronized void changeMoney(long add){
        card.money += add;
    }

    public synchronized void incrMoney(long inc){
        while(state == "INCREMENT"){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        card.money += inc;
        state = "INCREMENT";
        notify();
    }

    public synchronized void decrMoney(long dec){
        if(state == "DECREMENT"){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        card.money -= dec;
        state = "DECREMENT";
        notify();
    }

    public long getMoney(){
        return card.money;
    }
}

public class Homework8 {
    public void run_HW1() {
        Card card = new Card(1);
        Increaser incr = new Increaser(card);
        Decreaser decr = new Decreaser(card);
        incr.setPriority(Thread.MAX_PRIORITY);
        decr.setPriority(Thread.MIN_PRIORITY);

        long start = System.currentTimeMillis();
        incr.start();
        decr.start();

        try {
            incr.join();
            decr.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("money = " + card.money);
        System.out.println((System.currentTimeMillis() - start) + " мс");
    }

    public void run_HW2() {
        Card cardNew = new Card(1);
        CardSynch card = new CardSynch(cardNew);
        String state = "DECREMENT";
        long start = System.currentTimeMillis();
        SequentialIncreaser incr = new SequentialIncreaser(card, state);
        SequentialDecreaser decr = new SequentialDecreaser(card, state);

        try {
            incr.thr.join();
            decr.thr.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("money = " + card.getMoney());
        System.out.println((System.currentTimeMillis() - start) + " мс");
    }

    public void run_HW3() {

    }
}
