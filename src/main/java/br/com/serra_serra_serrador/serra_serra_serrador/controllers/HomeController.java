package br.com.serra_serra_serrador.serra_serra_serrador.controllers;

import br.com.serra_serra_serrador.serra_serra_serrador.models.GraphRequestModel;
import br.com.serra_serra_serrador.serra_serra_serrador.models.GraphResponseModel;
import br.com.serra_serra_serrador.serra_serra_serrador.services.GraphService;
import br.com.serra_serra_serrador.serra_serra_serrador.services.GraphServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

//@RestController
//@RequestMapping("/api")
@RestController
public class HomeController {
    //criar serviços para cada tipo de gráfico
    @Autowired
    private GraphServiceImpl graphService; //injeção de dependência para o serviço de gráficos


    //Recebe requisição e retorna uma resposta
    @GetMapping("/RealizaOperacoes")
    public Map<String, Object> realizaOperacoes(@RequestParam double frequenciaSinal, @RequestParam double frequenciaCanal) {

        return GraphServiceImpl.realizaOperacoes(frequenciaSinal, frequenciaCanal);
    }
}
