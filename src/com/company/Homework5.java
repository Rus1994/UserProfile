package com.company;

class NotEnoughMoneyOnCard extends Exception {
    public void showDescription() {
        System.out.println("Недостаточно средств на карте");
    }
}

class NotCorrectPIN extends Exception {
    public void showDescription() {
        System.out.println("Неверный ПИН-код");
    }
}

class BlockAccount extends Exception {
    public void showDescription() {
        System.out.println("Аккаунт заблокирован");
    }
}

class CreateDuplicate extends Exception {
    public void showDescription() {
        System.out.println("Создание дубликата");
    }
}

interface Terminal {
    void checkMoney();

    void putMoney(int money);

    boolean takeMoney(int money);

    void createClient();

    void deleteClient();

    void createCard();

    void deleteCard();
}

class Client {

}

class Card {

}


public class Homework5 {

}
