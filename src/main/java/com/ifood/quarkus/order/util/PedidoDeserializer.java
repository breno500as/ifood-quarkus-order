package com.ifood.quarkus.order.util;

import com.ifood.quarkus.order.dto.PedidoRealizadoDTO;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class PedidoDeserializer extends ObjectMapperDeserializer<PedidoRealizadoDTO> {

    public PedidoDeserializer() {
        super(PedidoRealizadoDTO.class);
    }

}