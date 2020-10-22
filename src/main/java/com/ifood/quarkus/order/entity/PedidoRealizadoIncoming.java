package com.ifood.quarkus.order.entity;

import java.util.ArrayList;

import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.JsonbBuilder;

import org.bson.types.Decimal128;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import com.ifood.quarkus.order.dto.PedidoRealizadoDTO;
import com.ifood.quarkus.order.dto.PratoPedidoDTO;
import com.ifood.quarkus.order.entity.Pedido;
import com.ifood.quarkus.order.entity.Prato;
import com.ifood.quarkus.order.entity.Restaurante;

@ApplicationScoped
public class PedidoRealizadoIncoming {

	@Incoming("pedidos")
	public void lerPedidos(PedidoRealizadoDTO dto) {
		System.out.println("-----------------");
		System.out.println(dto);

		Pedido p = new Pedido();
		p.cliente = dto.cliente;
		p.pratos = new ArrayList<>();
		dto.pratos.forEach(prato -> p.pratos.add(from(prato)));
		Restaurante restaurante = new Restaurante();
		restaurante.nome = dto.restaurante.nome;
		p.restaurante = restaurante;
		String json = JsonbBuilder.create().toJson(dto);
		// elastic.index("pedidos", json);
		p.persist();

	}

	private Prato from(PratoPedidoDTO prato) {
		Prato p = new Prato();
		p.descricao = prato.descricao;
		p.nome = prato.nome;
		p.preco = new Decimal128(prato.preco);
		return p;
	}
}