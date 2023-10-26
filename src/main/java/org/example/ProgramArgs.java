package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgramArgs {
    static void checkParams(Map<String, List<String>> params) throws AnExeption {
        if (!params.containsKey("p")) {
            throw new AnExeption("Param 'p' is missing");
        }
        if (params.get("p").isEmpty()) {
            throw new AnExeption("Param 'p' has not value");
        }
        if (!params.containsKey("s")) {
            throw new AnExeption("Param 's' is missing");
        }
        if (params.get("s").isEmpty()) {
            throw new AnExeption("Param 's' has not value");
        }
        if (!((params.get("s").size() == 1) || ((params.get("s").size() & 1) == 0))) {
            throw new AnExeption("Param 's' can have one option or even count it");
        }
    }
    static Map<String, List<String>> parsingArgs(String[] args) throws AnExeption {
        final Map<String, List<String>> params = new HashMap<>();
        List<String> options = null;
        for (final String a : args) {
            if (a.charAt(0) == '-') {
                if (a.length() < 2) {
                    params.clear();
                    throw new AnExeption("Error at argument " + a);
                }
                if (params.containsKey(a.substring(1))) {
                    options = params.get(a.substring(1));
                } else {
                    options = new ArrayList<>();
                    params.put(a.substring(1), options);
                }
            } else if (options != null) {
                options.add(a);
            } else {
                params.clear();
                throw new AnExeption("Illegal parameter usage");
            }
        }
        checkParams(params);
        return params;
    }
}
