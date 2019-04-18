package com.example.playstationsearchjava.Utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class ToolsMethods {

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String sha256(String hash) {
        return Hashing.sha256()
                .hashString(hash, StandardCharsets.UTF_8)
                .toString();
    }

}
