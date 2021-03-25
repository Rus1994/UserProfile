package com.company;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

class NotEnoughMoneyOnCard extends Exception {
    public NotEnoughMoneyOnCard() {
        super("Недостаточно средств на карте");
    }
}

class NotCorrectPIN extends Exception {
    public NotCorrectPIN() {
        super("Неверный ПИН-код");
    }
}

class BlockAccount extends Exception {
    public BlockAccount() {
        super("Аккаунт заблокирован");
    }
}

class CreateDuplicate extends Exception {
    public CreateDuplicate(String str) {
        super("Создание дубликата: " + str);
    }
}

interface Terminal {
    void checkMoney();

    void putMoney();

    boolean takeMoney() throws NotEnoughMoneyOnCard;

    Client createClient(String name) throws CreateDuplicate;

    void deleteClient();

    void createCard() throws CreateDuplicate;

    void deleteCard();
}

class Account {
    public String pin;
    public Client client;
//    private HashMap<Long, Double> cardMoneyList;

    Account(Client client, String pin) {
        this.client = client;
        this.pin = pin;
    }

}

class Client {
    public String name;
    public Vector<Card> cardList;

    Client(String name) {
        this.name = name;
        cardList = new Vector<Card>();
    }
}

class Card {
    Card(long num) {
        number = num;
        money = 0;
    }

    public long number;
    public long money;
}

class TerminalSimple implements Terminal {
    private HashMap<String, Account> clientList; // по имени клиента выбираем его аккаунт

    private Account currentAcc;

    private int countTryInputPIN = 0;
    Scanner scan;

    public void TerminalSimple() {
        scan = new Scanner(System.in);
    }

    private void inputPIN() throws NotCorrectPIN {
        System.out.print("Введите ПИН-код: ");
        String pin = scan.nextLine();
        if (!currentAcc.pin.equals(pin)) {
            throw new NotCorrectPIN();
        }
    }

    private boolean checkPIN() throws BlockAccount {
        try {
            inputPIN();
        } catch (NotCorrectPIN err) {
            countTryInputPIN++;
            if (countTryInputPIN == 3) {
                countTryInputPIN = 0;
                throw new BlockAccount();
            }
            return false;
        }
        return true;
    }

    public void enterToTerminal(String name) {
        if (!clientList.containsKey(name)) {
            System.out.println("Данный клиент отсутствует в базе");
            return;
        }
        currentAcc = null;
        try {
            if (checkPIN()) {
                currentAcc = clientList.get(name);
            }
        } catch (BlockAccount err) {
            try {
                System.out.println("Превышен лимит попыток. Ваш аккаунт будет заблокирован на 5 секунд");
                wait(5000);
            } catch (InterruptedException er) {
                er.printStackTrace();
            }
        }
    }

    private int getIndexOfCardList(Vector<Card> cardList) {
        for (int i = 0; i < cardList.size(); i++) {
            System.out.println(i + " --> Карта №" + cardList.get(i).number);
        }
        System.out.print("\nВыберите карту: ");
        int num = scan.nextInt();
        scan.nextLine();
        return num;
    }

    @Override
    public void checkMoney() {
        Vector<Card> cardList = currentAcc.client.cardList;
        int num = getIndexOfCardList(cardList);
        System.out.println(cardList.get(num).money);
    }

    @Override
    public void putMoney() {
        Vector<Card> cardList = currentAcc.client.cardList;
        int num = getIndexOfCardList(cardList);
        Card card = cardList.get(num);
        System.out.print("Введите сумму: ");
        long putOnCardMoney = scan.nextLong();
        scan.nextLine();
        if (putOnCardMoney < 0) {
            System.out.println("Требуемая сумма должна быть не меньше 0");
            return;
        } else if (putOnCardMoney % 100 != 0) {
            System.out.println("Требуемая сумма должна быть кратна 100");
            return;
        }
        card.money += putOnCardMoney;
    }

    @Override
    public boolean takeMoney() throws NotEnoughMoneyOnCard {
        Vector<Card> cardList = currentAcc.client.cardList;
        int num = getIndexOfCardList(cardList);
        Card card = cardList.get(num);
        long moneyOnCard = 100 * card.money / 100;
        System.out.println("Доступно для снятия: " + moneyOnCard);
        System.out.print("Введите сумму: ");
        long needMoney = scan.nextLong();
        scan.nextLine();
        if (needMoney > moneyOnCard) {
            throw new NotEnoughMoneyOnCard();
        } else if (needMoney < 0) {
            System.out.println("Требуемая сумма должна быть не меньше 0");
            return false;
        } else if (needMoney % 100 != 0) {
            System.out.println("Требуемая сумма должна быть кратна 100");
            return false;
        }
        card.money -= needMoney;
        return true;
    }

    @Override
    public Client createClient(String name) throws CreateDuplicate {
        if (clientList.containsKey(name)) {
            throw new CreateDuplicate("Клиент");
        }

        Client newClient = new Client(name);
        String pincode;
        boolean pinIsIncorrect;
        do {
            System.out.print("Введите новый PIN-код для своей карты: ");
            pincode = scan.nextLine();
            pinIsIncorrect = !((pincode.length() == 4) && pincode.chars().allMatch(x -> Character.isDigit(x)));
            if (pinIsIncorrect) {
                System.out.println("PIN-код должен состоять из 4-х цифр");
            }
        } while (pinIsIncorrect);

        Account newAcc = new Account(newClient, pincode);
        clientList.put(name, newAcc);
        return newClient;
    }

    @Override
    public void deleteClient() {
        clientList.remove(currentAcc.client.name);
        currentAcc = null;
    }

    @Override
    public void createCard() throws CreateDuplicate {
        System.out.print("Введите желаемый номер карты: ");
        long numCard = scan.nextLong();
        scan.nextLine();
        for (Map.Entry<String, Account> entry : clientList.entrySet()) {
            Vector<Card> cardList = entry.getValue().client.cardList;
            for (Card card : cardList) {
                if (card.number == numCard) {
                    throw new CreateDuplicate("Карта");
                }
            }
        }
        currentAcc.client.cardList.add(new Card(numCard));
        System.out.println("Создана карта с номером " + numCard);
    }

    @Override
    public void deleteCard() {
        Vector<Card> cardList = currentAcc.client.cardList;
        System.out.println("Удалить карту");
        int num = getIndexOfCardList(cardList);
        cardList.remove(num);
    }

}

public class Homework5 {
    TerminalSimple term = new TerminalSimple();

}
