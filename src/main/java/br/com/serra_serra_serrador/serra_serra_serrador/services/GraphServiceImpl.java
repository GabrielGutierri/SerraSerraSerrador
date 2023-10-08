package br.com.serra_serra_serrador.serra_serra_serrador.services;

import br.com.serra_serra_serrador.serra_serra_serrador.models.GraphRequestModel;
import br.com.serra_serra_serrador.serra_serra_serrador.models.GraphResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GraphServiceImpl implements  GraphService{

    @Autowired
    public GraphServiceImpl() {
    }

    @Override
    public List<GraphResponseModel> calculaSinalAoLongoDoTempo(GraphRequestModel model) {
        return null;
    }

    @Override
    public List<GraphResponseModel> calculaAmplitudeFourier(GraphRequestModel model) {
        return null;
    }

    @Override
    public List<GraphResponseModel> calculaFaseFourier(GraphRequestModel model) {
        return null;
    }

    @Override
    public List<GraphResponseModel> calculaGanhoAmplitude(GraphRequestModel model) {
        return null;
    }

    @Override
    public List<GraphResponseModel> calculaContribuicaoFase(GraphRequestModel model) {
        return null;
    }
}
