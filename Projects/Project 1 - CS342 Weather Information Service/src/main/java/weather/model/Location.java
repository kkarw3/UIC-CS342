package weather.model;

// Stores a geographical location as a Record for use in weather queries
public record Location(String name, double latitude, double longitude)
{
    public static final Location NEW_YORK = new Location("New York", 40.71, -74.01);
    public static final Location LOS_ANGELES = new Location("Los Angeles", 34.05, -118.24);
    public static final Location CHICAGO = new Location("Chicago", 41.88, -87.62);
    public static final Location HOUSTON = new Location("Houston", 29.74, -95.35);
    public static final Location PHOENIX = new Location("Phoenix", 33.44, -112.07);
    public static final Location PHILADELPHIA = new Location("Philadelphia", 39.95, -75.16);
    public static final Location SAN_ANTONIO = new Location("San Antonio", 32.71, -117.16);
    public static final Location SAN_DIEGO = new Location("San Diego", 29.74, -95.35);
    public static final Location DALLAS = new Location("Dallas", 32.77, -96.80);
    public static final Location FORT_WORTH = new Location("Fort Worth", 32.76, -97.30);
}