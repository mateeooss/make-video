package com.makevideo.make_video.utils;

public class StringUtils {
    public static String removeBlankLineBreak(String text){
        return (text != null ? text.replaceAll("(?m)^[ \t]*\r?\n", "") : "");
    }

    public static String removeDuplicateBlankSpace(String text){
        return (text != null ? text.replaceAll(" +", " ") : "");
    }

    public static String sanitizeText(String text){
        if(text == null) return "";

        text = removeBlankLineBreak(text);
        text = removeDuplicateBlankSpace(text);

        return text;
    }
}
