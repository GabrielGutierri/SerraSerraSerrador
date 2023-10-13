package br.com.serra_serra_serrador.serra_serra_serrador.services;

import br.com.serra_serra_serrador.serra_serra_serrador.models.GraphRequestModel;
import br.com.serra_serra_serrador.serra_serra_serrador.models.GraphResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GraphServiceImpl{

    /*Realiza as operações, para cada gráfico existe um método.
    Cada método retorna o valor para o eixoY. O eixoX é o tempo, que é comum a todos(dados1).
    Provalvelmente teremos que mudar, para não ser só um FOR para tudo.
    Quando for fazer as series de fourier tem que ter 50 harmonicas.
    Tem que adaptar o gráfico. Exemplo: 1hz e 20 khz.
    */
    public static Map<String, Object> realizaOperacoes(double frequenciaSinal, double frequenciaCanal) {
        Map<String, Object> response = new HashMap<>();
        List<Double> dados1 = new ArrayList<>();
        List<Double> dados2 = new ArrayList<>();
        List<Double> dados3 = new ArrayList<>();
        List<Double> dados4 = new ArrayList<>();
        List<Double> dados5 = new ArrayList<>();
        List<Double> dados6 = new ArrayList<>();
        List<Double> dados7 = new ArrayList<>();
        List<Double> dados8 = new ArrayList<>();
        List<Double> dados9 = new ArrayList<>();

        for(double i = 0; i <= frequenciaSinal; i++){
            dados1.add(i);
            dados2.add(calculaSinalEntrada(i));
            dados3.add(calculaEspectroSinalEntrada(i));
            dados4.add(calculaFaseSinalEntrada(i));
            dados5.add(calculaGanhoAmplitudeCanal(i,frequenciaCanal));
            dados6.add(calculaContribuicaoFaseCanal(i,frequenciaCanal));
            dados7.add(calculaSinalSaida(i, frequenciaCanal));
            dados8.add(calculaEspectroSinalSaida(i, frequenciaCanal));
            dados9.add(calculaFaseSinalSaida(i, frequenciaCanal));
        }

        response.put("dados1", dados1);
        response.put("dados2", dados2);
        response.put("dados3", dados3);
        response.put("dados4", dados4);
        response.put("dados5", dados5);
        response.put("dados6", dados6);
        response.put("dados7", dados7);
        response.put("dados8", dados8);
        response.put("dados9", dados9);

        return response;
    }

    public static double calculaGanhoAmplitudeCanal(double frequenciaSinal, double frequenciaCanal) {
        return 1/ Math.sqrt(1 + Math.pow((frequenciaSinal/frequenciaCanal), 2));
    }

    public static double calculaContribuicaoFaseCanal(double frequenciaSinal, double frequenciaCanal) {
        return Math.toDegrees(-Math.atan(Math.toRadians(frequenciaSinal/frequenciaCanal)));
    }

    public static double calculaSinalEntrada(double i){
        return 3;
    }

    public static double calculaEspectroSinalEntrada(double i) {
        return 4;
    }

    public static double calculaFaseSinalEntrada(double i) {
        return 5;
    }

    public static double calculaSinalSaida(double i, double frequenciaCanal) {
        return 6;
    }

    public static double calculaEspectroSinalSaida(double i, double frequenciaCanal) {
        return 7;
    }

    public static double calculaFaseSinalSaida(double i, double frequenciaCanal) {
        return 8;
    }
}
