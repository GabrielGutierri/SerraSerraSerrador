package br.com.serra_serra_serrador.serra_serra_serrador.services;

import br.com.serra_serra_serrador.serra_serra_serrador.models.GraphRequestModel;
import br.com.serra_serra_serrador.serra_serra_serrador.models.GraphResponseModel;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.TransformType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.FastFourierTransformer;

import java.util.*;

@Service
public class GraphServiceImpl{
    private static final int puloEixoGrafico = 500;
    private static final int numeroHarmonicas = 50;

    public static Map<String, Object> realizaOperacoes(double frequenciaSinal, double frequenciaCanal) {
        GraphResponseModel responseModel = new GraphResponseModel();
        /*
        double pulosEixoXGraficosTempo, pulosEixoXGraficosFrequenciaCanal;
        if(frequenciaSinal < puloEixoGrafico){
            pulosEixoXGraficosTempo = frequenciaSinal / puloEixoGrafico; // calcula a distancia entre os pontos do gráfico;
            pulosEixoXGraficosFrequenciaCanal = frequenciaSinal / puloEixoGrafico;
        }
        else{
            pulosEixoXGraficosTempo = 1;
            pulosEixoXGraficosFrequenciaCanal = 1;
        }
        */

        /*
        Dúvidas:
         1 - A frequência para o eixo X do canal, que varia por frequência, deve aumentar de 1 a 1, ou pode
        considerar o que eu fiz que é levando em consideração a taxa de amostragem, e o numero de amostras?
        2 - Mostrar gráficos para o professor para confirmar os calculos.
        */

        realizaLacoTempo(frequenciaSinal, frequenciaCanal, responseModel);
        //realizaLacoFrequencia(frequenciaSinal, frequenciaCanal, responseModel);
        //realizaLacoHarmonicas(frequenciaSinal, frequenciaCanal, responseModel);

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
    private static void realizaLacoTempo(double frequenciaSinal, double frequenciaCanal,  GraphResponseModel responseModel){

        double numSamples = 256; // Número de amostras, PRECISA SER POTÊNCIA DE 2
        double sampleRate = 44100.0; // Taxa de amostragem (amostras por segundo)

        //Esse laço calcula os valores para o eixo X dos gráficos de tempo, e também do sinal de entrada;
        List<Double> inputSignal = new ArrayList<Double>();
        for (double i = 0; i < numSamples; i++) {
            responseModel.xTempo.add(i);
            inputSignal.add(2.0 * (i % (sampleRate / frequenciaSinal)) / (sampleRate / frequenciaSinal) - 1.0);
        }
        responseModel.ySinalEntrada = inputSignal;

        //Converte a lista do sinal de entrada para array;
        double[] inputSignalArray = new double[inputSignal.size()];
        for (int i = 0; i < inputSignal.size(); i++) {
            inputSignalArray[i] = inputSignal.get(i);
        }

        //Converter o numero de amostras e a amostragem para frequência, eixo X dos gráficos de frequência;
        double[] frequencias = new double[(int)numSamples / 2];
        for (int i = 0; i < numSamples / 2; i++) {
            frequencias[i] = i * sampleRate / numSamples;
            responseModel.xFrequenciaCanal.add(frequencias[i]);
        }

        // Calcula a FFT do sinal de entrada
        FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
        Complex[] spectrum = transformer.transform(inputSignalArray, TransformType.FORWARD);


        for (int i = 0; i < numSamples / 2; i++) {

            //Representa eixo X para valores de amostragem pela metade. Exemplo: 256 vira 128;
            responseModel.xHarmonica.add((double)i);

            //Calcula eixo Y fase e espectro do sinal de entrada;
            double real = spectrum[i].getReal();
            double imag = spectrum[i].getImaginary();
            responseModel.yEspectroSinalEntrada.add(Math.sqrt(real * real + imag * imag));
            responseModel.yFaseSinalEntrada.add(Math.toDegrees(Math.atan2(imag, real)));

            //Calcula eixo Y dos gráficos de canal
            double frequenciaSinalNovo = frequencias[i];
            double ganhoAmplitude = 1 / Math.sqrt(1 + Math.pow((frequenciaSinalNovo / frequenciaCanal), 2));
            responseModel.yCalculoAmplitudeCanal.add(ganhoAmplitude);
            double fase = Math.toRadians(-Math.atan(frequenciaSinalNovo / frequenciaCanal));
            responseModel.yDeslocamentoFaseCanal.add(fase);

            //Calcula eixo Y fase e espectro do sinal de saída;
            double novoReal = real * ganhoAmplitude * Math.cos(fase);
            double novoImag = imag * ganhoAmplitude * Math.sin(fase);
            responseModel.yFaseSinalSaida.add(Math.toDegrees(Math.atan2(novoImag, novoReal)));
            responseModel.yEspectroSinalSaida.add(Math.sqrt(novoReal * novoReal + novoImag * novoImag));
            spectrum[i] = new Complex(novoReal, novoImag);
        }

        //Calcula eixo Y sinal de saída;
        Complex[] outputSpectrum = transformer.transform(spectrum, TransformType.INVERSE);
        for (int i = 0; i < numSamples; i++) {
            responseModel.ySinalSaida.add(outputSpectrum[i].getReal());
        }

    }


    private static void realizaLacoFrequencia(double frequenciaSinal, double frequenciaCanal,  GraphResponseModel responseModel){
        /*
        for(double i=0; i <= (frequenciaSinal * 2); i += (pulosEixoXGraficosFrequenciaCanal * 2)){
            //responseModel.xFrequenciaCanal.add(i);
            //responseModel.yCalculoAmplitudeCanal.add(calculaGanhoAmplitudeCanal(i,frequenciaCanal));
            //responseModel.yDeslocamentoFaseCanal.add(calculaContribuicaoFaseCanal(i,frequenciaCanal));

        }
        */

    }
    private static void realizaLacoHarmonicas(Double frequenciaSinal, Double frequenciaCanal, GraphResponseModel responseModel){
        for(double i = 0; i <= 50; i += 1){
            //responseModel.xHarmonica.add(i);
            //responseModel.yEspectroSinalEntrada.add(calculaEspectroSinalEntrada(i, frequenciaSinal));
            //responseModel.yFaseSinalEntrada.add(calculaFaseSinalEntrada(i));
            //responseModel.yEspectroSinalSaida.add(calculaEspectroSinalSaida(i, frequenciaCanal));
            //responseModel.yFaseSinalSaida.add(calculaFaseSinalSaida(i, frequenciaCanal));
        }



    }
    public static double calculaGanhoAmplitudeCanal(double frequenciaSinal, double frequenciaCanal) {
        // 1/ Math.sqrt(1 + Math.pow((frequenciaSinal/frequenciaCanal), 2));

        return 1;
    }

    public static double calculaContribuicaoFaseCanal(double frequenciaSinal, double frequenciaCanal) {
        // Math.toDegrees(-Math.atan(frequenciaSinal/frequenciaCanal));
        return 1;
    }

    public static double calculaEspectroSinalEntrada(double i, double frequenciaSinal) {

       return 1;
    }

    public static double calculaFaseSinalEntrada(double i) {
        return 2;
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

    public static double calculaSinalEntrada(double i, double frequenciaCanal) {
        return 6;
    }
}
