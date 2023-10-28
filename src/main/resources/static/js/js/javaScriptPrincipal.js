Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';

let chartGraficoCanal, chartSinalEntrada, chartEspectroSinalEntrada,
chartFaseSinalEntrada, chartSinalSaida, chartEspectroSinalSaida, chartFaseSinalSaida, chartSinalEntradaTeste;

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
    var ctxSinalEntrada = document.getElementById("sinalEntrada");
    var ctxEspectroSinalEntrada = document.getElementById("espectroSinalEntrada");
    var ctxFaseSinalEntrada = document.getElementById("faseSinalEntrada");
    var ctxSinalSaida = document.getElementById("sinalSaida");
    var ctxEspectroSinalSaida = document.getElementById("espectroSinalSaida");
    var ctxFaseSinalSaida = document.getElementById("faseSinalSaida");

    let retornoGraficoCanal = criaGraficoLinha(dados.dados3, dados.dados7, dados.dados8);
    handleCriacaoGrafico(chartGraficoCanal, ctxGraficoCanal, retornoGraficoCanal);

    let retornoGraficoSinalEntrada = criaGraficoSinalEntrada(dados.sinalEntradaResponse);
    handleCriacaoGrafico(chartSinalEntrada, ctxSinalEntrada, retornoGraficoSinalEntrada);

    let retornoGraficoEspectroSinalEntrada = criaGraficoBarra(dados.dados1, dados.dados5);
    handleCriacaoGrafico(chartEspectroSinalEntrada, ctxEspectroSinalEntrada, retornoGraficoEspectroSinalEntrada);

    let retornoGraficoFaseSinalEntrada = criaGraficoBarraFase(dados.dados1, dados.faseSinalEntradaResponse);
    handleCriacaoGrafico(chartFaseSinalEntrada, ctxFaseSinalEntrada, retornoGraficoFaseSinalEntrada);

    let retornoGraficoSinalSaida = criaGraficoLinha(dados.dados2, dados.dados9);
    handleCriacaoGrafico(chartSinalSaida, ctxSinalSaida, retornoGraficoSinalSaida);

    let retornoEspectroSinalSaida = criaGraficoBarra(dados.dados1, dados.dados10);
    handleCriacaoGrafico(chartEspectroSinalSaida, ctxEspectroSinalSaida, retornoEspectroSinalSaida);

    let retornoFaseSinalSaida = criaGraficoBarra(dados.dados1, dados.dados11);
    handleCriacaoGrafico(chartFaseSinalSaida, ctxFaseSinalSaida, retornoFaseSinalSaida);
}

function handleCriacaoGrafico(chartGrafico, ctxGrafico, retornoCriacaoGrafico){
    if (chartGrafico) {
        chartGrafico.destroy();
    }
    chartGrafico = new Chart(ctxGrafico, retornoCriacaoGrafico);
}

function criaGraficoSinalEntrada(dados){
    let datasets = [];
    let dadosXLength = dados.dadosXCiclos.length;
    for(let i =0; i < dadosXLength; i++){
        let graph = [];
        dados.dadosXCiclos[i].forEach((dadoXCiclo, index) => {
            graph.push({x: dadoXCiclo, y: dados.dadosYCiclos[i][index]});
        });
        data = {
             label: "Frequência",
             lineTension: 0,
             backgroundColor: "rgba(78, 115, 223, 0.05)",
             borderColor: "rgba(78, 115, 223, 1)",
             pointRadius: 1,
             pointBackgroundColor: "rgba(78, 115, 223, 1)",
             pointBorderColor: "rgba(78, 115, 223, 1)",
             pointHoverRadius: 1,
             pointHoverBackgroundColor: "rgba(78, 115, 223, 1)",
             pointHoverBorderColor: "rgba(78, 115, 223, 1)",
             pointHitRadius: 10,
             pointBorderWidth: 2,
             data: graph,
        }
        datasets.push(data);
    }
    return criaGraficoGeral('line', dados.dadosXGrafico, datasets);
}

function criaGraficoGeral(tipo, dadosEixoX, datasets, maxY = 1, minY = -1, stepSize = 0){
    var config = {
            type: tipo,
            data: {
                labels: dadosEixoX,
                datasets: datasets,
            },
            options: criaGraficoOptions(tipo, maxY, minY, stepSize)
    };
    return config;
}

function getTickConfiguration(tipo, minY, maxY) {
    const isLine = tipo === 'line';
    return {
        ticks: {
            maxTicksLimit: isLine ? 5 : 6,
            padding: 10,
            min: isLine ? undefined : minY,
            max: isLine ? undefined : maxY,
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
    };
}

function handleYAxes(tipo, minY, maxY) {
    return [getTickConfiguration(tipo, minY, maxY)];
}

function handleXAxes(tipo) {
    return [{
        time: {
            unit: tipo === 'line' ? 'date' : 'month'
        },
        gridLines: {
            display: false,
            drawBorder: false
        },
        ticks: {
            maxTicksLimit: tipo === 'line' ? 7 : 6
        }
    }];
}

function criaGraficoOptions(tipo, minY, maxY){
        return {
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
                xAxes: handleXAxes(tipo),
                yAxes: handleYAxes(tipo, minY, maxY),
            },
            legend: {
                display: false
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
                    label: function(tooltipItem, chart) {
                        var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
                        return datasetLabel + ': Frequência: ' + tooltipItem.yLabel;
                    }
                }
            }
        };
}

function criaGraficoLinha(dadosX, dadosYGanhaAmplitude, dadosYDeslocamentoFase) {
    let datasets = [
        {
            label: "Ganho Amplitude Canal",
            backgroundColor: "rgba(78, 115, 223, 0.05)",
            borderColor: "rgba(78, 115, 223, 1)", // Cor da primeira linha
            pointBackgroundColor: "rgba(78, 115, 223, 1)",
            pointBorderColor: "rgba(78, 115, 223, 1)",
            pointHoverBackgroundColor: "rgba(78, 115, 223, 1)",
            pointHoverBorderColor: "rgba(78, 115, 223, 1)",
            data: dadosYGanhaAmplitude,
        },
        {
            label: "Deslocamento Fase Canal",
            backgroundColor: "rgba(59, 184, 123, 0.05)",
            borderColor: "rgba(59, 184, 123, 1)", // Cor da segunda linha
            pointBackgroundColor: "rgba(59, 184, 123, 1)",
            pointBorderColor: "rgba(59, 184, 123, 1)",
            pointHoverBackgroundColor: "rgba(59, 184, 123, 1)",
            pointHoverBorderColor: "rgba(59, 184, 123, 1)",
            data: dadosYDeslocamentoFase,
        }
    ];
    return criaGraficoGeral('line', dadosX, datasets)
}


function criaGraficoBarra(dadosX, dadosY){
    let datasets = [{
                     label: "Revenue",
                     backgroundColor: "#4e73df",
                     hoverBackgroundColor: "#2e59d9",
                     borderColor: "#4e73df",
                     data: dadosY,
                   }];
    return criaGraficoGeral('bar', dadosX, datasets);
}

function criaGraficoBarraFase(dadosX, dadosY){
  let datasets = [{
    label: "Revenue",
    backgroundColor: "#4e73df",
    hoverBackgroundColor: "#2e59d9",
    borderColor: "#4e73df",
    data: dadosY,
  }];
  return criaGraficoGeral('bar', dadosX, datasets, 180, -180)

}