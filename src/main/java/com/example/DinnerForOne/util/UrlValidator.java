package com.example.DinnerForOne.util;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UrlValidator {

    public static void validateUrl(String url) {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("URL cannot be null or empty.");
        }

        String urlRegex = "^(https?|ftp)://[\\w\\d\\-.]+(:\\d+)?(/\\S*)?$";
        Pattern pattern = Pattern.compile(urlRegex);
        Matcher matcher = pattern.matcher(url);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid URL format.");
        }
    }
}
