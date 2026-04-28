import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;

public class Ventana extends JFrame {

    ListaLigada lista = new ListaLigada();
    DefaultTableModel modelo;

    public Ventana() {
        setTitle("🎵 Editor de Melodías");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // TABLA
        modelo = new DefaultTableModel(new String[]{"Nota", "Figura", "Octava"}, 0);
        JTable tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // PANEL SUPERIOR
        JPanel panelTop = new JPanel();
        panelTop.setBackground(new Color(30, 30, 30));

        JComboBox<String> cbNota = new JComboBox<>(new String[]{
        "DO","RE","MI","FA","SOL","LA","SI"
    });

        JComboBox<String> cbFigura = new JComboBox<>(new String[]{
        "REDONDA","BLANCA","NEGRA","CORCHEA"
    });

        JComboBox<Integer> cbOctava = new JComboBox<>();

        for (int i = 1; i <= 8; i++) {
         cbOctava.addItem(i);
        }
        panelTop.add(new JLabel("Nota"));
        panelTop.add(cbNota);

        panelTop.add(new JLabel("Figura"));
        panelTop.add(cbFigura);

        panelTop.add(new JLabel("Octava"));
        panelTop.add(cbOctava);

        add(panelTop, BorderLayout.NORTH);

        // PANEL BOTONES
        JPanel panelBotones = new JPanel();


        JButton btnAgregar = new JButton("Agregar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnEditar = new JButton("Editar");
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCargar = new JButton("Cargar");
        JButton btnReproducir = new JButton("Reproducir");

        btnAgregar.setIcon(new ImageIcon(getClass().getResource("/img/agregar.png")));
        btnEliminar.setIcon(new ImageIcon(getClass().getResource("/img/eliminar.png")));
        btnEditar.setIcon(new ImageIcon(getClass().getResource("/img/editar.png")));
        btnGuardar.setIcon(new ImageIcon(getClass().getResource("/img/guardar.png")));
        btnCargar.setIcon(new ImageIcon(getClass().getResource("/img/cargar.png")));
        btnReproducir.setIcon(new ImageIcon(getClass().getResource("/img/play.png")));

        btnAgregar.setHorizontalTextPosition(SwingConstants.CENTER);
        btnAgregar.setVerticalTextPosition(SwingConstants.BOTTOM);

        btnEliminar.setHorizontalTextPosition(SwingConstants.CENTER);
        btnEliminar.setVerticalTextPosition(SwingConstants.BOTTOM);

        btnEditar.setHorizontalTextPosition(SwingConstants.CENTER);
        btnEditar.setVerticalTextPosition(SwingConstants.BOTTOM);

        btnGuardar.setHorizontalTextPosition(SwingConstants.CENTER);
        btnGuardar.setVerticalTextPosition(SwingConstants.BOTTOM);

        btnCargar.setHorizontalTextPosition(SwingConstants.CENTER);
        btnCargar.setVerticalTextPosition(SwingConstants.BOTTOM);

        btnReproducir.setHorizontalTextPosition(SwingConstants.CENTER);
        btnReproducir.setVerticalTextPosition(SwingConstants.BOTTOM);  

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCargar);
        panelBotones.add(btnReproducir);

        add(panelBotones, BorderLayout.SOUTH);

        // EVENTOS

        btnAgregar.addActionListener(e -> {
        String nota = cbNota.getSelectedItem().toString();
        String figura = cbFigura.getSelectedItem().toString();
        int octava = (int) cbOctava.getSelectedItem();

        Nota n = new Nota(nota, figura, octava);
        lista.agregar(n);

        modelo.addRow(new Object[]{nota, figura, octava});
        });

        btnEliminar.addActionListener(e -> {
            lista.eliminarPrimero();
            if (modelo.getRowCount() > 0) {
                modelo.removeRow(0);
            }
        });
        
       btnEditar.addActionListener(e -> {
         int fila = tabla.getSelectedRow();

          if (fila != -1) {
           String nota = cbNota.getSelectedItem().toString();
           String figura = cbFigura.getSelectedItem().toString();
           int octava = (int) cbOctava.getSelectedItem();

           // actualizar tabla
           modelo.setValueAt(nota, fila, 0);
           modelo.setValueAt(figura, fila, 1);
           modelo.setValueAt(octava, fila, 2);
 
           // actualizar lista ligada
           actualizarLista(fila, nota, figura, octava);

         } else {
          JOptionPane.showMessageDialog(this, "Selecciona una fila para editar");
         }
        });

        btnGuardar.addActionListener(e -> {
            lista.guardarJSON();
            JOptionPane.showMessageDialog(this, "Guardado ");
        });

        btnCargar.addActionListener(e -> {

         JFileChooser fileChooser = new JFileChooser();

         int opcion = fileChooser.showOpenDialog(this);

         if (opcion == JFileChooser.APPROVE_OPTION) {

         File archivo = fileChooser.getSelectedFile();

         lista.cargarJSON(archivo.getAbsolutePath());

         // limpiar tabla
          modelo.setRowCount(0);

         // volver a llenar tabla
          Nodo temp = lista.cabeza;
          while (temp != null) {
             modelo.addRow(new Object[]{
                temp.dato.nota,
                temp.dato.figura,
                temp.dato.octava
             });
          temp = temp.siguiente;
          }
         }
        });

        btnReproducir.addActionListener(e -> {
            lista.reproducir();
        });
    }
    private void actualizarLista(int index, String nota, String figura, int octava) {
    Nodo temp = lista.cabeza;

    for (int i = 0; i < index; i++) {
        temp = temp.siguiente;
    }

    temp.dato.nota = nota;
    temp.dato.figura = figura;
    temp.dato.octava = octava;
  } 
}