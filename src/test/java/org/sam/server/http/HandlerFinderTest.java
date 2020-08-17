package org.sam.server.http;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class HandlerFinderTest {

    @Test
    void get_string() {
        Pattern pattern = Pattern.compile("[{](.*?)[}]");
        Matcher matcher = pattern.matcher("/board");

        while (matcher.find()) {
            System.out.println(matcher.group(1));
            System.out.println(313);
        }
    }

}