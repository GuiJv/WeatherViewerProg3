# 🌦️ WeatherViewer App --- Cliente Android de Previsão do Tempo

Aplicação desenvolvida como Trabalho Prático da disciplina, baseada no
**Capítulo 7 -- WeatherViewer App** do livro utilizado em aula.\
O aplicativo consome uma **API REST de previsão do tempo** fornecida
pelo professor, processa JSON, executa operações de rede em thread
separada e exibe uma lista customizada com informações climáticas.

------------------------------------------------------------------------

## Autor
Guilherme José Vinhas


## 📱 Objetivo do Projeto

Implementar um aplicativo Android que seja capaz de:

-   Realizar requisições HTTP para uma API REST remota.\
-   Consumir e interpretar dados JSON retornados pela API.\
-   Executar operações de rede fora da UI Thread (AsyncTask).\
-   Montar uma lista customizada com ícones e informações de previsão do
    tempo.\
-   Demonstrar compreensão de layouts, adapters, threads, networking e
    manipulação de dados.

------------------------------------------------------------------------

## 🚀 Tecnologias Utilizadas

-   **Java**
-   **Android SDK**
-   **AsyncTask** para requisições HTTP
-   **HttpURLConnection**
-   **JSON parsing (org.json)**
-   **ListView + Custom Adapter**
-   **GridLayout e Layouts customizados**
-   **API REST do professor (AWS Elastic Beanstalk)**

------------------------------------------------------------------------

## 🔗 API Utilizada

A previsão do tempo é obtida via endpoint:

    http://agent-weathermap-env-env.eba-6pzgqekp.us-east2.elasticbeanstalk.com/api/weather?city={CIDADE}&days=7&APPID=AgentWeather2024_a8f3b9c1d7e2f5g6h4i9j0k1l2m3n4o5p6

Retorno em JSON contendo: - Temperaturas mínimas e máximas\
- Umidade\
- Ícone meteorológico\
- Descrição\
- Timestamp

------------------------------------------------------------------------

## 📂 Estrutura Geral do Projeto

-   **MainActivity.java** --- configura UI, inicia requisição, gerencia
    ListView\
-   **Weather.java** --- modelo de dados\
-   **WeatherArrayAdapter.java** --- adapta os dados para a lista\
-   **list_item.xml** --- layout customizado de cada item da previsão\
-   **activity_main.xml** --- layout principal

------------------------------------------------------------------------

## ▶️ Como Executar o Projeto

1.  Clone o repositório ou extraia o ZIP.\
2.  Abra o projeto no **Android Studio**.\
3.  Certifique-se de que a `compileSdkVersion` está atualizada.\
4.  Execute o projeto em um emulador ou dispositivo físico.\
5.  Insira uma cidade no campo de busca e veja a previsão aparecer na
    lista.

------------------------------------------------------------------------

## ✔️ Funcionalidades Implementadas

-   Busca de dados climáticos por cidade\
-   Lista customizada com ícones, dia da semana, temperaturas e umidade\
-   Conversão automática de timestamps\
-   Tratamento de erros de rede e carregamento\

------------------------------------------------------------------------


------------------------------------------------------------------------

## 📘 Referências

Baseado no capítulo 7 --- *WeatherViewer App* do livro utilizado na
disciplina.

