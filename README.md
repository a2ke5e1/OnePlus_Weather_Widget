# OnePlus Weather Widget

It is a attempt to clone OnePlus's Classic Weather Widget with android 12 theme support. It uses
OpenWeatherMap's API to get weather data.

![](screenshots/a12.jpg)

## Instructions:
Before installing read these instructions:
<ul>
    <li>Give location permission for all the time then add widget to your home screen otherwise
it will not update and will say it's loading. </li>

<li>After giving permission, it will time to load for first time. Be patient.</li>
</ul>

**If any issue, Please give me logs.**

## Build:
In order to test and build of your own you will be API key from [OpenWeatherMap](https://openweathermap.org/api).
<br>
After obtaining API key, if you are building locally put your api key in environment variable
```
    API_KEY : "YOUR_API_KEY"
```
If you are using github actions, Go to your fork's

Settings > Secrets > Actions > Click on New repository secert > under Name put `API_KEY` > under Secret `YOUR_API_KEY` > then click Add Secret

You are ready to use github actions to compile your android app.
