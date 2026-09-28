package com.aerolineavs.tarifas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * Ventana de escritorio para solicitar una recomendación de tarifa.
 */
public final class TarifasGui extends JFrame {

    private static final Color BACKGROUND = new Color(244, 246, 241);
    private static final Color SURFACE = new Color(255, 255, 252);
    private static final Color INK = new Color(35, 48, 43);
    private static final Color MUTED = new Color(104, 117, 110);
    private static final Color GREEN = new Color(29, 65, 54);
    private static final Color LIME = new Color(218, 240, 112);
    private static final Color FIELD_BORDER = new Color(213, 221, 214);

    private final IPricingService pricingService;
    private final JTextField ageField = createTextField("30");
    private final JTextField flightsField = createTextField("6");
    private final JTextField incomeField = createTextField("25000");
        private final JComboBox<Option<TipoViajero>> travelerType = combo(List.of(
            option("General", TipoViajero.GENERAL),
            option("Menor", TipoViajero.MENOR),
            option("Estudiante universitario desplazado", TipoViajero.ESTUDIANTE_UNIVERSITARIO_DESPLAZADO),
            option("Trabajador joven", TipoViajero.TRABAJADOR_JOVEN)
        ));
        private final JComboBox<Option<ClaseVuelo>> preferredClass = combo(List.of(
            option("Turista", ClaseVuelo.TURISTA),
            option("Business", ClaseVuelo.BUSINESS)
        ));
        private final JComboBox<Option<RegionDestino>> destination = combo(List.of(
            option("Europa", RegionDestino.EUROPA),
            option("Asia", RegionDestino.ASIA),
            option("América", RegionDestino.AMERICA),
            option("Otra región", RegionDestino.OTRA)
        ));
    private final JCheckBox travelsWithChildren = new JCheckBox("Viaja con niños menores de 12 años");
    private final JCheckBox livesWithParents = new JCheckBox("Vive con sus padres (jóvenes trabajadores)");
    private final JTextArea tariffName = createResultText();
    private final JLabel discountValue = new JLabel("--%", SwingConstants.LEFT);
    private final JTextArea assumptions = createAssumptionsText();

    public TarifasGui(IPricingService pricingService) {
        super("AerolineaVS | Tarifas de vuelo");
        this.pricingService = Objects.requireNonNull(pricingService, "pricingService no puede ser null");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(920, 700));
        setSize(1080, 780);
        setContentPane(buildContent());
        setLocationRelativeTo(null);
        showPlaceholder();
    }

    private JPanel buildContent() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BACKGROUND);
        root.add(createHeader(), BorderLayout.NORTH);

        JPanel workspace = new JPanel(new GridBagLayout());
        workspace.setOpaque(false);
        workspace.setBorder(new EmptyBorder(24, 28, 28, 28));

        GridBagConstraints formConstraints = new GridBagConstraints();
        formConstraints.gridx = 0;
        formConstraints.gridy = 0;
        formConstraints.weightx = 0.62;
        formConstraints.weighty = 1;
        formConstraints.fill = GridBagConstraints.BOTH;
        formConstraints.insets = new Insets(0, 0, 0, 18);
        workspace.add(createFormPanel(), formConstraints);

        GridBagConstraints resultConstraints = new GridBagConstraints();
        resultConstraints.gridx = 1;
        resultConstraints.gridy = 0;
        resultConstraints.weightx = 0.38;
        resultConstraints.weighty = 1;
        resultConstraints.fill = GridBagConstraints.BOTH;
        workspace.add(createResultPanel(), resultConstraints);

        root.add(workspace, BorderLayout.CENTER);
        return root;
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout(20, 0));
        header.setBackground(GREEN);
        header.setBorder(new EmptyBorder(24, 32, 24, 32));

        JLabel brand = new JLabel("AEROLÍNEA  /  TARIFAS");
        brand.setForeground(LIME);
        brand.setFont(new Font("SansSerif", Font.BOLD, 11));

        JLabel title = new JLabel("Elige tu próxima tarifa");
        title.setForeground(SURFACE);
        title.setFont(new Font("Serif", Font.BOLD, 28));

        JPanel titles = new JPanel(new GridLayout(0, 1, 0, 5));
        titles.setOpaque(false);
        titles.add(brand);
        titles.add(title);
        header.add(titles, BorderLayout.WEST);

        JLabel mark = new JLabel("AV", SwingConstants.CENTER);
        mark.setPreferredSize(new Dimension(48, 48));
        mark.setForeground(GREEN);
        mark.setBackground(LIME);
        mark.setOpaque(true);
        mark.setFont(new Font("Serif", Font.BOLD, 20));
        header.add(mark, BorderLayout.EAST);
        return header;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(SURFACE);
        panel.setBorder(new EmptyBorder(26, 28, 26, 28));

        JLabel sectionTitle = new JLabel("Perfil del viajero");
        sectionTitle.setForeground(INK);
        sectionTitle.setFont(new Font("SansSerif", Font.BOLD, 19));
        panel.add(sectionTitle, BorderLayout.NORTH);

        JPanel fields = new JPanel(new GridBagLayout());
        fields.setOpaque(false);
        addField(fields, 0, 0, "Edad", ageField);
        addField(fields, 0, 1, "Vuelos al año", flightsField);
        addField(fields, 1, 0, "Ingresos anuales (€)", incomeField);
        addField(fields, 1, 1, "Tipo de viajero", travelerType);
        addField(fields, 2, 0, "Clase preferida", preferredClass);
        addField(fields, 2, 1, "Destino habitual", destination);

        JPanel checks = new JPanel(new GridLayout(0, 1, 0, 8));
        checks.setOpaque(false);
        styleCheckBox(travelsWithChildren);
        styleCheckBox(livesWithParents);
        checks.add(travelsWithChildren);
        checks.add(livesWithParents);
        addSpanningRow(fields, checks, 3, new Insets(15, 0, 0, 0));

        JButton calculateButton = new JButton("Calcular tarifa");
        calculateButton.setBackground(GREEN);
        calculateButton.setForeground(Color.WHITE);
        calculateButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        calculateButton.setFocusPainted(false);
        calculateButton.setBorder(new EmptyBorder(12, 20, 12, 20));
        calculateButton.addActionListener(event -> calculateTariff());

        JButton clearButton = new JButton("Limpiar");
        clearButton.setForeground(GREEN);
        clearButton.setBackground(SURFACE);
        clearButton.setFocusPainted(false);
        clearButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER),
                new EmptyBorder(11, 18, 11, 18)
        ));
        clearButton.addActionListener(event -> clearForm());

        JPanel actions = new JPanel(new BorderLayout(10, 0));
        actions.setOpaque(false);
        actions.add(clearButton, BorderLayout.WEST);
        actions.add(calculateButton, BorderLayout.EAST);
        addSpanningRow(fields, actions, 4, new Insets(20, 0, 0, 0));

        panel.add(fields, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createResultPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.Y_AXIS));
        panel.setBackground(GREEN);
        panel.setBorder(new EmptyBorder(30, 28, 28, 28));

        JLabel eyebrow = new JLabel("RESULTADO");
        eyebrow.setForeground(LIME);
        eyebrow.setFont(new Font("SansSerif", Font.BOLD, 11));
        eyebrow.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(eyebrow);
        panel.add(javax.swing.Box.createVerticalStrut(34));

        tariffName.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(tariffName);
        panel.add(javax.swing.Box.createVerticalStrut(24));

        discountValue.setForeground(LIME);
        discountValue.setFont(new Font("Serif", Font.BOLD, 48));
        discountValue.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(discountValue);

        JLabel discountCaption = new JLabel("de descuento sobre la tarifa");
        discountCaption.setForeground(new Color(216, 226, 218));
        discountCaption.setFont(new Font("SansSerif", Font.PLAIN, 13));
        discountCaption.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(discountCaption);
        panel.add(javax.swing.Box.createVerticalStrut(34));

        JPanel rule = new JPanel();
        rule.setBackground(new Color(83, 113, 98));
        rule.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        rule.setPreferredSize(new Dimension(1, 1));
        rule.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(rule);
        panel.add(javax.swing.Box.createVerticalStrut(20));

        JLabel detailTitle = new JLabel("Criterios aplicados");
        detailTitle.setForeground(SURFACE);
        detailTitle.setFont(new Font("SansSerif", Font.BOLD, 13));
        detailTitle.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(detailTitle);
        panel.add(javax.swing.Box.createVerticalStrut(8));

        assumptions.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(assumptions);
        panel.add(javax.swing.Box.createVerticalGlue());
        return panel;
    }

    private void calculateTariff() {
        try {
            int age = parseNonNegativeInteger(ageField.getText(), "edad");
            int annualFlights = parseNonNegativeInteger(flightsField.getText(), "vuelos al año");
            double annualIncome = parseIncome(incomeField.getText());

            ClientePotencial customer = new ClientePotencial(
                    age,
                    annualFlights,
                    selectedValue(travelerType),
                    selectedValue(preferredClass),
                    selectedValue(destination),
                    annualIncome,
                    travelsWithChildren.isSelected(),
                    livesWithParents.isSelected()
            );
            ResultadoTarifa result = pricingService.evaluar(customer);
            tariffName.setText(result.tarifa().getNombre());
            discountValue.setText(result.tarifa().getDescuentoPorcentaje() + "%");
            assumptions.setText(result.suposiciones());
        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Revisa los datos introducidos",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void clearForm() {
        ageField.setText("30");
        flightsField.setText("6");
        incomeField.setText("25000");
        travelerType.setSelectedIndex(0);
        preferredClass.setSelectedIndex(0);
        destination.setSelectedIndex(0);
        travelsWithChildren.setSelected(false);
        livesWithParents.setSelected(false);
        showPlaceholder();
    }

    private void showPlaceholder() {
        tariffName.setText("Completa el perfil para ver la recomendación.");
        discountValue.setText("--%");
        assumptions.setText("La tarifa se determina según los datos del viajero y las reglas disponibles.");
    }

    private static void addField(JPanel panel, int row, int column, String label, java.awt.Component field) {
        JPanel fieldPanel = new JPanel(new BorderLayout(0, 7));
        fieldPanel.setOpaque(false);

        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setForeground(MUTED);
        fieldLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        fieldPanel.add(fieldLabel, BorderLayout.NORTH);
        fieldPanel.add(field, BorderLayout.CENTER);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = column;
        constraints.gridy = row;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.insets = new Insets(0, column == 0 ? 0 : 10, 17, column == 0 ? 10 : 0);
        panel.add(fieldPanel, constraints);
    }

    private static void addSpanningRow(JPanel panel, java.awt.Component component, int row, Insets insets) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.gridwidth = 2;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = insets;
        panel.add(component, constraints);
    }

    private static JTextField createTextField(String initialValue) {
        JTextField field = new JTextField(initialValue, 12);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setForeground(INK);
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER),
                new EmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }

    private static <T> JComboBox<Option<T>> combo(List<Option<T>> options) {
        JComboBox<Option<T>> box = new JComboBox<>();
        options.forEach(box::addItem);
        box.setFont(new Font("SansSerif", Font.PLAIN, 13));
        box.setForeground(INK);
        box.setBackground(Color.WHITE);
        box.setBorder(BorderFactory.createLineBorder(FIELD_BORDER));
        return box;
    }

    private static <T> Option<T> option(String label, T value) {
        return new Option<>(label, value);
    }

    private static <T> T selectedValue(JComboBox<Option<T>> box) {
        return box.getItemAt(box.getSelectedIndex()).value();
    }

    private static int parseNonNegativeInteger(String value, String fieldName) {
        try {
            int parsed = Integer.parseInt(value.trim());
            if (parsed < 0) {
                throw new IllegalArgumentException("El campo «" + fieldName + "» no puede ser negativo.");
            }
            return parsed;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Introduce un número entero válido para «" + fieldName + "».");
        }
    }

    private static double parseIncome(String value) {
        try {
            double parsed = Double.parseDouble(value.trim().replace(',', '.'));
            if (!Double.isFinite(parsed) || parsed < 0) {
                throw new IllegalArgumentException("Los ingresos deben ser un número válido igual o superior a cero.");
            }
            return parsed;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Introduce un importe válido para los ingresos anuales.");
        }
    }

    private static void styleCheckBox(JCheckBox checkBox) {
        checkBox.setOpaque(false);
        checkBox.setForeground(INK);
        checkBox.setFont(new Font("SansSerif", Font.PLAIN, 13));
        checkBox.setFocusPainted(false);
    }

    private static JTextArea createResultText() {
        JTextArea text = new JTextArea(2, 20);
        text.setEditable(false);
        text.setFocusable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setOpaque(false);
        text.setForeground(SURFACE);
        text.setFont(new Font("Serif", Font.BOLD, 25));
        text.setBorder(null);
        return text;
    }

    private static JTextArea createAssumptionsText() {
        JTextArea text = new JTextArea(5, 22);
        text.setEditable(false);
        text.setFocusable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setOpaque(false);
        text.setForeground(new Color(216, 226, 218));
        text.setFont(new Font("SansSerif", Font.PLAIN, 12));
        text.setBorder(null);
        return text;
    }

    private record Option<T>(String label, T value) {
        @Override
        public String toString() {
            return label;
        }
    }
}
