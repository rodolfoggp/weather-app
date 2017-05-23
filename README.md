# Weather App
Aplicativo Android feito com o objetivo de buscar informações de clima pela API do openweather.  

## Informações de Uso do Aplicativo
O aplicativo requisita da API openweather informações de clima (16 dias) para a cidade escolhida na tela de preferências.
Atualmente o aplicativo só mostra a temperatura(em Kelvin) e condição do clima atual.  
Ao clicar nas opções no canto superior direito da Activity principal, e em seguida em Settings, a tela de preferências é aberta, onde o usuário poderá escolher a cidade(atualmente a lista só possui três cidades) e a unidade da temperatura desejada(ainda não implementado).  
O aplicativo funciona em modo offline, persistindo os últimos dados lidos de cada cidade e mostrando-os na tela principal quando não for possível buscá-los na internet.  
Para conseguir usar o aplicativo, deve-se preencher a constante "API_KEY", em WeatherHttpClient.java com uma APPID válida para a API do openweather. Futuramente, o aplicativo conseguirá ler uma APPID de um arquivo.  
  
## Informações de Desenvolvimento
Este projeto foi feito usando linguagem nativa JAVA e a IDE Android Studio.  
  
A modelagem dos dados foi feita tendo em vista a resposta da API da openweather:  
-A classe CityWeather representa o clima de uma cidade e guarda um identificador, a localização da cidade e uma lista de informações de clima(Weather) para esta cidade;  
-A classe Weather representa uma informação de clima com data, condição do clima e temperatura.  
  
A persistência de dados foi feita através de banco de dados, usando SQLite.
Há uma tabela "cities" para se guardar referências para as cidades já buscadas, e uma tabela "weathers" onde se guardam as informações de clima para todas as cidades já buscadas.

O aplicativo guarda as configurações feitas na tela Settings por meio de SharedPreferences.  

A interface ainda não mostra todas as informações pedidas. Esta será implementada totalmente na próxima versão.  

## Autor

* **Rodolfo Gusson** - (https://github.com/rodolfoggp)
