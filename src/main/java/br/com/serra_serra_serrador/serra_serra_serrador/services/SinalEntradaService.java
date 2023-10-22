package br.com.serra_serra_serrador.serra_serra_serrador.services;

import java.util.*;

public class SinalEntradaService {
    private List<Double> createXGraficoSinalEntrada(double T, int numeroCiclos){
        List<Double> xGrafico = new ArrayList<>();
        double epsilon = 1e-10; // small error tolerance
        for(double i = 0; i<= numeroCiclos * T + epsilon; i+= T/2){
            xGrafico.add(i);
        }
        return xGrafico;
    }

    private List<Double> createXCiclo(double T, double start, double end) {
        List<Double> xCiclo = new ArrayList<>();
        double epsilon = 1e-10; // small error tolerance
        for (double i = start * T; i <= end * T + epsilon; i += T / 2) {
            xCiclo.add(i);
        }
        return xCiclo;
    }

    private List<List<Double>> createXCiclos(int numeroCiclos, double T){
        List<List<Double>> xCiclos = new ArrayList<>();
        xCiclos.add(createXCiclo(T, 0, 0.5));//primeiro ciclo vai ser fixo, do 0 ao T/2;
        for (double cycle = 0; cycle < numeroCiclos - 1; cycle++) {
            double start = 0.5 + cycle;
            double end = start + 1;
            xCiclos.add(createXCiclo(T, start, end));
        }
        xCiclos.add(createXCiclo(T, numeroCiclos - 0.5, (double)numeroCiclos));
        return xCiclos;
    }

    private List<List<Double>> createYCiclos(int numeroCiclos){
        List<List<Double>> yCiclos = new ArrayList<>();
        for(int i=0; i<= numeroCiclos; i++){
            if(i == 0){
                List<Double> yCiclo = Arrays.asList(0.0, 1.0);
                yCiclos.add(yCiclo);
            }
            if(i > 0 && i <= numeroCiclos - 1){
                List<Double> yCiclo = Arrays.asList(-1.0, 0.0, 1.0);
                yCiclos.add(yCiclo);
            }
            if(i == numeroCiclos){
                List<Double> yCiclo = Arrays.asList(-1.0, 0.0);
                yCiclos.add(yCiclo);
            }
        }
        return yCiclos;
    }
    public Map<String, Object> calculaGraficoSinalEntrada(double frequenciaSinal){
        double T = 1/frequenciaSinal;
        int numeroCiclos = 3;
        List<Double> xGrafico = createXGraficoSinalEntrada(T, numeroCiclos);
        List<List<Double>> xCiclos = createXCiclos(numeroCiclos, T);
        List<List<Double>> yCiclos = createYCiclos(numeroCiclos);

        Map<String, Object> response = new HashMap<>();
        response.put("dadosXGrafico", xGrafico);
        response.put("dadosXCiclos", xCiclos);
        response.put("dadosYCiclos", yCiclos);
        return response;
    }

}
