package com.springboot.webflux.app.models.documents;
import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;




@Document("productos")
public class Producto {

@Id
@NotEmpty
    private String id;

@NotEmpty
    private String nombre;

@NotNull
    private Double precio;

    private String foto;

public Categoria getCategoria() {
    return categoria;
}

public void setCategoria(Categoria categoria) {
    this.categoria = categoria;
}

@DateTimeFormat(pattern = "yyy-MM-dd")
    private Date createAt;
@Valid
    private Categoria categoria;

    public Producto() {}
    
	public Producto(String nombre, Double precio) {
		this.nombre = nombre;
		this.precio = precio;
	}
    public Producto(String nombre, Double precio, Categoria categoria) {
		this(nombre, precio);
        this.categoria= categoria;
	}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date date) { 
		this.createAt = date;
	}

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
    

   
}
