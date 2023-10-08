package br.com.serra_serra_serrador.serra_serra_serrador.controllers;

import br.com.serra_serra_serrador.serra_serra_serrador.models.GraphRequestModel;
import br.com.serra_serra_serrador.serra_serra_serrador.models.GraphResponseModel;
import br.com.serra_serra_serrador.serra_serra_serrador.services.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@RestController
//@RequestMapping("/api")
@Controller
public class HomeController {
    //criar serviços para cada tipo de gráfico
    private GraphService graphService; //injeção de dependência para o serviço de gráficos
    //Criar um serviço para cada tipo de gráfico?
    @Autowired
    public HomeController(GraphService graphService) {
        this.graphService = graphService;
    }

    //Como iremos retornar os dados na forma de gráficos??
    @GetMapping("/teste")
    public String Teste(Model model){
        model.addAttribute("teste", "teste");
        return "Teste";
    }


    @GetMapping("/sinalEntradaTempo") //sinal de entrada ao longo do tempo
    public List<GraphResponseModel> calculaSinalEntradaAoLongoDoTempo(@RequestBody GraphRequestModel model){
        return graphService.calculaSinalAoLongoDoTempo(model);
    }

    @GetMapping("/amplitudeEntradaFourier")
    public List<GraphResponseModel> calculaAmplitudeEntradaFourier(@RequestBody GraphRequestModel model){
        return graphService.calculaAmplitudeFourier(model);
    }

    @GetMapping("/faseEntradaFourier")
    public List<GraphResponseModel> calculaFaseEntradaFourier(@RequestBody GraphRequestModel model){
        return graphService.calculaFaseFourier(model);
    }

    @GetMapping("/ganhoAmplitude")
    public List<GraphResponseModel> calculaGanhoAmplitude(@RequestBody GraphRequestModel model){
        return graphService.calculaGanhoAmplitude(model);
    }

    @GetMapping("/contribuicaoFase")
    public List<GraphResponseModel> calculaContribuicaoFase(@RequestBody GraphRequestModel model){
        return graphService.calculaContribuicaoFase(model);
    }

    @GetMapping("/sinalSaidaTempo") //sinal de saida ao longo do tempo
    public List<GraphResponseModel> calculaSinalSaidaAoLongoDoTempo(@RequestBody GraphRequestModel model){
        return graphService.calculaSinalAoLongoDoTempo(model);
    }

    @GetMapping("/amplitudeSaidaFourier")
    public List<GraphResponseModel> calculaAmplitudeSaidaFourier(@RequestBody GraphRequestModel model){
        return graphService.calculaAmplitudeFourier(model);
    }

    @GetMapping("/faseSaidaFourier")
    public List<GraphResponseModel> calculaFaseSaidaFourier(@RequestBody GraphRequestModel model){
        return graphService.calculaFaseFourier(model);
    }
}
