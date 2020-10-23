package com.ifood.quarkus.order.api;

import java.util.List;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.bson.types.ObjectId;

import com.ifood.quarkus.order.entity.Localizacao;
import com.ifood.quarkus.order.entity.Pedido;

import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.vertx.core.Vertx;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.mutiny.core.eventbus.EventBus;

public class OrderAPI {
	
	/**
	 * Implementação do vertx para criação do WebSocket
	 */
	@Inject
	Vertx vertx;
	

	/**
	 *  Evento que notifica o websocket para envio de dados de localização para o cliente.
	 */
	@Inject
	EventBus eventBus;

	 
	
	/**
	 * Registra o server do WebSocket no momento de start da aplicação.
	 * A comunicação do WebSocket ocorre na rota localizacoes disponibilizada pelo server. 
	 * Neste exemplo os dados de localização serão apenas consumidos por um client do WebSocket na rota localizacoes.
	 * @param router
	 */
	void startup(@Observes Router router) {
		router.route("/localizacoes*").handler(localizacaoHandler());
	}
	
	/**
	 * Define o tipo de autorização do client SockJSBridgeOptions: addOutboundPermitted (receber eventos) ou  addInboundPermitted(enviar dados).
	 *  Um evento pode ser disparado a partir de:
	 *  Um insert de registro em um banco de dados, ou em uma nova mensagem chegada no kafka, ou nesse caso uma chamada de uma api rest.
	 *  Assim que o evento chega no client automaticamente o websocket server envia o informação para o websocket client através da rota localizacoes
	 * @return
	 */

	private SockJSHandler localizacaoHandler() {
		SockJSHandler handler = SockJSHandler.create(vertx);
		PermittedOptions permitted = new PermittedOptions();
		permitted.setAddress("novaLocalizacao");
		SockJSBridgeOptions bridgeOptions = new SockJSBridgeOptions().addOutboundPermitted(permitted);
		handler.bridge(bridgeOptions);
		return handler;
	}

	
	/**
	 * Método post para receber os dados de localização do entregador. Dispara o evento que 
	 * notifica o websocket para envio dos dados da localização para o cliente.
	 * @param idPedido
	 * @param localizacao
	 * @return
	 */

	@POST
	@Path("{idPedido}/localizacao")
	public Pedido novaLocalizacao(@PathParam("idPedido") String idPedido, Localizacao localizacao) {
		Pedido pedido = Pedido.findById(new ObjectId(idPedido));

		pedido.localizacaoEntregador = localizacao;
		String json = JsonbBuilder.create().toJson(localizacao);
		eventBus.sendAndForget("novaLocalizacao", json);
		pedido.persistOrUpdate();
		return pedido;
	}
	
	/**
	 * Lista os pedidos.
	 * @return
	 */
	
	@GET
	public List<PanacheMongoEntityBase> getPedidos() {
		return Pedido.listAll();
	}
}
