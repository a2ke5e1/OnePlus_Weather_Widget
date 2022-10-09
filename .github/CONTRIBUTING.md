# Contributing Guide to OnePlus Weather Widget Clone

## Getting Started 
Go to Issue Page, Check out issues with label with "help wanted".

### Note
If you are going to use Github actions, add your [OpenWeatherMap API](https://openweathermap.org/api) Key to your fork's secrets settings. 
Read Testing and Building Section.

**Few Rules before creating a pull request**
* Preferred Language is **Kotlin** 
* Before, creating a pull request make sure your app compiles.
* Insert a screenshot of changes are made related to UI.
eg; If you create a Dialog Box for permission in [issue #2](https://github.com/a2krocks/OnePlus_Weather_Widget/issues/2)

**Adding New Features**
* Before creating a pull request for new features, create a issue with a enhancement

## Testing and Building
In order to test and build of your own you will be API key from [OpenWeatherMap](https://openweathermap.org/api).
<br>
After obtaining API key, if you are building locally put your api key in environment variable
```
    API_KEY : "YOUR_API_KEY"
```
If you are using github actions, Go to your fork's 

Settings > Secrets > Actions > Click on New repository secert > under Name put `API_KEY` > under Secret `YOUR_API_KEY` > then click Add Secret 

You are ready to use github actions to compile your android app.