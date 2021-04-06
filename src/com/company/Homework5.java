package com.company;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;

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

class Account implements Serializable{
    private String pin;
    private Client client;

    Account(Client client, String pin) {
        this.client = client;
        this.pin = pin;
    }

    public String getPin() {
        return pin;
    }

    public Client getClient() {
        return client;
    }
}

class Client implements Serializable{
    private final String name;
    public Vector<Card> cardVector;

    Client(String name) {
        this.name = name;
        cardVector = new Vector<Card>();
    }

    public String getName() {
        return name;
    }
}

class Card implements Serializable{
    private final long number;
    public long money;

    Card(long num) {
        number = num;
        money = 0;
    }

    public long getNumber() {
        return number;
    }
}

class TerminalSimple implements Terminal, Serializable {
    public HashMap<String, Account> clientList; // по имени клиента выбираем его аккаунт

    private transient Account currentAcc;
    private transient int countTryInputPIN = 0;
    private transient Scanner scan;

    TerminalSimple() {
        scan = new Scanner(System.in);
        clientList = new HashMap<String, Account>();
    }
    public void initTerminal(){
        if(scan == null){
            scan = new Scanner(System.in);
        }
    }

    public boolean enterToTerminal(String name) {
        if (!clientList.containsKey(name)) {
            System.out.println("Данный клиент отсутствует в базе");
            System.out.println("Создать аккаунт?");
            System.out.println("1 - Да\n0 - Нет");
            int ans = scan.nextInt();
            scan.nextLine();
            if (ans == 1) {
                try {
                    createClient(name);
                } catch (CreateDuplicate err) {
                    err.printStackTrace();
                }
            }
            return false;
        }
        currentAcc = clientList.get(name);
        try {
            if (checkPIN()) {
                return true;
            } else {
                currentAcc = null;
            }
        } catch (BlockAccount err) {
            try {
                System.out.println("Превышен лимит попыток. Ваш аккаунт будет заблокирован на 5 секунд");
                Thread.sleep(5000);
            } catch (InterruptedException er) {
                System.out.println(er.toString());
            }
        }
        return false;
    }

    @Override
    public void checkMoney() {
        Vector<Card> cardList = currentAcc.getClient().cardVector;
        int num = getIndexOfCardList(cardList);
        System.out.println(cardList.get(num).money);
    }

    @Override
    public void putMoney() {
        Vector<Card> cardVector = currentAcc.getClient().cardVector;
        int num = getIndexOfCardList(cardVector);
        Card card = cardVector.get(num);
        long putOnCardMoney = getLongFromConsole("Введите сумму: ");
        if (putOnCardMoney < 0) {
            System.out.println("Требуемая сумма должна быть не меньше 0");
            return;
        }
        if (putOnCardMoney % 100 != 0) {
            System.out.println("Требуемая сумма должна быть кратна 100");
            return;
        }
        card.money += putOnCardMoney;
    }

    @Override
    public boolean takeMoney() throws NotEnoughMoneyOnCard {
        Vector<Card> cardVector = currentAcc.getClient().cardVector;
        int num = getIndexOfCardList(cardVector);
        Card card = cardVector.get(num);
        long moneyOnCard = 100 * card.money / 100;
        System.out.println("Доступно для снятия: " + moneyOnCard);
        long needMoney = getLongFromConsole("Введите сумму: ");
        if (needMoney > moneyOnCard) {
            throw new NotEnoughMoneyOnCard();
        }
        if (needMoney < 0) {
            System.out.println("Требуемая сумма должна быть не меньше 0");
            return false;
        }
        if (needMoney % 100 != 0) {
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
            pincode = getLineFromConsole("Введите новый PIN-код для своей карты: ");
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
        clientList.remove(currentAcc.getClient().getName());
        currentAcc = null;
    }

    @Override
    public void createCard() throws CreateDuplicate {
        long numCard = getLongFromConsole("Введите желаемый номер карты: ");
        for (Map.Entry<String, Account> entry : clientList.entrySet()) {
            Vector<Card> cardList = entry.getValue().getClient().cardVector;
            for (Card card : cardList) {
                if (card.getNumber() == numCard) {
                    throw new CreateDuplicate("Карта");
                }
            }
        }
        currentAcc.getClient().cardVector.add(new Card(numCard));
        System.out.println("Создана карта с номером " + numCard);
    }

    @Override
    public void deleteCard() {
        Vector<Card> cardList = currentAcc.getClient().cardVector;
        System.out.println("Удалить карту");
        int num = getIndexOfCardList(cardList);
        cardList.remove(num);
    }

    private String getLineFromConsole(String message) {
        System.out.println(message);
        return scan.nextLine();
    }

    private void inputPIN() throws NotCorrectPIN {
        String pin = getLineFromConsole("Введите ПИН-код: ");
        if (!currentAcc.getPin().equals(pin)) {
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

    private int getIndexOfCardList(Vector<Card> cardList) {
        for (int i = 0; i < cardList.size(); i++) {
            System.out.println(i + " --> Карта №" + cardList.get(i).getNumber());
        }
        System.out.print("\nВыберите карту: ");
        int num = scan.nextInt();
        scan.nextLine();
        return num;
    }

    private long getLongFromConsole(String message) {
        System.out.print(message);
        long num = scan.nextLong();
        scan.nextLine();
        return num;
    }

}

class MenuTerminal {
    private TerminalSimple terminal;
    private Scanner scan;

    private final String nameFileChar = "dataChar.txt";
    private final String nameFileByte = "dataByte.txt";
    private final String nameFileObj = "dataObj.ser";

    MenuTerminal() {
        terminal = new TerminalSimple();
        scan = new Scanner(System.in);
    }

    enum MenuItem {
        EXIT,
        CHECK_MONEY,
        PUT_MONEY,
        TAKE_MONEY,
        CREATE_CARD,
        DELETE_CARD,
        DELETE_CLIENT
    }

    public void beginTerminal() {
//        loadFromFileChar();
//        loadFromFileByte();
        loadFromFileObj();
        int ans = 0;
        do {
            if (ans == 1) {
                start();
            }
            System.out.println("Желаете войти в терминал?\n1 - Да\n0 - Нет");
            ans = scan.nextInt();
            scan.nextLine();
        } while (ans == 1);
        saveToFileChar();
        saveToFileByte();
        saveTerminal();
    }

    private void start() {
        System.out.println("Введите имя пользователя");
        String name = scan.nextLine();
        if (!terminal.enterToTerminal(name)) {
            return;
        }

        MenuItem ans = MenuItem.CHECK_MONEY;
        while (ans != MenuItem.EXIT) {
            System.out.println("1 - Проверить счёт");
            System.out.println("2 - Внести наличные");
            System.out.println("3 - Снять наличные");
            System.out.println("4 - Создать карту");
            System.out.println("5 - Удалить карту");
            System.out.println("6 - Удалить клиента");
            System.out.println("0 - Выйти из терминала");

            ans = MenuItem.values()[scan.nextInt()];
            scan.nextLine();

            switch (ans) {
                case CHECK_MONEY:
                    terminal.checkMoney();
                    break;
                case PUT_MONEY:
                    terminal.putMoney();
                    break;
                case TAKE_MONEY:
                    try {
                        if (terminal.takeMoney()) {
                            System.out.println("Возьмите деньги");
                        }
                    } catch (NotEnoughMoneyOnCard err) {
                        System.err.println(err.toString());
                    }
                    break;
                case CREATE_CARD:
                    try {
                        terminal.createCard();
                    } catch (CreateDuplicate err) {
                        System.out.println("Дубликат карты");
                    }
                    break;
                case DELETE_CARD:
                    terminal.deleteCard();
                    break;
                case DELETE_CLIENT:
                    terminal.deleteClient();
                    break;
            }
        }
    }

    private void saveTerminal(){
        try(ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(nameFileObj))){
            objectOutputStream.writeObject(terminal);
        } catch (IOException err){
            System.err.println(err.toString());
        }
    }

    private void loadFromFileObj(){
        try(ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(nameFileObj))){
            terminal = (TerminalSimple) objectInputStream.readObject();
            terminal.initTerminal();
        } catch (IOException | ClassNotFoundException err){
            System.err.println(err.toString());
        }
    }

    private void saveToFileChar() {
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(nameFileChar)));
            HashMap<String, Account> clientList = terminal.clientList;
            for (Map.Entry<String, Account> entry : clientList.entrySet()) {
                Account acc = entry.getValue();
                out.println(acc.getClient().getName());
                out.println(acc.getPin());
                out.println(acc.getClient().cardVector.size());
                for (Card card : acc.getClient().cardVector) {
                    out.println(card.getNumber());
                    out.println(card.money);
                }
            }
            out.close();
        } catch (IOException err) {
            System.out.println("Error save to file");
        }
    }

    private void loadFromFileChar() {
        try (BufferedReader br = new BufferedReader(new FileReader(nameFileChar))) {
            HashMap<String, Account> clientList = terminal.clientList;
            String nameClient;
            while( (nameClient = br.readLine()) != null ){
                String pin = br.readLine();
                Client client = new Client(nameClient);
                clientList.put(nameClient, new Account(client, pin));
                int countCard = Integer.parseInt(br.readLine());
                for(int i = 0; i < countCard; i++){
                    long numCard = Long.parseLong(br.readLine());
                    long money = Long.parseLong(br.readLine());
                    Card card = new Card(numCard);
                    card.money = money;
                    client.cardVector.add(card);
                }
            }
        } catch (FileNotFoundException err) {
            System.err.println(err.toString());
        } catch (IOException err) {
            System.err.println(err.toString());
        }
    }

    private void saveToFileByte() {
        try(FileOutputStream out = new FileOutputStream(nameFileByte)) {

            HashMap<String, Account> clientList = terminal.clientList;
            for (Map.Entry<String, Account> entry : clientList.entrySet()) {
                Account acc = entry.getValue();
                writeDataToFile(out, acc.getClient().getName().getBytes());
                writeDataToFile(out, acc.getPin().getBytes());
                writeDataToFile(out, longToBytes(acc.getClient().cardVector.size()));
                for (Card card : acc.getClient().cardVector) {
                    writeDataToFile(out, longToBytes(card.getNumber()));
                    writeDataToFile(out, longToBytes(card.money));
                }
            }
        } catch (IOException err) {
            System.err.println(err.toString());
        }
    }

    private void loadFromFileByte() {
        try (FileInputStream in = new FileInputStream(nameFileByte)) {
            HashMap<String, Account> clientList = terminal.clientList;
            String nameClient;
            while ((nameClient = new String(readLine(in), StandardCharsets.UTF_8)).length() != 0 ) {
                String pin = new String(readLine(in), StandardCharsets.UTF_8);
                Client client = new Client(nameClient);
                clientList.put(nameClient, new Account(client, pin));
                int countCard = (int) bytesToLong(readLine(in));
                for (int i = 0; i < countCard; i++) {
                    long numCard = bytesToLong(readLine(in));
                    long money = bytesToLong(readLine(in));
                    Card card = new Card(numCard);
                    card.money = money;
                    client.cardVector.add(card);
                }
            }
        } catch (IOException err) {
            System.err.println(err.toString());
        }
    }

    private void writeDataToFile(FileOutputStream out, byte[] data){
        try{
            out.write(data);
            out.write('\n');
        } catch(IOException err){
            System.err.println(err.toString());
        }
    }

    private byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

    private long bytesToLong(byte[] data){
        byte[] ans = new byte[8];
        System.arraycopy(data, 0, ans, 0, Long.BYTES);
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(ans);
        buffer.flip();//need flip
        return buffer.getLong();
    }

    private byte[] readLine(FileInputStream in){
        ByteBuffer buffer = ByteBuffer.allocate(64);
        int count = 0;
        try{
            char eof = (char) -1;
            char ch;
            boolean charIsWrite;
            do{
                ch = (char)in.read();
                charIsWrite = (ch != '\n') && (ch != eof);
                if(charIsWrite){
                    buffer.put((byte)ch);
                    count++;
                }
            } while (charIsWrite);

        } catch (IOException err){
            System.err.println(err.toString());
        }
        byte[] ans = new byte[count];
        System.arraycopy(buffer.array(), 0, ans, 0, count);
        return ans;
    }
}

public class Homework5 {
    public static void run() {
        MenuTerminal menu = new MenuTerminal();
        menu.beginTerminal();
    }
}
