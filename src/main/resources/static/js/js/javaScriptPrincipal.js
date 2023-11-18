Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';

let chartGraficoCanal, chartSinais, chartEspectroSinais,
chartFaseSinais;

function verificaNumeros(fSinal, fCanal){
    let regex = /[,.]/;
    if((parseInt(fSinal) < 0) || parseInt(fCanal) < 0 || regex.test(fSinal) || regex.test(fCanal)){
        Swal.fire({
          title: 'Atenção!',
          text: 'Os dados de frequência devem ser inteiros positivos!',
          icon: 'warning',
          confirmButtonText: 'OK'
        });
        return false;
    }
    return true;
}

function Plota() {
    let vfrequenciaSinal = document.querySelector('#frequenciaSinal').value;
	let vfrequenciaCanal = document.querySelector('#frequenciaCanal').value;

    if(verificaNumeros(vfrequenciaSinal, vfrequenciaCanal)){
    let divGraficos = document.querySelector('#container-graficos');
    let divSemGrafico = document.querySelector('#container-vazio');
    divSemGrafico.classList.add('hidden');
    divGraficos.style.display = 'block';

	$.ajax({
    		url: "RealizaOperacoes",
    		data: { frequenciaSinal: vfrequenciaSinal, frequenciaCanal: vfrequenciaCanal},
    		success: function (dados) {
                console.log(dados);
    			if (dados.erro != undefined) {
    				alert(dados.msg);
    			}
    			else {
    				criaGraficos(dados);
    			}
    		},
    	});
    }
}

function criaGraficos(dados){

    var ctxGraficoCanal = document.getElementById("graficoCanal");
    var ctxSinais = document.getElementById("sinais");
    var ctxEspectroSinais = document.getElementById("espectroSinais");
    var ctxFaseSinais = document.getElementById("faseSinais");

    if (chartGraficoCanal) {
        chartGraficoCanal.destroy();
    }
    chartGraficoCanal = new Chart(ctxGraficoCanal, criaGraficoLinhaCanal(dados.dadosxFrequenciaCanal, dados.dadosyCalculoAmplitudeCanal, dados.dadosyDeslocamentoFaseCanal));

    if(chartSinais){
        chartSinais.destroy();
    }
    chartSinais = new Chart(ctxSinais, criaGraficoLinhaSinais(dados.dadosxTempo, dados.dadosySinalEntrada, dados.dadosySinalSaida));

    if(chartEspectroSinais){
        chartEspectroSinais.destroy();
    }
    chartEspectroSinais = new Chart(ctxEspectroSinais, criaGraficoBarraEspectro(dados.dadosxHarmonica, dados.dadosyEspectroSinalEntrada, dados.dadosyEspectroSinalSaida));

    if(chartFaseSinais){
        chartFaseSinalEntrada.destroy();
    }
    chartFaseSinalEntrada = new Chart(ctxFaseSinais, criaGraficoBarraFase(dados.dadosxHarmonica, dados.dadosyFaseSinalEntrada, dados.dadosyFaseSinalSaida));

}


function criaGraficoLinha(labels, datasets, ticks){
    var config = {
        type: 'line',
        data: {
            labels: labels,
            datasets: datasets
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            parsing: false,
            normalized: true,
            animation: false,
            spanGaps: false,
            layout: {
                padding: {
                    left: 10,
                    right: 25,
                    top: 25,
                    bottom: 0
                }
            },
            scales: {
                xAxes: [{
                  gridLines: {
                    display: false,
                    drawBorder: false
                  },
                  ticks: {
                    maxTicksLimit: 7
                  }
                }],
                yAxes: [{
                  suggestedMin: -90,
                  suggestedMax: 2,
                  ticks: ticks,
                  gridLines: {
                    color: "rgb(234, 236, 244)",
                    zeroLineColor: "rgb(234, 236, 244)",
                    drawBorder: false,
                    borderDash: [2],
                    zeroLineBorderDash: [2]
                  }
                }],
            },
            legend: {
                display: true
            },
            tooltips: {
                backgroundColor: "rgb(255,255,255)",
                bodyFontColor: "#858796",
                titleMarginBottom: 10,
                titleFontColor: '#6e707e',
                titleFontSize: 14,
                borderColor: '#dddfeb',
                borderWidth: 1,
                xPadding: 15,
                yPadding: 15,
                displayColors: false,
                intersect: false,
                mode: 'index',
                caretPadding: 10,
                callbacks: {
                    label: function (tooltipItem, chart) {
                        var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
                        return datasetLabel  + ": " + tooltipItem.yLabel;
                    }
                }
            },
            datasets: {
                    line: {
                        pointRadius: 0 // disable for all `'line'` datasets
                    }
               },
            elements: {
                point: {
                    radius: 0 // default to disabled in all datasets
                }
            }
        }
    };
    return config;
}

function criaGraficoLinhaCanal(dadosX, dadosYGanhaAmplitude, dadosYDeslocamentoFase) {
    let datasets = [{
                       label: "Ganho Amplitude Canal",
                       lineTension: 0,
                       backgroundColor: "rgba(4, 31, 74, 0.05)",
                       borderColor: "rgba(4, 31, 74, 1)", // Cor da primeira linha
                       pointRadius: 0.5,
                       pointBackgroundColor: "rgba(4, 31, 74, 1)",
                       pointBorderColor: "rgba(4, 31, 74, 1)",
                       pointHoverRadius: 0.5,
                       pointHoverBackgroundColor: "rgba(4, 31, 74, 1)",
                       pointHoverBorderColor: "rgba(4, 31, 74, 1)",
                       pointHitRadius: 5,
                       pointBorderWidth: 1,
                       spanGaps: true,
                       data: dadosYGanhaAmplitude,
                   },
                   {
                       label: "Deslocamento Fase Canal",
                       lineTension: 0,
                       backgroundColor: "rgba(179, 148, 11, 0.05)",
                       borderColor: "rgba(179, 148, 11, 1)", // Cor da segunda linha
                       pointRadius: 0.5,
                       pointBackgroundColor: "rgba(179, 148, 11, 1)",
                       pointBorderColor: "rgba(179, 148, 11, 1)",
                       pointHoverRadius: 0.5,
                       pointHoverBackgroundColor: "rgba(179, 148, 11, 1)",
                       pointHoverBorderColor: "rgba(179, 148, 11, 1)",
                       pointHitRadius: 5,
                       pointBorderWidth: 1,
                       spanGaps: true,
                       data: dadosYDeslocamentoFase,
                   }];
    let ticks = {
                maxTicksLimit: 5,
                padding: 10,
                callback: function(value, index, values) {
                  return 'Frequência: ' + value + 'Hz';
                }};

    return criaGraficoLinha(dadosX, datasets, ticks);

}

function criaGraficoLinhaSinais(dadosX, dadosYSinalEntrada, dadosYSinalSaida) {
    // Formata a lista dadosX para ter no máximo 2 casas decimais
    var dadosXFormatados = formataListaParaCasasDecimais(dadosX, 5);
    let datasets = [{
                    label: "Sinal de Entrada",
                    lineTension: 0,
                    backgroundColor: "rgba(199, 18, 8, 0.05)",
                    borderColor: "rgba(199, 18, 8, 1)", // Cor da primeira linha
                    pointRadius: 0.5,
                    pointBackgroundColor: "rgba(199, 18, 8, 1)",
                    pointBorderColor: "rgba(199, 18, 8, 1)",
                    pointHoverRadius: 0.5,
                    pointHoverBackgroundColor: "rgba(199, 18, 8, 1)",
                    pointHoverBorderColor: "rgba(199, 18, 8, 1)",
                    pointHitRadius: 5,
                    pointBorderWidth: 1,
                    spanGaps: true,
                    data: dadosYSinalEntrada,
                   },
                   {
                    label: "Sinal de Saída",
                    lineTension: 0,
                    backgroundColor: "rgba(6, 138, 148, 0.05)",
                    borderColor: "rgba(6, 138, 148, 1)", // Cor da segunda linha
                    pointRadius: 0.5,
                    pointBackgroundColor: "rgba(6, 138, 148, 1)",
                    pointBorderColor: "rgba(6, 138, 148, 1)",
                    pointHoverRadius: 0.5,
                    pointHoverBackgroundColor: "rgba(6, 138, 148, 1)",
                    pointHoverBorderColor: "rgba(6, 138, 148, 1)",
                    pointHitRadius: 5,
                    pointBorderWidth: 1,
                    spanGaps: true,
                    data: dadosYSinalSaida,
                   }];
    let ticks = {
                maxTicksLimit: 5,
                padding: 10,
                callback: function(value, index, values) {
                    return 'Amplitude: ' + value + 'V';
                }};

    return criaGraficoLinha(dadosXFormatados, datasets, ticks);

}

function criaGraficoBarra(labels, datasets, ticks, datasetLabel){
    var config = {
      type: 'bar',
      data: {
        labels: labels,
        datasets: datasets
      },
      options: {
        maintainAspectRatio: false,
        layout: {
          padding: {
            left: 10,
            right: 25,
            top: 25,
            bottom: 0
          }
        },
        scales: {
          xAxes: [{
            time: {
              unit: 'month'
            },
            gridLines: {
              display: false,
              drawBorder: false
            },
            ticks: {
              maxTicksLimit: 6
            },
          }],
          yAxes: [{
            ticks: ticks,
            gridLines: {
              color: "rgb(234, 236, 244)",
              zeroLineColor: "rgb(234, 236, 244)",
              drawBorder: false,
              borderDash: [2],
              zeroLineBorderDash: [2]
            }
          }],
        },
        legend: {
          display: true
        },
        tooltips: {
          titleMarginBottom: 10,
          titleFontColor: '#6e707e',
          titleFontSize: 14,
          backgroundColor: "rgb(255,255,255)",
          bodyFontColor: "#858796",
          borderColor: '#dddfeb',
          borderWidth: 1,
          xPadding: 15,
          yPadding: 15,
          displayColors: false,
          caretPadding: 10,
          callbacks: {
            label: function(tooltipItem, chart) {
              var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
              return datasetLabel + ": " + tooltipItem.yLabel;
            }
          }
        },
      }
    };
    return config;
}

function criaGraficoBarraEspectro(dadosX, dadosYEntrada, dadosYSaida){

    let datasets =  [{
                     label: "Espectro Sinal Entrada",
                     backgroundColor: "rgba(84, 9, 176, 1)",
                     hoverBackgroundColor: "rgba(84, 9, 176, 1)",
                     borderColor: "rgba(84, 9, 176, 1)",
                     data: dadosYEntrada,
                   },
                   {
                     label: "Espectro Sinal Saída",
                     backgroundColor: "#129406",
                     hoverBackgroundColor: "#129406",
                     borderColor: "#129406",
                     data: dadosYSaida,
                   }];
    let ticks = {
                /*
                min: -1,
                max: 1,
                */
                maxTicksLimit: 5,
                padding: 10,
                callback: function(value, index, values) {
                    return 'Frequência: ' + value + 'Hz';
                }};
    let datasetLabel = 'Harmônica';
    return criaGraficoBarra(dadosX, datasets, ticks, datasetLabel);
}

function criaGraficoBarraFase(dadosX, dadosYEntrada, dadosYSaida){
    let datasets =  [{
                    label: "Fase Sinal Entrada",
                    backgroundColor: "#4e73df",
                    hoverBackgroundColor: "#2e59d9",
                    borderColor: "#4e73df",
                    data: dadosYEntrada,
                },{
                    label: "Fase Sinal Saída",
                    backgroundColor: "rgba(59, 184, 123, 1)",
                    hoverBackgroundColor: "rgba(59, 184, 123, 1)",
                    borderColor: "rgba(59, 184, 123, 1)",
                    data: dadosYSaida,
                }];
        let ticks = {
                    /*
                      min: -1,
                      max: 1,
                    */
                    maxTicksLimit: 5,
                    padding: 10,
                    callback: function(value, index, values) {
                        return 'Fase: ' + value + '°';
                  }};
    let datasetLabel = 'Harmônica';
    return criaGraficoBarra(dadosX, datasets, ticks, datasetLabel);

}

function formataListaParaCasasDecimais(lista, casasDecimais) {
    return lista.map(function(valor) {
        return parseFloat(valor.toFixed(casasDecimais));
    });
}