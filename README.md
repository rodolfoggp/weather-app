# Weather App
Aplicativo Android feito com o objetivo de buscar informações de clima pela API do openweather.  

## Informações de Uso do Aplicativo
O aplicativo requisita da API Openweather informações de clima (16 dias) para a cidade escolhida na tela de preferências. 

Ao clicar nas opções no canto superior direito da Activity principal, e em seguida em Settings, a tela de preferências é aberta, onde o usuário poderá escolher a unidade da temperatura desejada e a cidade(podendo escolher a localização atual, buscada pela internet ou gps; ou ainda buscar uma localização fixa online).  

O aplicativo funciona em modo offline, persistindo os últimos dados lidos de cada cidade e mostrando-os na tela principal quando não for possível buscar dados mais recentes na internet.

Para conseguir usar o aplicativo, deve-se preencher a "API Key" com uma chave válida do OpenWeather, na tela Settings. 

Na tela principal, será possível ver o clima e temperatura atuais, assim como a previsão para toda a semana.
Ao clicar em um dos dias, o usuário é levado para uma tela com informações mais detalhadas sobre o clima daquele dia.
  
## Informações de Desenvolvimento
Este projeto foi feito usando linguagem nativa JAVA e a IDE Android Studio.  
  
A modelagem dos dados foi feita tendo em vista a resposta da API da OpenWeather:  
-A classe CityWeather representa o clima de uma cidade e guarda um identificador, a localização da cidade e uma lista de informações de clima(Weather) para esta cidade;  
-A classe Weather representa uma informação de clima com data, condição do clima e temperatura.  
  
A persistência de dados foi feita através de banco de dados, usando SQLite.
Há uma tabela "cities" para se guardar referências para as cidades já buscadas, e uma tabela "weathers" onde se guardam as informações de clima para todas as cidades já buscadas.

O aplicativo guarda as configurações feitas na tela Settings por meio de SharedPreferences.  

## Autor

* **Rodolfo Gusson** - (https://github.com/rodolfoggp)
