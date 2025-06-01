package teo.springjwt.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // Marks this as a Spring configuration class
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**") // Allow CORS for all paths
            .allowedOrigins("http://localhost:5173", "https://teoyou881.github.io/hc_h_m/")
            // During development, use 5173 (Vite), 3000 (CRA), etc.
            // For actual deployment, add your frontend's production domain.
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed HTTP methods
            .allowedHeaders("*") // Allow all headers
            .allowCredentials(true) // Allow credentials (cookies, auth headers, etc.)
            .maxAge(3600); // Cache preflight request results for 3600 seconds
  }
}