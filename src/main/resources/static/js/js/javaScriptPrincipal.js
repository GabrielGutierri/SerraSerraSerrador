Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';

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

    			if (dados.erro != undefined) {
    				alert(dados.msg);
    			}
    			else {
    				criaGraficos(dados);
    			}
    		},
    	});

}


let chartGanhoAmplitudeCanal, chartContribuicaoFaseCanal, chartSinalEntrada, chartEspectroSinalEntrada,
chartFaseSinalEntrada, chartSinalSaida, chartEspectroSinalSaida, chartFaseSinalSaida;

function criaGraficos(dados){

    var ctxGanhoAmplitudeCanal = document.getElementById("ganhoAmplitudeCanal");
    var ctxContribuicaoFaseCanal = document.getElementById("contribuicaoFaseCanal");
    var ctxSinalEntrada = document.getElementById("sinalEntrada");
    var ctxEspectroSinalEntrada = document.getElementById("espectroSinalEntrada");
    var ctxFaseSinalEntrada = document.getElementById("faseSinalEntrada");
    var ctxSinalSaida = document.getElementById("sinalSaida");
    var ctxEspectroSinalSaida = document.getElementById("espectroSinalSaida");
    var ctxFaseSinalSaida = document.getElementById("faseSinalSaida");

    if (chartGanhoAmplitudeCanal) {
        chartGanhoAmplitudeCanal.destroy();
        chartGanhoAmplitudeCanal = new Chart(ctxGanhoAmplitudeCanal, criaGrafico(dados.dados1, dados.dados5));
    } else {
        chartGanhoAmplitudeCanal = new Chart(ctxGanhoAmplitudeCanal, criaGrafico(dados.dados1, dados.dados5));
    }

    if(chartContribuicaoFaseCanal){
        chartContribuicaoFaseCanal.destroy();
        chartContribuicaoFaseCanal = new Chart(ctxContribuicaoFaseCanal, criaGrafico(dados.dados1, dados.dados6));
    } else {
        chartContribuicaoFaseCanal = new Chart(ctxContribuicaoFaseCanal, criaGrafico(dados.dados1, dados.dados6));
    }

    if(chartSinalEntrada){
        chartSinalEntrada.destroy();
        chartSinalEntrada = new Chart(ctxSinalEntrada, criaGrafico(dados.dados1, dados.dados2));
    } else {
        chartSinalEntrada = new Chart(ctxSinalEntrada, criaGrafico(dados.dados1, dados.dados2));
    }

    if(chartEspectroSinalEntrada){
        chartEspectroSinalEntrada.destroy();
        chartEspectroSinalEntrada = new Chart(ctxEspectroSinalEntrada, criaGrafico(dados.dados1, dados.dados3));
    } else {
        chartEspectroSinalEntrada = new Chart(ctxEspectroSinalEntrada, criaGrafico(dados.dados1, dados.dados3));
    }

    if(chartFaseSinalEntrada){
        chartFaseSinalEntrada.destroy();
        chartFaseSinalEntrada = new Chart(ctxFaseSinalEntrada, criaGrafico(dados.dados1, dados.dados4));
    } else {
        chartFaseSinalEntrada = new Chart(ctxFaseSinalEntrada, criaGrafico(dados.dados1, dados.dados4));
    }

    if(chartSinalSaida){
        chartSinalSaida.destroy();
        chartSinalSaida = new Chart(ctxSinalSaida, criaGrafico(dados.dados1, dados.dados7));
    } else {
        chartSinalSaida = new Chart(ctxSinalSaida, criaGrafico(dados.dados1, dados.dados7));
    }

    if(chartEspectroSinalSaida){
        chartEspectroSinalSaida.destroy();
        chartEspectroSinalSaida = new Chart(ctxEspectroSinalSaida, criaGrafico(dados.dados1, dados.dados8));
    } else {
        chartEspectroSinalSaida = new Chart(ctxEspectroSinalSaida, criaGrafico(dados.dados1, dados.dados8));
    }

    if(chartFaseSinalSaida){
        chartFaseSinalSaida.destroy();
        chartFaseSinalSaida = new Chart(ctxFaseSinalSaida, criaGrafico(dados.dados1, dados.dados9));
    } else {
        chartFaseSinalSaida = new Chart(ctxFaseSinalSaida, criaGrafico(dados.dados1, dados.dados9));
    }
}

function criaGrafico(dadosX, dadosY) {


    var config ={
      type: 'line',
      data: {
        labels: dadosX,
        datasets: [{
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
              unit: 'date'
            },
            gridLines: {
              display: false,
              drawBorder: false
            },
            ticks: {
              maxTicksLimit: 7
            }
          }],
          yAxes: [{
            ticks: {
              maxTicksLimit: 5,
              padding: 10,
              //PODE ESTAR ERRADO
              callback: function(value, index, values) {
                return 'Frquência: ' + value;
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
          //Pode estar errado
          callbacks: {
            label: function(tooltipItem, chart) {
              var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
              return datasetLabel + ': Frequência: ' + tooltipItem.yLabel;
            }
          }
        }
      }
    };

    return config;
}