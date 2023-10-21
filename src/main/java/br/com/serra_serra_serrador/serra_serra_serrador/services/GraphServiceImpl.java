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
    private static final int puloEixoGrafico = 500;
    private static final int numeroHarmonicas = 50;
    /*Realiza as operações, para cada gráfico existe um método.
    Cada método retorna o valor para o eixoY. O eixoX é o tempo, que é comum a todos(dados1).
    Provalvelmente teremos que mudar, para não ser só um FOR para tudo.
    Quando for fazer as series de fourier tem que ter 50 harmonicas.
    Tem que adaptar o gráfico. Exemplo: 1hz e 20 khz.
    */
    public static Map<String, Object> realizaOperacoes(double frequenciaSinal, double frequenciaCanal) {
        GraphResponseModel responseModel = new GraphResponseModel();

        double pulosEixoXGraficosTempo, pulosEixoXGraficosFrequenciaCanal;
        if(frequenciaSinal < puloEixoGrafico){
            pulosEixoXGraficosTempo = frequenciaSinal / puloEixoGrafico; // calcula a distancia entre os pontos do gráfico;
            pulosEixoXGraficosFrequenciaCanal = frequenciaSinal / puloEixoGrafico;
        }
        else{
            pulosEixoXGraficosTempo = 1;
            pulosEixoXGraficosFrequenciaCanal = 1;
        }

        realizaLacoTempo(frequenciaSinal, frequenciaCanal, pulosEixoXGraficosTempo, responseModel);
        realizaLacoFrequencia(frequenciaSinal, frequenciaCanal, pulosEixoXGraficosFrequenciaCanal, responseModel);
        realizaLacoHarmonicas(frequenciaSinal, frequenciaCanal, responseModel);

        return montaResponse(responseModel);
    }

    private static Map<String, Object> montaResponse(GraphResponseModel responseModel){
        Map<String, Object> response = new HashMap<>();
        response.put("dados1", responseModel.xHarmonica);
        response.put("dados2", responseModel.xTempo);
        response.put("dados3", responseModel.xFrequenciaCanal);
        response.put("dados4", responseModel.ySinalEntrada);
        response.put("dados5", responseModel.yEspectroSinalEntrada);
        response.put("dados6", responseModel.yFaseSinalEntrada);
        response.put("dados7", responseModel.yCalculoAmplitudeCanal);
        response.put("dados8", responseModel.yDeslocamentoFaseCanal);
        response.put("dados9", responseModel.ySinalSaida);
        response.put("dados10", responseModel.yEspectroSinalSaida);
        response.put("dados11", responseModel.yFaseSinalSaida);
        return response;
    }
    //Laço em relação ao tempo -> Para o sinal de entrada e sinal de saída;
    // i representa o tempo
    private static void realizaLacoTempo(double frequenciaSinal, double frequenciaCanal, double pulosEixoXGraficosTempo, GraphResponseModel responseModel){
        for(double i = 0; i <= frequenciaSinal; i += pulosEixoXGraficosTempo){
            responseModel.xTempo.add(i);
            responseModel.ySinalEntrada.add(calculaSinalEntrada(i, frequenciaSinal));
            responseModel.ySinalSaida.add(calculaSinalSaida(i, frequenciaCanal));
        }
    }
    //Laço em relação a frequencia -> Para os gráficos de canal;
    // i representa frequencia. É o dobro da frequencia do sinal;
    private static void realizaLacoFrequencia(double frequenciaSinal, double frequenciaCanal, double pulosEixoXGraficosFrequenciaCanal, GraphResponseModel responseModel){
        for(double i=0; i <= (frequenciaSinal * 2); i += (pulosEixoXGraficosFrequenciaCanal * 2)){
            responseModel.xFrequenciaCanal.add(i);
            responseModel.yCalculoAmplitudeCanal.add(calculaGanhoAmplitudeCanal(i,frequenciaCanal));
            responseModel.yDeslocamentoFaseCanal.add(calculaContribuicaoFaseCanal(i,frequenciaCanal));

        }
    }
    private static void realizaLacoHarmonicas(Double frequenciaSinal, Double frequenciaCanal, GraphResponseModel responseModel){
        for(double i = 0; i <= 50; i += 1){
            responseModel.xHarmonica.add(i);
            responseModel.yEspectroSinalEntrada.add(calculaEspectroSinalEntrada(i, frequenciaSinal));
            responseModel.yFaseSinalEntrada.add(calculaFaseSinalEntrada(i));
            responseModel.yEspectroSinalSaida.add(calculaEspectroSinalSaida(i, frequenciaCanal));
            responseModel.yFaseSinalSaida.add(calculaFaseSinalSaida(i, frequenciaCanal));
        }
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
        if (i == 1) {
            return Math.PI; // Primeira harmônica
        } else if (i % 2 == 0) {
            return 0; // Harmônicas pares
        } else {
            return -2 * Math.PI / (i * Math.PI); // Harmônicas ímpares
        }
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
