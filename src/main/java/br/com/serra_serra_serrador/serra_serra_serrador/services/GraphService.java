package br.com.serra_serra_serrador.serra_serra_serrador.services;

import br.com.serra_serra_serrador.serra_serra_serrador.models.GraphRequestModel;
import br.com.serra_serra_serrador.serra_serra_serrador.models.GraphResponseModel;

import java.util.List;
import java.util.Map;

public interface GraphService {
    //dependendo do tipo de cálculo, se ele muda para entrada e saída, criar métodos específicos para entrada e saída
    public List<GraphResponseModel> calculaSinalAoLongoDoTempo(GraphRequestModel model);
    public List<GraphResponseModel> calculaAmplitudeFourier(GraphRequestModel model);
    public List<GraphResponseModel> calculaFaseFourier(GraphRequestModel model);
    public Map<String, Object> calculaGanhoAmplitude(double frequenciaCanal);
    public List<GraphResponseModel> calculaContribuicaoFase(GraphRequestModel model);
}
