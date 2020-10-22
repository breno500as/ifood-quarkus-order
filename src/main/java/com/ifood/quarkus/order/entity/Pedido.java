package com.ifood.quarkus.order.entity;

import java.util.List;

import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntity;

@MongoEntity(collection = "pedido", database = "pedido")
public class Pedido extends PanacheMongoEntity {

	public String cliente;
	
	public List<Prato> pratos;
	
	public String entregador;
	
	public Localizacao localizacao;
	
	public Restaurante restaurante;
}
