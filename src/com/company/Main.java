package com.company;

import java.util.ArrayList;
import java.util.Scanner;

class UserProfile
{
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
        return  "Имя = " + name + '\n' +
                "Дата рождения = " + date + '\n' +
                "email = " + email + '\n' +
                "Адрес = " + address + '\n' +
                "Номер телефона = " + numberPhone + '\n' +
                "Место работы = " + placeWork + '\n' +
                "Информация о себе = " + infoMyself + '\n' +
                "Другая информация = " + otherInfo + '\n' ;
    }
}

public class Main {
    static Scanner scan;
    static ArrayList<UserProfile> Users;

    static int Menu()
    {
        System.out.println("Выберите действие:");
        System.out.println("1 - найти пользователя");
        System.out.println("2 - создать пользователя");
        System.out.println("3 - удалить пользователя");
        System.out.println("4 - показать список всех пользователей");
        System.out.println("0 - выйти из программы");
        int result = scan.nextInt();
        scan.nextLine();
        return result;
    }
    static int nameAtArr(String name)
    {
        int ind;
        for(ind = 0; ind < Users.size(); ind++)
        {
            if(Users.get(ind).name.equals(name))
            {
                return ind;
            }
        }
        return -1;
    }
    static void findProfile()
    {
//        scan.nextLine(); // костыль
        System.out.print("Введите имя пользователя: ");
        String nameUser = scan.nextLine();
        int ind = nameAtArr(nameUser);
        if(ind == -1)
        {
            System.out.println("Пользователь не найден");
        }
        else
        {
            System.out.println(Users.get(ind).toString());
        }
    }
    static void deleteProfile()
    {
//        scan.nextLine(); // костыль
        System.out.print("Введите имя пользователя, которого хотите удалить: ");
        String nameUser = scan.nextLine();
        int ind = nameAtArr(nameUser);
        if(ind == -1)
        {
            System.out.println("Пользователь не найден");
        }
        else
        {
            Users.remove(ind);
        }
    }
    static void showListProfile()
    {
        for(UserProfile user : Users)
        {
            System.out.println(user.name);
        }
    }
    static void createProfile()
    {
        UserProfile user = new UserProfile();
        Users.add(user);
//        scan.nextLine(); // костыль
        System.out.print("Введите имя: ");
        user.name = scan.nextLine();
        System.out.print("Введите адрес эл.почты: ");
        user.email = scan.nextLine();
        System.out.print("Введите дату рождения: ");
        user.date = scan.nextLine();
        System.out.print("Введите адрес проживания: ");
        user.address = scan.nextLine();
        System.out.print("Введите номер телефона: ");
        user.numberPhone = scan.nextLine();
        System.out.print("Введите место работы: ");
        user.placeWork = scan.nextLine();
        System.out.print("Напишите о себе: ");
        user.infoMyself = scan.nextLine();
        System.out.print("Укажите прочую информацию: ");
        user.otherInfo = scan.nextLine();
    }

    public static void main(String[] args) {
        scan = new Scanner(System.in);
        Users = new ArrayList<UserProfile>();
        int option;
        do {
            option = Menu();
            switch(option)
            {
                case 0:
                    break;
                case 1:
                    findProfile();
                    break;
                case 2:
                    createProfile();
                    break;
                case 3:
                    deleteProfile();
                    break;
                case 4:
                    showListProfile();
                    break;
                default:
                    System.out.println("Выбранный вариант отсутствует");
                    break;
            }
        }while(option != 0);
    }
}
