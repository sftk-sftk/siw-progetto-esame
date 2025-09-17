package it.uniroma3.siw.authentication;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

//import it.uniroma3.siw.authentication.OAuth2AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class AuthConfiguration { // Spring Authentication configuration class

	@Autowired
	private DataSource dataSource;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
				.authoritiesByUsernameQuery("SELECT username, role FROM credentials WHERE username=?")
				.usersByUsernameQuery("SELECT username, password, 1 as enabled FROM credentials WHERE username=?");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
/*
	@Bean
	public AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
		return new OAuth2AuthenticationSuccessHandler(); // Register your custom success handler
	}
*/
	// @SuppressWarnings({ "removal", "deprecation" })
	@Bean
	protected SecurityFilterChain configure(final HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf(csrf -> csrf.disable()) // Disabilita CSRF se necessario, o lo configura come richiesto
				.cors(cors -> cors.disable()) // Disabilita CORS se necessario, o lo configura come richiesto
				// AUTORIZZAZIONE: here we define who can access what
				.authorizeHttpRequests(authorize -> authorize
						/* chiunque (autenticato o no) può accedere alle pagine index, login,
						register ai css e alle immagini */
						.requestMatchers(HttpMethod.GET, "/", "/oauth2/**", "/index", "/register", "/emailErrorRegister",
								"/emailErrorAdminRegister","/adminRegister", "/css/**", "/js/**", "/static/**", "/images/**",
								"favicon.ico", "/error")
						.permitAll()
						/* chiunque (autenticato o no) può mandare richieste POST al punto di
						accesso per login e register */
						.requestMatchers(HttpMethod.POST, "/register", "/adminRegister",
								"/login", "/emailErrorRegister", "/emailErrorAdminRegister")
						.permitAll()
						/* solo gli utenti autenticati con ruolo HOST e INTERMEDIATE_HOST possono accedere a
						risorse con path */
						.requestMatchers(HttpMethod.GET, "/user/**").hasAnyAuthority("USER")
						.requestMatchers(HttpMethod.POST, "/user/**").hasAnyAuthority("USER")
						.requestMatchers(HttpMethod.GET, "/admin/**").hasAnyAuthority("ADMIN")
						.requestMatchers(HttpMethod.POST, "/admin/**").hasAnyAuthority("ADMIN")						
						// tutti gli utenti autenticati possono accere alle pag
						.anyRequest().authenticated())

				// LOGIN: here it's defined how the authentication gets handled
				.formLogin(formLogin -> formLogin
						// the login page is /login
						.loginPage("/login").permitAll()
						// se il login ha successo, si viene rediretti al path /default
						.defaultSuccessUrl("/success", true).failureUrl("/login?error=true"))

				/* OAUTH: here it's defined the OAuth2.0 login
				.oauth2Login(oauth2Login -> oauth2Login.loginPage("/login")
						.successHandler(oAuth2AuthenticationSuccessHandler())) // this is a constructor of a specific
																				// class (the other in this same
					*/															// package)

				// LOGOUT: here we define the logout
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/").invalidateHttpSession(true)
						.deleteCookies("JSESSIONID").logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
						.clearAuthentication(true).permitAll());

		return httpSecurity.build();
	}
}