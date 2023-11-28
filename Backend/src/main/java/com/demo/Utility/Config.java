package com.demo.Utility;



import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class Config {

    @Value("${openai.api.key}")
    private String apikey;

    @Bean
    public RestTemplate template() {
        RestTemplate restTemplate= new RestTemplate();
        restTemplate.getInterceptors().add((request,body,execution)->{
            request.getHeaders().add("Authorization", "Bearer "+apikey);
            return execution.execute(request,body);
        });

        return restTemplate;
    }

	@Bean
	public SecurityFilterChain configuration(HttpSecurity http) throws Exception {

		http.sessionManagement(se -> se.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).cors(cors -> {
			cors.configurationSource(new org.springframework.web.cors.CorsConfigurationSource() {

				@Override
				public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
					CorsConfiguration configuration = new CorsConfiguration();
					configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
					configuration.setAllowedMethods(Collections.singletonList("*"));
					configuration.setAllowCredentials(true);
					configuration.setAllowedHeaders(Collections.singletonList("*"));
					configuration.setExposedHeaders(Arrays.asList("Authorization"));
					return configuration;
				}
			});
		}).authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.POST, "/students/registerStudent", "/tutors/registerTutor").permitAll()
				.requestMatchers("/swagger-ui*/**", "/v3/api-docs/**").permitAll()
				.requestMatchers("/chat/**").permitAll()
				.requestMatchers(HttpMethod.GET,"/chat/**").permitAll()
				.requestMatchers("/students/**").hasRole("STUDENT")
				.requestMatchers("/tutors/**").hasRole("TUTOR")

				.anyRequest().authenticated()).csrf(csrf -> csrf.disable())
				.addFilterAfter(new JwtTokenGeneratorFilter(), BasicAuthenticationFilter.class)
				.addFilterBefore(new JwtTokenValidatorFilter(), BasicAuthenticationFilter.class)
//				.formLogin(Customizer.withDefaults())
				.httpBasic(Customizer.withDefaults());

		return http.build();

	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}