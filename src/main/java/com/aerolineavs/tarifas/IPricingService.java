package com.aerolineavs.tarifas;

/**
 * Servicio de dominio para recomendar una tarifa a partir de los datos del cliente.
 */
public interface IPricingService {

    /**
     * Evalúa las reglas de elegibilidad para una única tarifa.
     *
     * @param cliente datos del cliente
     * @return tarifa resultante junto con las suposiciones de negocio
     */
    ResultadoTarifa evaluar(ClientePotencial cliente);
}
