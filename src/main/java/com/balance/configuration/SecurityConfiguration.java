package com.balance.configuration;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private DataSource dataSource;

	@Value("${spring.queries.users-query}")
	private String usersQuery;

	@Value("${spring.queries.roles-query}")
	private String rolesQuery;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.
				jdbcAuthentication()
				.usersByUsernameQuery(usersQuery)
				.authoritiesByUsernameQuery(rolesQuery)
				.dataSource(dataSource)
				.passwordEncoder(bCryptPasswordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.
				authorizeRequests()


                .antMatchers("/").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/forgotpassword").permitAll()
                .antMatchers("/bands").permitAll()
                .antMatchers("/band").permitAll()
				.antMatchers("/locations").permitAll()
				.antMatchers("/steps").permitAll()
				.antMatchers("/getLocations/**").permitAll()
                .antMatchers("/getSteps/**").permitAll()
                .antMatchers("/getDistance/**").permitAll()
                .antMatchers("/calories").permitAll()
                .antMatchers("/getCalories/**").permitAll()
				.antMatchers("/Pulse").permitAll()
				.antMatchers("/getPulse/**").permitAll()
                .antMatchers("/registration").permitAll()
                .antMatchers("/send-mail").permitAll()
                .antMatchers("/forgotpasswordconfirm/**").permitAll()
                .antMatchers("/changepassword").permitAll()
                .antMatchers("/changepasswordyes").permitAll()
                .antMatchers("/admin/**").hasAuthority("ADMIN")			//.anyRequest().authenticated()
                .antMatchers("/user/**").hasAuthority("LIMITED")		//.anyRequest().authenticated()
				.anyRequest().authenticated()  //Este comando hace que para las siguientes lines de codigo el usuario lo debe tomar en cuenta como registrado
				.and().csrf().disable()									// PREVIENE ATAQUES CSRF ???
				.formLogin()
				.loginPage("/login")
				.loginProcessingUrl("/login")		// <-------- SE CREA LOGIN / POST
				.failureUrl("/login?error=true")
				.defaultSuccessUrl("/default")
				.usernameParameter("email")
				.passwordParameter("password")
				.and().logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // <---- Se crea LogOut / GET
				.logoutSuccessUrl("/").and().exceptionHandling()
				.accessDeniedPage("/access-denied");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
	} //Evita que el usuario entre a estas direcciones
}