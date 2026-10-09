package weather.model;

/*
Stores the current weather data for a given location as a record

   The record includes the location's
     - temperature
     - wind speed
     - relative humidity
*/
public record WeatherData(double currTemperature, double windSpeed, int relativeHumidity) {}