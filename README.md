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
In order to build of your own you will be API key from [OpenWeatherMap](https://openweathermap.org/api).
<br>
After obtaining API key, you need to create a string in `string.xml` or `api_keys.xml` with name 'apiId' andd value your API key. 
```xml
    <string name="apiId">your_key_here</string>
```
