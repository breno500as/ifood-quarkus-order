package com.ifood.quarkus.order.entity;

import org.bson.types.Decimal128;

import io.quarkus.mongodb.panache.PanacheMongoEntity;

public class Prato extends PanacheMongoEntity {

	public String nome;

	public String descricao;

	public Decimal128 preco;

}
