package com.company;

class SequentialDecreaser implements Runnable {
    private CardSynch card;
    Thread thr;

    SequentialDecreaser(CardSynch card) {
        this.card = card;
        thr = new Thread(this, "Decreaser");
        thr.start();
    }

    public void decrement(long dec) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (card) {
            try {
                if (card.state == "DECREMENT") {
                    card.wait();
                }
            } catch (InterruptedException e) {
                System.out.println("Прерывание в декременте");
            }
            if (card.getMoney() >= dec) {
                card.decrMoney(dec);
            }
            System.out.println(thr.getName() + " --> " + card.getMoney());
            card.notify();
        }
    }


    @Override
    public void run() {
        for (int i = 1; i <= 3; i++) {
            decrement(2 * i);
        }
    }
}
