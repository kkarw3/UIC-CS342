package weather.provider;

// Throws when weather data is unable to be retrieved or parsed successfully
public class WeatherProviderException extends RuntimeException
{
    public WeatherProviderException(String message)
    {
        super(message);
    }
    public WeatherProviderException(String message, Throwable cause)
    {
        super(message, cause);
    }
}