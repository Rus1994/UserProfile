package com.company;

class SequentialDecreaser implements Runnable {
    Thread thr;
    private CardSynch card;
    String state;
    int i = 1;

    SequentialDecreaser(CardSynch card, String state) {
        this.card = card;
        this.state = state;
        thr = new Thread(this, "Decreaser");
        thr.start();
    }

    public void decrement(long dec) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        synchronized (card) {
//            try {
//                if (state == "DECREMENT") {
//                    card.wait();
//                }
//            } catch (InterruptedException e) {
//                System.out.println("Прерывание в декременте");
//            }
            if (card.getMoney() >= dec) {
//                card.changeMoney(-dec);
                card.decrMoney(dec);
            }
            System.out.println(thr.getName() + " --> " + card.getMoney());
//            state = "DECREMENT";
//            card.notify();
//        }
    }


    @Override
    public void run() {
        for (int i = 1; i <= 3; i++) {
            decrement(2 * i);
//        i++;
        }
    }
}
