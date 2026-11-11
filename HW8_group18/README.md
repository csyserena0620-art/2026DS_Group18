# HW8 Group 8 — Tavily Search

This Spring Boot application provides a web page that searches the web with the Tavily Search API and displays each result's title, URL, and content excerpt. The backend keeps the API key out of browser code.

## Requirements

- Java 21
- Maven (or the included Maven wrapper)
- A Tavily API key from <https://app.tavily.com>

## Configure and run

Set the key in the environment before starting the application. Do not commit the key to GitHub.

PowerShell:

```powershell
$env:TAVILY_ENABLED = "true"
$env:TAVILY_API_KEY = "tvly-your-key"
.\mvnw.cmd spring-boot:run
```

Then open <http://localhost:8080> and submit a search query. The backend sends a POST request to Tavily's `/search` endpoint with the query and a limit of 10 results. `GET /api/search?query=...` returns the query and a JSON `results` array. Each result includes `title`, `url`, and `content`.

When the feature is disabled or the key is missing, the API returns an empty results array. Never paste a real API key into source code, screenshots, or a public repository.
