package br.com.serra_serra_serrador.serra_serra_serrador.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphResponseModel{
    public List<Double> xHarmonica;
    public List<Double> xTempo;
    public List<Double> xFrequenciaCanal;
    public List<Double> ySinalEntrada;
    public List<Double> yEspectroSinalEntrada;
    public List<Double> yFaseSinalEntrada;
    public List<Double> yCalculoAmplitudeCanal;
    public  List<Double> yDeslocamentoFaseCanal;
    public  List<Double> ySinalSaida;
    public  List<Double> yEspectroSinalSaida;
    public  List<Double> yFaseSinalSaida;
    public GraphResponseModel() {
        this.xHarmonica = new ArrayList<>();
        this.xTempo = new ArrayList<>();
        this.xFrequenciaCanal = new ArrayList<>();
        this.ySinalEntrada = new ArrayList<>();
        this.yEspectroSinalEntrada = new ArrayList<>();
        this.yFaseSinalEntrada = new ArrayList<>();
        this.yCalculoAmplitudeCanal = new ArrayList<>();
        this.yDeslocamentoFaseCanal = new ArrayList<>();
        this.ySinalSaida = new ArrayList<>();
        this.yEspectroSinalSaida = new ArrayList<>();
        this.yFaseSinalSaida = new ArrayList<>();
    }
}
