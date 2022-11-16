package com.springboot.webflux.app;
import reactor.core.publisher.Flux;

import java.util.Date;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.springboot.webflux.app.models.dao.ProductoDao;
import com.springboot.webflux.app.models.documents.Producto;

import ch.qos.logback.classic.Logger;



@SpringBootApplication
public class SpringBootWebfluxProjectApplication implements CommandLineRunner  {
    @Autowired
	private ProductoDao dao;
	@Autowired
	private ReactiveMongoTemplate mongoTemplate;

	private static final Logger log= (Logger) LoggerFactory.getLogger(SpringBootWebfluxProjectApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebfluxProjectApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	     
		mongoTemplate.dropCollection("productos").subscribe();

		Flux.just(
			new Producto("mesa", 23.33),
			new Producto("pesa",56.3),
			new Producto("soga",15.33)

		)
		.flatMap(producto->{
			producto.setCreateAt(new Date(0));
			return dao.save(producto);
		})
		.subscribe(producto-> log.info("insert: "+ producto.getId()+" "+ producto.getNombre()));
	}
}


	
