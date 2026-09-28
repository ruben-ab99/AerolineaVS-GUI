package com.aerolineavs.tarifas;

import javax.swing.SwingUtilities;

/**
 * Punto de entrada de la aplicación de escritorio.
 */
public final class GuiApp {

    private GuiApp() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            IPricingService pricingService = new PricingServiceImpl();
            new TarifasGui(pricingService).setVisible(true);
        });
    }
}
