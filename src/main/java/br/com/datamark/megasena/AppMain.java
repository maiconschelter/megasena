package br.com.datamark.megasena;

public class AppMain {

    public static void main(String[] args) {
        try {
            new CEFCapture().initCapture();
        }
        catch (Exception except) {
            except.printStackTrace();
        }
    }
}