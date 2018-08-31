package br.com.datamark.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {

    private static Connection instance;

    public static Connection getConnection() {
        try {
            if (instance == null || instance.isClosed()) {
                instance = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/megasena", "postgres", "KtZDY6uSq6kDCgh8");
                instance.prepareStatement("SET APPLICATION_NAME='CEFCapture'").execute();
            }
        }
        catch (Exception except) {
            except.printStackTrace();
        }
        return instance;
    }

    public static void closeConnection() {
        try {
            if (instance != null && !instance.isClosed()) {
                instance.close();
            }
        }
        catch (Exception except) {
            except.printStackTrace();
        }
    }
}