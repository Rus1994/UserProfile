package com.company;

class SequentialIncreaser implements Runnable {
    private CardSynch card;
    Thread thr;

    SequentialIncreaser(CardSynch card) {
        this.card = card;
        thr = new Thread(this, "Increaser");
        thr.start();
    }

    public void increment(long inc) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (card) {
            try {
                if (card.state == "INCREMENT") {
                    card.wait();
                }
            } catch (InterruptedException e) {
                System.out.println("Прерывание в инкременте");
            }
            card.incrMoney(inc);
            System.out.println(thr.getName() + " --> " + card.getMoney());
            card.notify();
        }
    }

    @Override
    public void run() {
        for (int i = 1; i <= 3; i++) {
            increment(5 * i);
        }
    }
}
