Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';

let chartGraficoCanal, chartSinais, chartEspectroSinalEntrada,
chartFaseSinalEntrada, chartEspectroSinalSaida, chartFaseSinalSaida;

function Plota() {
    let divGraficos = document.querySelector('#container-graficos');
    let divSemGrafico = document.querySelector('#container-vazio');
    divSemGrafico.classList.add('hidden');
    divGraficos.style.display = 'block';

    let vfrequenciaSinal = document.querySelector('#frequenciaSinal').value;
	let vfrequenciaCanal = document.querySelector('#frequenciaCanal').value;

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

function criaGraficos(dados){

    var ctxGraficoCanal = document.getElementById("graficoCanal");
    var ctxSinais = document.getElementById("sinais");
    var ctxEspectroSinalEntrada = document.getElementById("espectroSinalEntrada");
    var ctxFaseSinalEntrada = document.getElementById("faseSinalEntrada");
    var ctxEspectroSinalSaida = document.getElementById("espectroSinalSaida");
    var ctxFaseSinalSaida = document.getElementById("faseSinalSaida");

    if (chartGraficoCanal) {
        chartGraficoCanal.destroy();
        chartGraficoCanal = new Chart(ctxGraficoCanal, criaGraficoLinhaCanal(dados.dadosxFrequenciaCanal, dados.dadosyCalculoAmplitudeCanal, dados.dadosyDeslocamentoFaseCanal));
    } else {
        chartGraficoCanal = new Chart(ctxGraficoCanal, criaGraficoLinhaCanal(dados.dadosxFrequenciaCanal, dados.dadosyCalculoAmplitudeCanal, dados.dadosyDeslocamentoFaseCanal));
    }

    if(chartSinais){
        chartSinais.destroy();
        chartSinais = new Chart(ctxSinais, criaGraficoLinhaSinais(dados.dadosxTempo, dados.dadosySinalEntrada, dados.dadosySinalSaida));

    } else {
        chartSinais = new Chart(ctxSinais, criaGraficoLinhaSinais(dados.dadosxTempo, dados.dadosySinalEntrada, dados.dadosySinalSaida));
    }

    if(chartEspectroSinalEntrada){
        chartEspectroSinalEntrada.destroy();
        chartEspectroSinalEntrada = new Chart(ctxEspectroSinalEntrada, criaGraficoBarra(dados.dadosxHarmonica, dados.dadosyEspectroSinalEntrada));
    } else {
        chartEspectroSinalEntrada = new Chart(ctxEspectroSinalEntrada, criaGraficoBarra(dados.dadosxHarmonica, dados.dadosyEspectroSinalEntrada));
    }

    if(chartFaseSinalEntrada){
        chartFaseSinalEntrada.destroy();
        chartFaseSinalEntrada = new Chart(ctxFaseSinalEntrada, criaGraficoBarra(dados.dadosxHarmonica, dados.dadosyFaseSinalEntrada));
    } else {
        chartFaseSinalEntrada = new Chart(ctxFaseSinalEntrada, criaGraficoBarra(dados.dadosxHarmonica, dados.dadosyFaseSinalEntrada));
    }

    if(chartEspectroSinalSaida){
        chartEspectroSinalSaida.destroy();
        chartEspectroSinalSaida = new Chart(ctxEspectroSinalSaida, criaGraficoBarra(dados.dadosxHarmonica, dados.dadosyEspectroSinalSaida));
    } else {
        chartEspectroSinalSaida = new Chart(ctxEspectroSinalSaida, criaGraficoBarra(dados.dadosxHarmonica, dados.dadosyEspectroSinalSaida));
    }

    if(chartFaseSinalSaida){
        chartFaseSinalSaida.destroy();
        chartFaseSinalSaida = new Chart(ctxFaseSinalSaida, criaGraficoBarra(dados.dadosxHarmonica, dados.dadosyFaseSinalSaida));
    } else {
        chartFaseSinalSaida = new Chart(ctxFaseSinalSaida, criaGraficoBarra(dados.dadosxHarmonica, dados.dadosyFaseSinalSaida));
    }
}


function criaGraficoLinhaCanal(dadosX, dadosYGanhaAmplitude, dadosYDeslocamentoFase) {
    var config = {
        type: 'line',
        data: {
            labels: dadosX,
            datasets: [
                {
                    label: "Ganho Amplitude Canal",
                    lineTension: 0,
                    backgroundColor: "rgba(78, 115, 223, 0.05)",
                    borderColor: "rgba(78, 115, 223, 1)", // Cor da primeira linha
                    pointRadius: 1,
                    pointBackgroundColor: "rgba(78, 115, 223, 1)",
                    pointBorderColor: "rgba(78, 115, 223, 1)",
                    pointHoverRadius: 1,
                    pointHoverBackgroundColor: "rgba(78, 115, 223, 1)",
                    pointHoverBorderColor: "rgba(78, 115, 223, 1)",
                    pointHitRadius: 10,
                    pointBorderWidth: 2,
                    data: dadosYGanhaAmplitude,
                },
                {
                    label: "Deslocamento Fase Canal",
                    lineTension: 0,
                    backgroundColor: "rgba(59, 184, 123, 0.05)",
                    borderColor: "rgba(59, 184, 123, 1)", // Cor da segunda linha
                    pointRadius: 1,
                    pointBackgroundColor: "rgba(59, 184, 123, 1)",
                    pointBorderColor: "rgba(59, 184, 123, 1)",
                    pointHoverRadius: 1,
                    pointHoverBackgroundColor: "rgba(59, 184, 123, 1)",
                    pointHoverBorderColor: "rgba(59, 184, 123, 1)",
                    pointHitRadius: 10,
                    pointBorderWidth: 2,
                    data: dadosYDeslocamentoFase,
                }
            ],
        },
        options: {
            responsive: true,
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
                  ticks: {
                    maxTicksLimit: 5,
                    padding: 10,
                    callback: function(value, index, values) {
                      return 'Frequência: ' + value;
                    }
                  },
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
            }
        }
    };

    return config;
}

function criaGraficoLinhaSinais(dadosX, dadosYSinalEntrada, dadosYSinalSaida) {
    // Formata a lista dadosX para ter no máximo 2 casas decimais
    var dadosXFormatados = formataListaParaCasasDecimais(dadosX, 2);

    var config = {
        type: 'line',
        data: {
            labels: dadosXFormatados,
            datasets: [
                {
                    label: "Sinal de Entrada",
                    lineTension: 0,
                    backgroundColor: "rgba(78, 115, 223, 0.05)",
                    borderColor: "rgba(78, 115, 223, 1)", // Cor da primeira linha
                    pointRadius: 1,
                    pointBackgroundColor: "rgba(78, 115, 223, 1)",
                    pointBorderColor: "rgba(78, 115, 223, 1)",
                    pointHoverRadius: 1,
                    pointHoverBackgroundColor: "rgba(78, 115, 223, 1)",
                    pointHoverBorderColor: "rgba(78, 115, 223, 1)",
                    pointHitRadius: 10,
                    pointBorderWidth: 2,
                    data: dadosYSinalEntrada,
                },
                {
                    label: "Sinal de Saída",
                    lineTension: 0,
                    backgroundColor: "rgba(59, 184, 123, 0.05)",
                    borderColor: "rgba(59, 184, 123, 1)", // Cor da segunda linha
                    pointRadius: 1,
                    pointBackgroundColor: "rgba(59, 184, 123, 1)",
                    pointBorderColor: "rgba(59, 184, 123, 1)",
                    pointHoverRadius: 1,
                    pointHoverBackgroundColor: "rgba(59, 184, 123, 1)",
                    pointHoverBorderColor: "rgba(59, 184, 123, 1)",
                    pointHitRadius: 10,
                    pointBorderWidth: 2,
                    data: dadosYSinalSaida,
                }
            ],
        },
        options: {
            responsive: true,
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
                  ticks: {
                    maxTicksLimit: 5,
                    padding: 10,
                    callback: function(value, index, values) {
                      return 'Frequência: ' + value;
                    }
                  },
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
            }
        }
    };

    return config;
}


function criaGraficoBarra(dadosX, dadosY){
    var config = {
      type: 'bar',
      data: {
        labels: dadosX,
        datasets: [{
          label: "Revenue",
          backgroundColor: "#4e73df",
          hoverBackgroundColor: "#2e59d9",
          borderColor: "#4e73df",
          data: dadosY,
        }],
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
            ticks: {
            /*
              min: -1,
              max: 1,
              */
              maxTicksLimit: 5,
              padding: 10,
              callback: function(value, index, values) {
                return 'Frequência: ' + value;
              }
            },
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
              var datasetLabel = 'Harmônica' + chart.datasets[tooltipItem.datasetIndex].label || '';
              return datasetLabel + tooltipItem.yLabel;
            }
          }
        },
      }
    };

    return config;
}

function formataListaParaCasasDecimais(lista, casasDecimais) {
    return lista.map(function(valor) {
        return parseFloat(valor.toFixed(casasDecimais));
    });
}