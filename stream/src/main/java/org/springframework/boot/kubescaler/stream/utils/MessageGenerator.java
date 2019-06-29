package org.springframework.boot.kubescaler.stream.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MessageGenerator {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private static final Random RANDOM = new Random(System.currentTimeMillis());
    private static final String HOSTNAME = System.getenv("HOSTNAME");

    private static final List<String> MESSAGE = Arrays.asList(
        generateLongMessage('a'),
        generateLongMessage('b'),
        generateLongMessage('c'),
        generateLongMessage('d'),
        generateLongMessage('e'),
        generateLongMessage('f'));

    public static String host() {
        return HOSTNAME;
    }

    public static String randomMessage() {
        return MESSAGE.get(RANDOM.nextInt(MESSAGE.size()));
    }

    public static String getCurrentTimeStamp() {
        return dtf.format(LocalDateTime.now());
    }

    private static String generateLongMessage(char symbol) {
        char[] message = new char[1024 * 1024];
        Arrays.fill(message, symbol);
        return "Huge message of 1048576+ chars ... " +  String.valueOf(message);
    }
}
