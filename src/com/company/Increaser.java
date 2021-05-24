package com.company;

class Increaser extends Thread {
    private Card card;

    Increaser(Card card) {
        super("Increaser");
        this.card = card;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 3; i++) {
            if (i == 2) {
                interrupt();
            }
            if (!isInterrupted()) {
                increment(5 * i);
            } else {
                break;
            }
        }
    }

    public void increment(long inc) {
        try {
            Thread.sleep(2500);
        } catch (InterruptedException err) {
            err.printStackTrace();
        }
        synchronized (card) {
            card.money += inc;
            System.out.println(this.getName() + " --> " + card.money);
        }
    }

}
