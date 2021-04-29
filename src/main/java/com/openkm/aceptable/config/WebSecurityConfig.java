/**
 * OpenKM, Open Document Management System (http://www.openkm.com)
 * Copyright (c) 2006-2016  Paco Avila & Josep Llort
 * <p>
 * No bytes were intentionally harmed during the development of this application.
 * <p>
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.openkm.aceptable.config;

import javax.servlet.Filter;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.ErrorPageFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.openkm.aceptable.config.auth.CustomAuthenticationProvider;
import com.openkm.aceptable.config.auth.CustomLoginSuccessHandler;
import com.openkm.aceptable.config.auth.CustomLogoutSuccessHandler;

/**
 * Created by pavila on 17/06/16.
 */
@Configuration
@EnableWebSecurity
@ComponentScan("com.openkm")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	@Qualifier("dataSource")
	DataSource dataSource;

	@Autowired
	CustomLoginSuccessHandler customLoginSuccessHandler;

	@Autowired
	CustomLogoutSuccessHandler customLogoutSuccessHandler;

	@Autowired
	CustomAuthenticationProvider customAuthenticationProvider;

	@Autowired
	private UserDetailsService userDetailsService;


	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(customAuthenticationProvider).userDetailsService(userDetailsService);
		auth.eraseCredentials(false);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		super.configure(auth);
		auth.eraseCredentials(false);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Spring add ROLE_ to our roles automatically
		http.headers().frameOptions().sameOrigin().and()
			.csrf().disable()
			.cors().and()
			.authorizeRequests()
				.antMatchers("/private/**").authenticated()
				.antMatchers("/admin/**").access("hasRole('ADMIN')")
				.antMatchers("/catalog/**").access("hasRole('CATALOG')")
				.anyRequest().permitAll()
				.and()
//				.headers()
//				.addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
//				.and()
			.formLogin()
				.loginPage("/login")
				.failureUrl("/login?error")
				.successHandler(customLoginSuccessHandler)
				.defaultSuccessUrl("/")
				.permitAll()
				.and()
			.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessHandler(customLogoutSuccessHandler)
				.logoutSuccessUrl("/login")
				.permitAll()
				.and()
			.rememberMe()
				.rememberMeParameter("remember-me")
				.tokenRepository(persistentTokenRepository());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
	  super.configure(web);

	  web.httpFirewall(allowUrlEncodedSlashHttpFirewall());
	}

	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
		tokenRepository.setDataSource(dataSource);
		return tokenRepository;
	}

	@Bean
	public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
	    StrictHttpFirewall firewall = new StrictHttpFirewall();
	    firewall.setAllowUrlEncodedSlash(true);
	    firewall.setAllowSemicolon(true);
	    return firewall;
	}
	

	@Bean
	public FilterRegistrationBean<Filter> disableSpringBootErrorFilter(ErrorPageFilter filter) {
	    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
	    filterRegistrationBean.setFilter(filter);
	    filterRegistrationBean.setEnabled(false);
	    
	    return filterRegistrationBean;
	}

//	@Bean
//	public CorsConfigurationSource corsConfigurationSource() {
//		CorsConfiguration configuration = new CorsConfiguration();
//		configuration.setAllowedOrigins(Arrays.asList("*"));
//		configuration.setAllowedMethods(Arrays.asList("GET", "POST"));
//		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "content-type",
//				"x-requested-with", "Access-Control-Allow-Origin", "Access-Control-Allow-Headers", "x-auth-token",
//				"x-app-id", "Origin", "Accept", "X-Requested-With", "Access-Control-Request-Method",
//				"Access-Control-Request-Headers"));
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", configuration);
//		return source;
//	}

//	@Bean
//	public FilterRegistrationBean<CorsFilter> simpleCorsFilter() {
//	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//	    CorsConfiguration config = new CorsConfiguration();
//	    config.setAllowCredentials(true);
//	    // *** URL below needs to match the Vue client URL and port ***
//	    config.setAllowedOrigins(Collections.singletonList("*"));
//	    config.setAllowedMethods(Collections.singletonList("*"));
//	    config.setAllowedHeaders(Collections.singletonList("*"));
//	    source.registerCorsConfiguration("/**", config);
//	    FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(source));
//	    bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
//	    return bean;
//	}
}
