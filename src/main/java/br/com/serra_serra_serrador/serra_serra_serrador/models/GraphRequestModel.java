package br.com.serra_serra_serrador.serra_serra_serrador.models;

import lombok.*;

public class GraphRequestModel {
    private Double frequenciaSinal;
    private Double frequenciaCanal;

    public GraphRequestModel(Double frequenciaSinal, Double frequenciaCanal) {
        this.frequenciaSinal = frequenciaSinal;
        this.frequenciaCanal = frequenciaCanal;
    }

}
