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
        List<Double> dados1 = new ArrayList<>(); // eixo X para gráficos de harmonica;
        List<Double> dados2 = new ArrayList<>(); //eixo X para gráficos de tempo;
        List<Double> dados3 = new ArrayList<>(); //eixo X para gráficos de frequencia do canal;
        List<Double> dados4 = new ArrayList<>(); // eixo y sinalEntrada;
        List<Double> dados5 = new ArrayList<>(); // eixo y espectro sinalEntrada;
        List<Double> dados6 = new ArrayList<>(); // eixo y fase sinalEntrada;
        List<Double> dados7 = new ArrayList<>(); // eixo y calculoAmplitudeCanal
        List<Double> dados8 = new ArrayList<>(); // eixo y deslocamentoFaseCanal;
        List<Double> dados9 = new ArrayList<>(); // eixo y sinalSaida;
        List<Double> dados10 = new ArrayList<>(); // eixo y espectro sinalSaida;
        List<Double> dados11 = new ArrayList<>();  // eixo y fase sinalSaida;

        double pulosEixoXGraficosTempo, pulosEixoXGraficosFrequenciaCanal;
        if(frequenciaSinal < 500){
            pulosEixoXGraficosTempo = frequenciaSinal / 500; // calcula a distancia entre os pontos do gráfico;
            pulosEixoXGraficosFrequenciaCanal = frequenciaSinal / 500;
        }
        else{
            pulosEixoXGraficosTempo = 1;
            pulosEixoXGraficosFrequenciaCanal = 1;
        }

        //Laço em relação ao tempo -> Para o sinal de entrada e sinal de saída;
        // i representa o tempo
        for(double i = 0; i <= frequenciaSinal; i += pulosEixoXGraficosTempo){
            dados2.add(i);
            dados4.add(calculaSinalEntrada(i, frequenciaSinal));
            dados9.add(calculaSinalSaida(i, frequenciaCanal));
        }

        //Laço em relação a frequencia -> Para os gráficos de canal;
        // i representa frequencia. É o dobro da frequencia do sinal;
        for(double i=0; i <= (frequenciaSinal * 2); i += (pulosEixoXGraficosFrequenciaCanal * 2)){
            dados3.add(i);
            dados7.add(calculaGanhoAmplitudeCanal(i,frequenciaCanal));
            dados8.add(calculaContribuicaoFaseCanal(i,frequenciaCanal));

        }

        //Laço em relação a harmonicas -> Para os gráficos de espectro e fase;
        //i representa a harmonica. 50 é o maximo;
        for(double i = 0; i <= 50; i += 1){
            dados1.add(i);
            dados5.add(calculaEspectroSinalEntrada(i, frequenciaSinal));
            dados6.add(calculaFaseSinalEntrada(i));
            dados10.add(calculaEspectroSinalSaida(i, frequenciaCanal));
            dados11.add(calculaFaseSinalSaida(i, frequenciaCanal));
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
        response.put("dados10", dados10);
        response.put("dados11", dados11);

        return response;
    }

    public static double calculaGanhoAmplitudeCanal(double frequenciaSinal, double frequenciaCanal) {
        return 1/ Math.sqrt(1 + Math.pow((frequenciaSinal/frequenciaCanal), 2));
    }

    public static double calculaContribuicaoFaseCanal(double frequenciaSinal, double frequenciaCanal) {
        return Math.toDegrees(-Math.atan(frequenciaSinal/frequenciaCanal));
    }

    public static double calculaSinalEntrada(double i, double frequenciaSinal) {


        return 3;
    }

    public static double calculaEspectroSinalEntrada(double i, double frequenciaSinal) {

        double T = 1.0 / frequenciaSinal; // Período
        double A = 1.0; // Amplitude do sinal de dente de serra

        // Cálculo da amplitude da componente de frequência n para um sinal de dente de serra
        if (i == 0) {
            return 0.0; // A amplitude da componente DC é zero
        } else if (i % 2 == 1) {
            return (2.0 * A) / (i * Math.PI);
        } else {
            return 0.0; // As componentes pares têm amplitude zero para um sinal de dente de serra
        }
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
