package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class Main {

    public static volatile AtomicLong countTreeLength = new AtomicLong(0);
    public static volatile AtomicLong countFourLength = new AtomicLong(0);
    public static volatile AtomicLong countFiveLength = new AtomicLong(0);


    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }




        Thread threadFindBeautifulNic = new Thread(() -> {
            for (String name : texts) {
                boolean answer = beautifulNic(name);
                getIncrement(answer,name);
            }
        });
        threadFindBeautifulNic.start();


        Thread threadIdenticalLetters = new Thread(() -> {
            for (String name : texts) {
                boolean answer = identicalLetters(name);
                getIncrement(answer,name);
            }
        });
        threadIdenticalLetters.start();

        Thread threadFindIsPalindrome = new Thread(() -> {
            for (String name : texts) {
                boolean answer = isPalindrome(name);
                getIncrement(answer,name);
            }
        });
        threadFindIsPalindrome.start();

        threadFindBeautifulNic.join();
        threadIdenticalLetters.join();
        threadFindIsPalindrome.join();



        System.out.println("Красивых слов с длиной 3 " + countTreeLength + " шт");
        System.out.println("Красивых слов с длиной 4 " + countFourLength + " шт");
        System.out.println("Красивых слов с длиной 5 " + countFiveLength + " шт");


    }


    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static boolean beautifulNic(String name) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder stringBuilder = new StringBuilder(alphabet);
        StringBuilder word = new StringBuilder(name);

        int start;
        int countStart = 0;
        boolean answer = true;
        boolean coorect = true;

        start = stringBuilder.indexOf(String.valueOf(word.toString().charAt(0)));

        for (int i = start; i < stringBuilder.length(); i++) {
            for (int j = countStart; j < word.length(); j++) {
                if (word.toString().contains(String.valueOf(stringBuilder.charAt(i)))) {
                    if (stringBuilder.charAt(i) != word.charAt(j)) {
                        String text = word.substring(j);
                        countStart = j;
                        if (text.contains(String.valueOf(stringBuilder.charAt(i)))) {
                            coorect = false;
                            break;
                        } else {
                            break;
                        }
                    }
                } else {
                    break;
                }
            }
            if (coorect == false) {
                answer = false;
                break;
            }
        }
        return answer;
    }

    public static boolean identicalLetters(String name) {
        String letter = String.valueOf(name.charAt(0));
        boolean answer = true;

        for (int i = 0; i < name.length(); i++) {
            if (String.valueOf(name.charAt(i)).equals(letter)) {
                answer = false;
                break;
            }
        }
        return answer;
    }

    public static boolean isPalindrome(String name) {
        StringBuilder builder = new StringBuilder(name);
        boolean answer;
        String left = builder.substring(0, builder.length() / 2 + 1);
        String right = builder.reverse().substring(0, builder.length() / 2 + 1);
        return answer = (left.equals(right)) ? true : false;
    }

    public static void getIncrement(boolean answer, String name) {
        if (answer) {
            if (name.length() == 3) {
                countTreeLength.getAndIncrement();
            }
            if (name.length() == 4) {
                countFourLength.getAndIncrement();
            }
            if (name.length() == 5) {
                countFiveLength.getAndIncrement();
            }
        }
    }
}