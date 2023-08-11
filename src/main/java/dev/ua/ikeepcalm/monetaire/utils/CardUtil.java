package dev.ua.ikeepcalm.monetaire.utils;

import java.util.Random;

public class CardUtil {
    public static String generateCardNumber() {
        Random rand = new Random();
        StringBuilder cardNumber = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            cardNumber.append(rand.nextInt(10));
        }
        cardNumber.append('-');
        for (int i = 0; i < 4; i++) {
            cardNumber.append(rand.nextInt(10));
        }
        cardNumber.append('-');
        for (int i = 0; i < 4; i++) {
            cardNumber.append(rand.nextInt(10));
        }
        cardNumber.append('-');
        for (int i = 0; i < 4; i++) {
            cardNumber.append(rand.nextInt(10));
        }

        return cardNumber.toString();
    }

    public static int generateCVV() {
        Random rand = new Random();
        return 1000 + rand.nextInt(9000); // Generates a number between 1000 and 9999 (inclusive)
    }
}

