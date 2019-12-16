package ru.kafkaspark.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final String IPADDRESS_PATTERN = "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";

    private static Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);

    public static boolean isParameterValid(String ipAddress) {
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.find();
    }

    public static String getIpFromString(String string) {
        Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return "";
        }
    }
}
