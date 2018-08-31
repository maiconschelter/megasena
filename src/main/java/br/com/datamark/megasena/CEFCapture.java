package br.com.datamark.megasena;

import br.com.datamark.util.Database;
import br.com.datamark.util.HttpRequest;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Arrays;

public class CEFCapture {

    private static final String URL_SEARCH = "http://loterias.caixa.gov.br/wps/portal/loterias/landing/megasena/!ut/p/a1/04_Sj9CPykssy0xPLMnMz0vMAfGjzOLNDH0MPAzcDbwMPI0sDBxNXAOMwrzCjA0sjIEKIoEKnN0dPUzMfQwMDEwsjAw8XZw8XMwtfQ0MPM2I02-AAzgaENIfrh-FqsQ9wNnUwNHfxcnSwBgIDUyhCvA5EawAjxsKckMjDDI9FQE-F4ca/dl5/d5/L2dBISEvZ0FBIS9nQSEh/pw/Z7_HGK818G0KO6H80AU71KG7J0072/res/id=buscaResultado/c=cacheLevelPage/=/?timestampAjax=1535593682159&concurso=%s";

    private HttpRequest request;

    public CEFCapture() {
        this.request = new HttpRequest();
    }

    public void initCapture() throws Exception {
        int current = 0;
        String sql = "INSERT INTO public.results(rescodigo,resdate,resnumber) VALUES (?,?,?)";
        PreparedStatement statement = Database.getConnection().prepareStatement(sql);
        do {
            if (current == 0) {
                String url = String.format(URL_SEARCH, "");
                String result = this.request.getUrl(url);
                JSONObject data = new JSONObject(result);
                current = Integer.parseInt(data.getString("proximoConcurso"));
            }
            String url = String.format(URL_SEARCH, String.valueOf(current));
            String result = this.request.getUrl(url);
            JSONObject data = new JSONObject(result);
            String number = data.getString("resultado");
            String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date(data.getLong("data")));
            System.out.println(current + " - " + date + " - " + number);
            String[] values = number.split("-");
            Arrays.sort(values);
            statement.setInt(1, current);
            statement.setDate(2, new Date(data.getLong("data")));
            statement.setArray(3, Database.getConnection().createArrayOf("int", values));
            statement.execute();
            current--;
            Thread.sleep(1000);
        }
        while (current >= 2);
        statement.close();
    }
}