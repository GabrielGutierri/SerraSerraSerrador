package br.com.serra_serra_serrador.serra_serra_serrador.services;

import br.com.serra_serra_serrador.serra_serra_serrador.models.GraphRequestModel;
import br.com.serra_serra_serrador.serra_serra_serrador.models.GraphResponseModel;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.TransformType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.FastFourierTransformer;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.IntStream;

@Service
public class GraphServiceImpl{

    private static Map<String, Object> montaResponse(GraphResponseModel responseModel){
        Map<String, Object> response = new HashMap<>();

        response.put("dadosxHarmonica", responseModel.xHarmonica);
        response.put("dadosxTempo", responseModel.xTempo);
        response.put("dadosxFrequenciaCanal", responseModel.xFrequenciaCanal);
        response.put("dadosySinalEntrada", responseModel.ySinalEntrada);
        response.put("dadosyEspectroSinalEntrada", responseModel.yEspectroSinalEntrada);
        response.put("dadosyFaseSinalEntrada", responseModel.yFaseSinalEntrada);
        response.put("dadosyCalculoAmplitudeCanal", responseModel.yCalculoAmplitudeCanal);
        response.put("dadosyDeslocamentoFaseCanal", responseModel.yDeslocamentoFaseCanal);
        response.put("dadosySinalSaida", responseModel.ySinalSaida);
        response.put("dadosyEspectroSinalSaida", responseModel.yEspectroSinalSaida);
        response.put("dadosyFaseSinalSaida", responseModel.yFaseSinalSaida);
        return response;
    }

    public static Map<String, Object> realizaOperacoes(double frequenciaSinal, double frequenciaCanal) {
        GraphResponseModel responseModel = new GraphResponseModel();

        int numeroHarmonicas = 100; // Número de harmônicas a serem consideradas
        double periodo = 1 / frequenciaSinal;
        double periodoFinal = 4 * periodo; // Considero 4 períodos para o gráfico de tempo
        double numeroPontosGrafico = 10000;
        double espacos = periodoFinal / (numeroPontosGrafico - 1); // Calcula espaçamento entre pontos

        //Preciso inicializar as listas abaixo senão da IndexOutOfBounds
        for(int i = 0; i < numeroPontosGrafico; i++){
            responseModel.xTempo.add(i * espacos);
            responseModel.ySinalEntrada.add(0.0);
            responseModel.ySinalSaida.add(0.0);
        }

        for(int i =0; i < numeroHarmonicas; i++){
            responseModel.xHarmonica.add((double)i);
            calculaDadosCanal(i, responseModel, frequenciaSinal, frequenciaCanal);
            calculaDadosSinal(i, responseModel, frequenciaSinal, responseModel.yCalculoAmplitudeCanal.get(i) , responseModel.yDeslocamentoFaseCanal.get(i));
        }

        return montaResponse(responseModel);
    }

    public static void calculaDadosCanal(double n, GraphResponseModel responseModel, double frequenciaSinal, double frequenciaCanal) {
        responseModel.xFrequenciaCanal.add(n * frequenciaSinal);
        responseModel.yCalculoAmplitudeCanal.add(calculaGanhoAmplitudeCanal(n * frequenciaSinal, frequenciaCanal));
        responseModel.yDeslocamentoFaseCanal.add(Math.toDegrees(calculaContribuicaoFaseCanal(n * frequenciaSinal, frequenciaCanal)));
    }

    public static void calculaDadosSinal(double n, GraphResponseModel responseModel, double frequenciaSinal, double ganhoAmplitude, double ganhoDeslocamentoFase) {

        double faseInicial = calculaFaseSinalEntrada((int)n);
        double faseFinal = calculaFaseSinalSaida((int)n, Math.toRadians(ganhoDeslocamentoFase));;
        double amplitudeInicial = calculaEspectroSinalEntrada((int)n);
        double amplitudeFinal = calculaEspectroSinalSaida((int)n, ganhoAmplitude);

        responseModel.yEspectroSinalEntrada.add(amplitudeInicial);
        responseModel.yFaseSinalEntrada.add(Math.toDegrees(faseInicial));
        responseModel.yEspectroSinalSaida.add(amplitudeFinal);
        responseModel.yFaseSinalSaida.add(Math.toDegrees(faseFinal));

        double pi2 = 2 * Math.PI;
        double pi2FrequenciaSinal = pi2 * frequenciaSinal;

        for(int a = 0; a < responseModel.xTempo.size(); a++){
            double xTempo = responseModel.xTempo.get(a);
            double cosEntrada = Math.cos(n * pi2FrequenciaSinal * xTempo + faseInicial);
            double cosSaida = Math.cos( n * pi2FrequenciaSinal * xTempo + faseFinal);
            double ySinalEntrada = responseModel.ySinalEntrada.get(a);
            double ySinalSaida = responseModel.ySinalSaida.get(a);
            responseModel.ySinalEntrada.set(a, ySinalEntrada + amplitudeInicial * cosEntrada);
            responseModel.ySinalSaida.set(a, ySinalSaida + amplitudeFinal * cosSaida);
        }
    }

    public static double calculaEspectroSinalEntrada(int harmonica) {
        if(harmonica != 0) {
            return 2 / (harmonica * Math.PI);
        }
        else{
            return  0;
        }
    }

    public static double calculaFaseSinalEntrada(int harmonica) {
        if(harmonica != 0){
            if(harmonica % 2 == 0) {
                return Math.PI / 2; //Esta em radianos
            }
            else{
                return- Math.PI / 2; //Esta em radianos
            }
        }
        else{
            return  0;
        }
    }

    public static double calculaGanhoAmplitudeCanal(double frequencia, double frequenciaCorte) {
        return 1/ Math.sqrt(1 + Math.pow((frequencia/frequenciaCorte), 2));
    }

    public static double calculaContribuicaoFaseCanal(double frequencia, double frequenciaCorte) {
        return -Math.atan(frequencia/frequenciaCorte);
    }

    public static double calculaEspectroSinalSaida(int harmonica, double ganhoAmplitudeCanal) {

        if(harmonica != 0) {
            return ganhoAmplitudeCanal * (2 / (harmonica * Math.PI));
        }
        else{
            return  0;
        }
    }

    public static double calculaFaseSinalSaida(int harmonica, double deslocamentoFaseCanal) {
        if(harmonica != 0){
            if(harmonica % 2 == 0) {
                return deslocamentoFaseCanal + Math.PI / 2; //Esta em radianos
            }
            else{
                return deslocamentoFaseCanal + -Math.PI / 2; //Esta em radianos
            }
        }
        else{
            return  0;
        }
    }
}
