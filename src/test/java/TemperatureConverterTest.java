import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TemperatureConverterTest {

    private static final double TOLERANCE = 0.001;

    private final TemperatureConverter converter = new TemperatureConverter();

    @ParameterizedTest(name = "{0}°F = {1}°C")
    @CsvSource({
            "32, 0",
            "212, 100",
            "-40, -40",
            "0, -17.7778",
            "98.6, 37",
            "451, 232.7778"
    })
    @DisplayName("Fahrenheit is converted to Celsius correctly")
    void testFahrenheitToCelsius(double fahrenheit, double expectedCelsius) {
        assertEquals(expectedCelsius, converter.fahrenheitToCelsius(fahrenheit), TOLERANCE);
    }

    @ParameterizedTest(name = "{0}°C = {1}°F")
    @CsvSource({
            "0, 32",
            "100, 212",
            "-40, -40",
            "37, 98.6",
            "-273.15, -459.67",
            "50, 122"
    })
    @DisplayName("Celsius is converted to Fahrenheit correctly")
    void testCelsiusToFahrenheit(double celsius, double expectedFahrenheit) {
        assertEquals(expectedFahrenheit, converter.celsiusToFahrenheit(celsius), TOLERANCE);
    }

    @Test
    @DisplayName("Temperatures outside the inclusive -40..50 range are extreme")
    void testIsExtremeTemperature() {
        assertFalse(converter.isExtremeTemperature(-40));
        assertFalse(converter.isExtremeTemperature(50));

        assertTrue(converter.isExtremeTemperature(-40.1));
        assertTrue(converter.isExtremeTemperature(50.1));

        assertTrue(converter.isExtremeTemperature(-41));
        assertTrue(converter.isExtremeTemperature(51));

        assertFalse(converter.isExtremeTemperature(-39.9));
        assertFalse(converter.isExtremeTemperature(49.9));
        assertFalse(converter.isExtremeTemperature(20));
        assertFalse(converter.isExtremeTemperature(0));
    }

    @Test
    @DisplayName("Kelvin is converted to Celsius correctly")
    void testKelvinToCelsius() {
        assertEquals(26.85, converter.kelvinToCelsius(300), TOLERANCE);
        assertEquals(0.0, converter.kelvinToCelsius(273.15), TOLERANCE);
        assertEquals(-273.15, converter.kelvinToCelsius(0), TOLERANCE);
    }

    @Test
    @DisplayName("Celsius and Fahrenheit conversions round-trip")
    void testConversionsRoundTrip() {
        double[] celsiusValues = {-100, -40, 0, 21.5, 100, 250};
        for (double celsius : celsiusValues) {
            double fahrenheit = converter.celsiusToFahrenheit(celsius);
            assertEquals(celsius, converter.fahrenheitToCelsius(fahrenheit), TOLERANCE);
        }
    }
}
