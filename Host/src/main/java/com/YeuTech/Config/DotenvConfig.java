package com.YeuTech.Config;

import io.github.cdimascio.dotenv.Dotenv;

public class DotenvConfig {
    public static void load() {
        Dotenv dotenv = Dotenv.configure()
                .directory("./")
                .ignoreIfMissing()
                .load();

        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
    }
}
