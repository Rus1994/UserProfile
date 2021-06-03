package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class UserProfile {
    String name;
    String email;
    String date;
    String address;
    String numberPhone;
    String placeWork;
    String infoMyself;
    String otherInfo;

    @Override
    public String toString() {
        return "Имя = " + name + '\n' +
                "Дата рождения = " + date + '\n' +
                "email = " + email + '\n' +
                "Адрес = " + address + '\n' +
                "Номер телефона = " + numberPhone + '\n' +
                "Место работы = " + placeWork + '\n' +
                "Информация о себе = " + infoMyself + '\n' +
                "Другая информация = " + otherInfo + '\n';
    }
}

public class Homework2 {

    private Scanner scan = new Scanner(System.in);
    private ArrayList<UserProfile> Users;

    private String getLineConsole(String message){
        System.out.print(message);
        return scan.nextLine();
    }

    enum MenuItem{
        EXIT,
        FIND_USER,
        CREATE_USER,
        DELETE_USER,
        SHOW_USERS
    }

    private MenuItem menu() {
        System.out.println("Выберите действие:");
        System.out.println("1 - найти пользователя");
        System.out.println("2 - создать пользователя");
        System.out.println("3 - удалить пользователя");
        System.out.println("4 - показать список всех пользователей");
        System.out.println("0 - выйти из программы");
        int result = scan.nextInt();
        scan.nextLine();
        return MenuItem.values()[result];
    }

    private int nameAtArr(String name) {
        for (int i = 0; i < Users.size(); i++) {
            if (Users.get(i).name.equals(name)) {
                return i;
            }
        }
        return -1;
    }

    private void findProfile() {
        String nameUser = getLineConsole("Введите имя пользователя: ");
        int ind = nameAtArr(nameUser);
        if (ind == -1) {
            System.out.println("Пользователь не найден");
        } else {
            System.out.println(Users.get(ind).toString());
        }
    }

    private void deleteProfile() {
        String nameUser = getLineConsole("Введите имя пользователя, которого хотите удалить: ");
        int ind = nameAtArr(nameUser);
        if (ind == -1) {
            System.out.println("Пользователь не найден");
        } else {
            Users.remove(ind);
        }
    }

    private void showListProfile() {
        for (UserProfile user : Users) {
            System.out.println(user.name);
        }
    }

    private void createProfile() {
        UserProfile user = new UserProfile();
        Users.add(user);
        user.name = getLineConsole("Введите имя: ");
        user.email = getLineConsole("Введите адрес эл.почты: ");
        user.date = getLineConsole("Введите дату рождения: ");
        user.address = getLineConsole("Введите адрес проживания: ");
        user.numberPhone = getLineConsole("Введите номер телефона: ");
        user.placeWork = getLineConsole("Введите место работы: ");
        user.infoMyself = getLineConsole("Напишите о себе: ");
        user.otherInfo = getLineConsole("Укажите прочую информацию: ");
    }

    public void homework2_2() {
        System.out.println("Домашнее задание 2 - Профиль");
        Users = new ArrayList<UserProfile>();
        MenuItem option;
        do {
            option = menu();
            switch (option) {
                case EXIT:
                    break;
                case FIND_USER:
                    findProfile();
                    break;
                case CREATE_USER:
                    createProfile();
                    break;
                case DELETE_USER:
                    deleteProfile();
                    break;
                case SHOW_USERS:
                    showListProfile();
                    break;
                default:
                    System.out.println("Выбранный вариант отсутствует");
                    break;
            }
        } while (option != MenuItem.EXIT);
        System.out.println("");
    }

    public void homeworkRegex() {
        System.out.println("Домашнее задание 2 - регулярные выражения, проверка входных данных от пользователя(паспорт)");

        String inputStr = getLineConsole("Введите серию и номер паспорта: ");//"36 14 123456";
        Pattern ptr = Pattern.compile("\\d{4}\\s\\d{6}"); // шаблон
        Matcher match = ptr.matcher(inputStr);

        Pattern ptr2 = Pattern.compile("\\d"); // шаблон
        Matcher match2 = ptr2.matcher(inputStr);

        StringBuffer strBuf = new StringBuffer();

        if (match.find()) {
            System.out.println(match.group(0));
        } else {
            int count = 0;
            while (match2.find()) {
                count++;
                if (count > 10) {
                    break;
                }
                strBuf.append(match2.group(0));
                if (count == 4) {
                    strBuf.append(" ");
                }
            }
            if (count == 10) {
                System.out.println(strBuf);
            } else {
                System.out.println("Количество цифр не соответствует формату");
            }
        }
        System.out.println("");
    }

    public void homeworkAdd() {
        System.out.println("Домашнее задание 2 - Вывод текста в квадрате");
        homeworkAdd(0);
        System.out.println("");
    }

    private void homeworkAdd(int addSize) {
        String text = "Java is a general-purpose computer programming language that is concurrent, class-based, object-oriented, and " +
                "specifically designed to have as few implementation dependencies as possible. It is intended to let application " +
                "developers write once, run anywhere, meaning that compiled Java code can run on all platforms that support Java " +
                "without the need for recompilation. Java applications are typically compiled to bytecode that can run on any Java " +
                "virtual machine regardless of computer architecture. As of 2016, Java is one of the most popular programming " +
                "languages in use, particularly for client-server web applications, with a reported 9 million developers. Java was " +
                "originally developed by James Gosling at Sun Microsystems and released in 1995 as a core component of Sun " +
                "Microsystems' Java platform.";

        int count = text.length();
        int size = (int) Math.sqrt(count) + addSize;
        Pattern ptr = Pattern.compile(" "); // шаблон
        Matcher match = ptr.matcher(text);
        List<Integer> indexList = new ArrayList<Integer>(); // индексы всех пробелов
        List<Integer> spaceList = new ArrayList<Integer>();  // индексы тех мест где будет переход на новую строку
        List<Integer> countSpaceList = new ArrayList<Integer>();
        List<Integer> addSpaceList = new ArrayList<Integer>(); // сколько пробелов надо добавить в строку

        int limit = size;
        int countSpace = 0;

        while (match.find()) // записываем индексы всех пробелов
        {
            indexList.add(match.start());
        }

        for (int i = 0; i < indexList.size(); i++) {
            if (indexList.get(i) > limit) {
                spaceList.add(indexList.get(i - 1)); // индексы переходов на новую строку
                addSpaceList.add(limit - indexList.get(i - 1));
                limit = indexList.get(i - 1) + size;
                countSpaceList.add(countSpace - 1); // записываем кол-во пробелов в строке, чтобы в них распределять оставшеесь пространство
                countSpace = 1;
            } else {
                countSpace++;
            }
        }
        spaceList.add(text.length());
        indexList.add(text.length());
        if (Math.abs(size - spaceList.size()) <= 1) {
            int ind = 0;
            int indSp = 0;
            int addSpace = addSpaceList.get(0) / countSpaceList.get(0);
            countSpace = addSpaceList.get(0) % countSpaceList.get(0);
            for (int i = 0; i < text.length(); i++) {
                if (i != indexList.get(ind)) // если символ не пробел, то просто его выводим
                {
                    System.out.print(text.charAt(i));
                } else if (i != spaceList.get(indSp)) // если это просто пробел
                {
                    if (countSpace == 0) {
                        String str = new String(new char[addSpace + 1]).replace("\0", " ");
                        System.out.print(str);
                    } else {
                        String str = new String(new char[addSpace + 2]).replace("\0", " ");
                        System.out.print(str);
                        countSpace--;
                    }
                    ind++;
                } else // если переход на другую строку
                {
                    System.out.println("");
                    ind++;
                    indSp++;
                    if (indSp < addSpaceList.size()) {
                        addSpace = countSpaceList.get(indSp) != 0 ? (addSpaceList.get(indSp) / countSpaceList.get(indSp)) : 0;
                        countSpace = countSpaceList.get(indSp) != 0 ? addSpaceList.get(indSp) % countSpaceList.get(indSp) : 0;
                        countSpace++;
                    }

                }
            }

        } else {
            homeworkAdd(addSize + 1);
        }

    }
}
