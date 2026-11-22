
Weather Service — Spring Boot

A minimal Spring Boot application that exposes a REST endpoint wrapping the free Open-Meteo public weather API. No API key required.

Features

GET /api/weather — returns current weather for any latitude/longitude

No API key or registration needed (Open-Meteo is free for non-commercial use)

Built-in request validation for latitude/longitude bounds

Uses Spring's modern RestClient (Spring 6.1+)

Java 21, Spring Boot 4.2.0

Tech Stack
Layer	Technology
Framework	Spring Boot 4.2.0
Language	Java 21

HTTP Client	Spring RestClient
Validation	Jakarta Bean Validation
External API	Open-Meteo (free, no key)
Build	Maven


Project Structure
text
weather-service/
├── pom.xml
└── src/
    └── main/
        ├── java/com/afro/weather/
        │   ├── WeatherServiceApplication.java
        │   ├── controller/WeatherController.java
        │   ├── service/WeatherService.java
        │   └── dto/WeatherResponse.java
        └── resources/
            └── application.properties

Getting Started
Prerequisites
Java 17 or higher

Maven 3.8+

Verify your setup:

bash
java -version
mvn -version
Build
bash
mvn clean package
Run
Option 1 — Maven:

bash
mvn spring-boot:run
Option 2 — JAR:

bash
java -jar target/weather-service-0.0.1.jar
The app starts on http://localhost:9090.

API Reference
GET /api/weather
Fetch current weather for a location.

Query Parameters
Parameter	Type	Required	Range	Description
latitude	double	✅	-90.0 to 90.0	Latitude in decimal degrees
longitude	double	✅	-180.0 to 180.0	Longitude in decimal degrees
Example Request
bash
curl "http://localhost:9090/api/weather?latitude=51.5074&longitude=-0.1278"
Example Response
json
{
  "current": {
    "time": "2025-01-15T14:00",
    "temperature_2m": 8.4,
    "relative_humidity_2m": 76,
    "wind_speed_10m": 14.2
  }
}
Response Fields

Field	Type	Description
current.time	string	Observation timestamp (ISO 8601)
current.temperature_2m	number	Air temperature at 2 m (°C)
current.relative_humidity_2m	integer	Relative humidity at 2 m (%)
current.wind_speed_10m	number	Wind speed at 10 m (km/h)

Error Cases
Scenario	HTTP Status	Cause
Missing latitude or longitude	400 Bad Request	MissingServletRequestParameterException
Out-of-range coordinates	400 Bad Request	Bean Validation constraint violation
Non-numeric coordinate	400 Bad Request	Type mismatch
Sample Locations
City	Latitude	Longitude
London	51.5074	-0.1278
New York	40.7128	-74.0060
Tokyo	35.6762	139.6503
Sydney	-33.8688	151.2093
São Paulo	-23.5505	-46.6333
How It Works
WeatherController receives the request and validates the coordinate bounds.

WeatherService calls Open-Meteo via RestClient:

text
GET https://api.open-meteo.com/v1/forecast
    ?latitude={lat}
    &longitude={lon}
    &current=temperature_2m,relative_humidity_2m,wind_speed_10m
The JSON response is deserialized into WeatherResponse (unknown fields ignored) and returned to the client.

Configuration
src/main/resources/application.properties:

properties
server.port=9090
spring.application.name=afro-weather-service
To change the port:

properties
server.port=9090
Design Notes
No secrets: Open-Meteo requires no API key, so there's no config or secrets management in this demo.

Minimal DTO: Only the required fields are mapped; @JsonIgnoreProperties(ignoreUnknown = true) keeps the DTO resilient to upstream schema changes.

Validation at the edge: Coordinate bounds are enforced at the controller boundary using Jakarta Bean Validation.

RestClient over RestTemplate: RestClient (Spring 6.1+) provides a fluent, modern API and is the recommended replacement for RestTemplate.

Testing
Once the app is running, verify with:

bash
# Valid request
curl -i "http://localhost:9090/api/weather?latitude=51.5074&longitude=-0.1278"

# Missing parameter → expect 400
curl -i "http://localhost:9090/api/weather?latitude=51.5074"

# Out-of-range latitude → expect 400
curl -i "http://localhost:9090/api/weather?latitude=999&longitude=0"
Troubleshooting
Symptom	Likely Cause	Fix
Connection refused on :9090	App not running	Start with mvn spring-boot:run
400 Bad Request on valid coords	Coord format issue	Ensure decimal point, e.g. 51.5 not 51,5
Upstream 5xx	Open-Meteo temporarily unavailable	Retry; check open-meteo.com status
401/403 from upstream	You switched to a keyed API	Revert to Open-Meteo or add key handling
License
This project is provided as-is for demonstration and learning purposes. Open-Meteo's terms apply to the underlying data — see open-meteo.com/en/terms.