# OnePlus Weather Widget [![Build Status](https://github.com/a2krocks/OnePlus_Weather_Widget/actions/workflows/android.yml/badge.svg)](https://github.com/a2krocks/OnePlus_Weather_Widget/actions/workflows/android.yml)

An attempt to clone OnePlus's Classic Weather Widget with android 12 theme support. It uses OpenWeatherMap's API to get weather data.

<img src="/screenshots/a13.png" style="border-radius: 25px;" >

## Instructions:

Before installing read these instructions:

* Allow location access at all times and add the Weather Widget to your home screen, else the weather will not update and "Loading..." will continue
  to appear.

<img src="/screenshots/error.png" style="border-radius: 25px;" >

* After allowing location access, it will take some time to load when booted for the first time. Be patient.


**Still Not Working?**
1. First clear data and uninstall the app
2. Install the app
3. Give location permission for all the time, Going into the settings.
4. Open your google maps, then tap on locate button ( I don't know why but it helps gps)
5. Then add Widget to your home screen.

It should work now.

**If any issue prevails, Please send me the logs.**

## Build:

In order to test and build the widget on your own, you will require an API key from [OpenWeatherMap](https://openweathermap.org/api).

After obtaining API key,  After obtaining API key, if building locally, put your API key in environment variable as:

```
    API_KEY : "YOUR_API_KEY"
```

If you are using github actions, Go to:

Fork's settings > Secrets > Actions > Click on New repository secert > under Name put `API_KEY` > under Secret `YOUR_API_KEY` > then click Add Secret

You are ready to use github actions to compile your android app.
