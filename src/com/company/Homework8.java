package com.company;

class Increaser extends Thread{
    Card card;
    Increaser(Card card){
        this.card = card;
    }

    public void increment(long inc) {
        card.money += inc;
    }
}

class Decreaser extends Thread{
    Card card;
    Decreaser(Card card){
        this.card = card;
    }
    public void decrement(long dec) {
        card.money -= dec;
    }
}

public class Homework8 {
    public void run(){
        Card card = new Card(1);
        Increaser incr = new Increaser(card);
        Decreaser decr = new Decreaser(card);
        incr.start();
        decr.start();
//        Thread t1 = new Thread(incr);
//        t1.start();
//        Thread t2 = new Thread(decr);
//        t2.start();

        long start = System.nanoTime();
        for(int i = 0; i < 1000000; i++)
        {
            if(i % 2 == 0){
                incr.increment(3*i);
            }else{
                decr.decrement(2*i);
            }
        }
        System.out.println("money = " + card.money/1000000);
        System.out.println((System.nanoTime() - start)/1000 + " мкс");
    }
}
