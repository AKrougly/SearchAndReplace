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
        String option = "";
        List<String> arguments = null;
        for (final String arg : args) {
            System.out.println(arg);

            if (arg.charAt(0) == '-') {
                if (arg.length() < 2) {
                    throw new AnExeption("Error at argument " + arg);
                }
                option = arg.substring(1);
                if (params.containsKey(option)) {
                    arguments = params.get(option);
                } else {
                    arguments = new ArrayList<>();
                    params.put(option, arguments);
                }
            } else if (arguments != null) {
                arguments.add(option.equals("p") ? arg.replace("/", "\\") : arg);
            } else {
                throw new AnExeption("Illegal parameter usage" + arg);
            }
        }
        for (Map.Entry<String, List<String>> m : params.entrySet()) {
            System.out.println(m.getKey() + " " + m.getValue());
        }
        checkParams(params);
        return params;
    }
}
