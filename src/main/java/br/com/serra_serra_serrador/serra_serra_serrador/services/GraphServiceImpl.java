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

@Service
public class GraphServiceImpl{


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
            calculaDadosSinalEntrada(i, responseModel, frequenciaSinal);
            calculaDadosCanal(i, responseModel, frequenciaSinal, frequenciaCanal);
            calculaDadosSinalSaida(i, responseModel, frequenciaSinal, responseModel.yCalculoAmplitudeCanal.get(i) , responseModel.yDeslocamentoFaseCanal.get(i));
        }

        return montaResponse(responseModel);
    }

    public static void calculaDadosSinalEntrada(double n, GraphResponseModel responseModel, double frequenciaSinal) {

        double fase, amplitude;
        amplitude = calculaEspectroSinalEntrada((int)n);
        fase = calculaFaseSinalEntrada((int)n);
        responseModel.yEspectroSinalEntrada.add(amplitude);
        responseModel.yFaseSinalEntrada.add(Math.toDegrees(fase));

        for(int a = 0; a < responseModel.xTempo.size(); a++){
            responseModel.ySinalEntrada.set(a, responseModel.ySinalEntrada.get(a) + amplitude * Math.cos(2* Math.PI * n * frequenciaSinal * responseModel.xTempo.get(a) + fase));
        }
    }

    public static void calculaDadosCanal(double n, GraphResponseModel responseModel, double frequenciaSinal, double frequenciaCanal) {
        responseModel.xFrequenciaCanal.add(n * frequenciaSinal);
        responseModel.yCalculoAmplitudeCanal.add(calculaGanhoAmplitudeCanal(n * frequenciaSinal, frequenciaCanal));
        responseModel.yDeslocamentoFaseCanal.add(Math.toDegrees(calculaContribuicaoFaseCanal(n * frequenciaSinal, frequenciaCanal)));
    }

    public static void calculaDadosSinalSaida(double n, GraphResponseModel responseModel, double frequenciaSinal, double ganhoAmplitude, double ganhoDeslocamentoFase) {

        double faseFinal, amplitudeFinal;
        amplitudeFinal = calculaEspectroSinalSaida((int)n, ganhoAmplitude);
        faseFinal = calculaFaseSinalSaida((int)n, Math.toRadians(ganhoDeslocamentoFase));
        responseModel.yEspectroSinalSaida.add(amplitudeFinal);
        responseModel.yFaseSinalSaida.add(Math.toDegrees(faseFinal));

        for(int a = 0; a < responseModel.xTempo.size(); a++){
            responseModel.ySinalSaida.set(a, responseModel.ySinalSaida.get(a) + amplitudeFinal * Math.cos(2* Math.PI * n * frequenciaSinal * responseModel.xTempo.get(a) + faseFinal));
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









     /*
     AQUI ESTÁ UMA OUTRA LÓGICA PARA A PARTE DA OPERAÇÃO.
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
    */
}
