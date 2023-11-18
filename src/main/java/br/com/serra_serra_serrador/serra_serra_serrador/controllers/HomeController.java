package br.com.serra_serra_serrador.serra_serra_serrador.controllers;

import br.com.serra_serra_serrador.serra_serra_serrador.services.GraphServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class HomeController {
    //criar serviços para cada tipo de gráfico
    @Autowired
    private GraphServiceImpl graphService; //injeção de dependência para o serviço de gráficos


    //Recebe requisição e retorna uma resposta
    @GetMapping("/RealizaOperacoes")
    @ResponseBody
    public Map<String, Object> realizaOperacoes(@RequestParam double frequenciaSinal, @RequestParam double frequenciaCanal) {

        return GraphServiceImpl.realizaOperacoes(frequenciaSinal, frequenciaCanal);
    }

    @GetMapping("/index")
    public String redirecionarParaIndex() {
        return "index";
    }

    @GetMapping("/integrantes")
    public String redirecionarParaIntegrantes() {
        return "integrantes";
    }
}
