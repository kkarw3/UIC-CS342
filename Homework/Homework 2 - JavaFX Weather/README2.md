### Visual Encodings
1. **Bar Chart:** Compares the current temperatures of Chicago, Los Angeles, and New York side-by-side. The Y-axis represents the temperature in degrees Fahrenheit, and the X-axis categorizes the cities.
2. **Progress Bar (Temperature Gauge):** Maps the current temperature to a visual gauge next to each city's numeric label. 
   - **Scale:** The bar maps 0°F to 100°F to a progress value of `0.0` to `1.0`. 
   - **Clamping:** Values below 0°F are clamped to `0.0` (empty), and values above 100°F are clamped to `1.0` (full) to prevent rendering errors.