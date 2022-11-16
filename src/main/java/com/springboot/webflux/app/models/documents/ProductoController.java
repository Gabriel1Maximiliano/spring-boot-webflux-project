package com.springboot.webflux.app.models.documents;



import java.time.Duration;
import java.util.Date;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;


import com.springboot.webflux.app.models.services.ProductoService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SessionAttributes("producto")
@Controller
public class ProductoController {
    @Autowired
    private ProductoService service;

    private static final Logger log= LoggerFactory.getLogger(ProductoController.class);
@GetMapping({"/listar","/"})
    public Mono<String> listar(Model model){

        Flux<Producto> productos = service.findAllConNombreUpperCase();
        
        productos.subscribe(prod-> log.info(prod.getNombre()));

        model.addAttribute("productos", productos);
        model.addAttribute("titulo", "Listado de productos");
        return Mono.just("listar");
    }
    @GetMapping("/form")  
      public Mono<String> crear(Model model){
        model.addAttribute("producto", new Producto());
        model.addAttribute("titulo", "Formulario del producto");
        model.addAttribute("boton", "Crear");
        return Mono.just("form");
    }
    @GetMapping("/form-v2/{id}")
    public Mono<String> editarV2(@PathVariable String id, Model model){
       return service.findById(id).doOnNext(p->{
            log.info("producto"+ p.getNombre());
            model.addAttribute("boton", "Editar");
            model.addAttribute("titulo", "Editar producto");
            model.addAttribute("producto", p);
        }).defaultIfEmpty(new Producto())
        .flatMap(p->{
             if( p.getId() == null ){
                return Mono.error(new InterruptedException("No existe elproducto "));
            }
            return Mono.just(p);
        })
        .then(Mono.just("form"))
        .onErrorResume(ex-> Mono.just("redirect:/listar?error=no+existe+el+producto"));
       
          
    }
    @GetMapping("/form/{id}")
    public Mono<String> editar(@PathVariable String id, Model model){
        Mono<Producto> productomono = service.findById(id).doOnNext(p->{
            log.info("producto"+ p.getNombre());
        }).defaultIfEmpty(new Producto());
        model.addAttribute("boton", "Editar");
        model.addAttribute("titulo", "Editar producto");
        model.addAttribute("producto", productomono);
              return Mono.just("form");
    }
    @PostMapping("/form")
    public Mono<String> guardar(@Valid Producto producto,BindingResult result ,Model model, SessionStatus status){
        if(result.hasErrors()){
            model.addAttribute("titulo", "Errores en el formulario");
            model.addAttribute("boton", "Guardar");
            return Mono.just("form");
        }else{
            status.setComplete();
            if(producto.getCreateAt() == null ){
                producto.setCreateAt(new Date());
            }
            return service.save(producto).doOnNext(p->{
                log.info("Producto guardado"+ p.getNombre()+"Id :"+p.getId());
            }).thenReturn("redirect:/listar?success=producto+guardado+con+éxito");
        }
      
    }

    @GetMapping("/listar-datadriver")
    public String listarDataDriver(Model model){

        Flux<Producto> productos = service.findAllConNombreUpperCase().delayElements(Duration.ofSeconds(1));
        productos.subscribe(prod-> log.info(prod.getNombre()));

        model.addAttribute("productos",new ReactiveDataDriverContextVariable(productos,2));
        model.addAttribute("titulo", "Listado de productos");
        return "listar";
    }
    @GetMapping("/listar-full")
	public String listarFull(Model model) {
		
		Flux<Producto> productos = service.findAllConNombreUpperCaseRepeat();

		model.addAttribute("productos", productos);
		model.addAttribute("titulo", "Listado de productos");
		return "listar";
	}
    	@GetMapping("/listar-chunked")
	public String listarChunked(Model model) {
		
		Flux<Producto> productos = service.findAllConNombreUpperCaseRepeat();
		
		model.addAttribute("productos", productos);
		model.addAttribute("titulo", "Listado de productos");
		return "listar-chunked";
	}
    
} 
