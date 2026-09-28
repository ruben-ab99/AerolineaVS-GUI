package com.aerolineavs.tarifas;

/**
 * Fachada de compatibilidad para el antiguo punto de entrada del evaluador.
 */
@Deprecated
public final class EvaluadorTarifas {

    private static final IPricingService PRICING_SERVICE = new PricingServiceImpl();

    private EvaluadorTarifas() {
    }

    public static ResultadoTarifa evaluar(ClientePotencial cliente) {
        return PRICING_SERVICE.evaluar(cliente);
    }
}
