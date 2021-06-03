package com.company;

class Decreaser extends Thread {
    private Card card;

    Decreaser(Card card) {
        super("Decreaser");
        this.card = card;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 3; i++) {
            decrement(2 * i);
        }
    }

    public void decrement(long dec) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException err) {
            err.printStackTrace();
        }
        synchronized (card) {
            if (card.money >= dec) {
                card.money -= dec;
            }
            System.out.println(this.getName() + " --> " + card.money);
        }
    }
}
