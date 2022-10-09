# Contributing Guide to OnePlus Weather Widget Clone

## Getting Started 
Go to Issue Page, Check out issues with label with "help wanted".

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
After obtaining API key, you need to create a string in `string.xml` or `api_keys.xml` with name 'apiId' andd value your API key.
```xml
    <string name="apiId">your_key_here</string>
```