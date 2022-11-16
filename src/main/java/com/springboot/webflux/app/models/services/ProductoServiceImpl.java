package com.springboot.webflux.app.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.webflux.app.models.dao.ProductoDao;
import com.springboot.webflux.app.models.documents.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoDao dao;

    @Override
    public Flux<Producto> findAll() {
        
        return dao.findAll();
    }

    @Override
    public Mono<Producto> findById(String id) {
      
        return dao.findById(id);
    }

    @Override
    public Mono<Producto> save(Producto producto) {
    
        return dao.save(producto);
    }

    @Override
    public Mono<Void> delete(Producto producto) {
        
        return dao.delete(producto);
    }

    @Override
    public Flux<Producto> findAllConNombreUpperCase() {
        // TODO Auto-generated method stub
        return dao.findAll().map(producto->{
            producto.setNombre(producto.getNombre().toUpperCase());
            return producto;
        });
    }
    
}
