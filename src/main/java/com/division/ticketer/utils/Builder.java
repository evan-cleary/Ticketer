/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.division.ticketer.utils;

import java.util.List;
import java.util.Set;

/**
 *
 * @author Evan
 */
public class Builder {

    public Builder() {
    }

    public static String buildString(String[] strings) {
        StringBuilder sb = new StringBuilder();
        for (String s : strings) {
            sb.append(s);
            sb.append("");
        }
        return sb.toString();
    }

    public static String buildString(List<String> strings) {
        String[] array = new String[strings.size()];
        return buildString(strings.toArray(array));
    }

    public static String buildString(Set<String> strings) {
        String[] array = new String[strings.size()];
        return buildString(strings.toArray(array));
    }
}
