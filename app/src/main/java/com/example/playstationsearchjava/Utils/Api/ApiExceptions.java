package com.example.playstationsearchjava.Utils.Api;

import android.annotation.SuppressLint;

import java.util.HashMap;

public class ApiExceptions extends Exception {

    public static String parse(Integer code) {
        HashMap<String, String> ExceptionMap = getByCode(code);
        String Exception = parseExceptionMap(ExceptionMap);
        return Exception;
    }

    @SuppressLint("DefaultLocale")
    private static String parseExceptionMap(HashMap<String, String> Map)
    {
        return String.format(
                "ERROR DURING YoApi Request!\n\nCODE : %s\nDESCRIPTION: %s",
                Map.get("code"),
                Map.get("description")
        );
    }

    private static HashMap<String, String> getByCode(Integer Code)
    {
        HashMap<String, String> ExceptionData = new HashMap<>();


        if (Code == ResponseStatusTypes.EXISTING_LOGIN) {
            ExceptionData.put("code", Code.toString());
            ExceptionData.put("description", "Account with the same login is exists, try to use a new one...");
        } else if (Code == ResponseStatusTypes.WRONG_CREDENTIALS) {
            ExceptionData.put("code", Code.toString());
            ExceptionData.put("description", "Incorrect credentials");
        } else {
            ExceptionData.put("code", Code.toString());
            ExceptionData.put("description", "Unknown code response status.");
        }

        return ExceptionData;
    }
}
