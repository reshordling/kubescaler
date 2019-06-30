package org.springframework.boot.kubescaler.stream.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.springframework.boot.kubescaler.stream.model.Message;

import lombok.NonNull;

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

    public static Message generate() {
        return new Message(host(), randomMessage(), getCurrentTimeStamp());
    }

    public static Message generate(@NonNull String message) {
        return new Message(host(), message, getCurrentTimeStamp());
    }

    private static String host() {
        return HOSTNAME;
    }

    private static String randomMessage() {
        return MESSAGE.get(RANDOM.nextInt(MESSAGE.size()));
    }

    private static String getCurrentTimeStamp() {
        return dtf.format(LocalDateTime.now());
    }

    private static String generateLongMessage(char symbol) {
        char[] message = new char[1024 * 1024];
        Arrays.fill(message, symbol);
        return "Huge message of 1048576+ chars ... " +  String.valueOf(message);
    }
}
