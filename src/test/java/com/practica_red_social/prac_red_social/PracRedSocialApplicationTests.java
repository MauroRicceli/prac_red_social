package com.practica_red_social.prac_red_social;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PracRedSocialApplicationTests {

    @BeforeAll
    static void setup(){
        //CARGO MIS VARIABLES DE ENTORNO.
        Dotenv dotenv = Dotenv.load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
    }

	@Test
	void contextLoads() {
	}

}
