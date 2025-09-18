package com.practica_red_social.prac_red_social;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PracRedSocialApplication {

	public static void main(String[] args) {

        //CARGO MIS VARIABLES DE ENTORNO.
        Dotenv dotenv = Dotenv.load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

        SpringApplication.run(PracRedSocialApplication.class, args);
	}

}
