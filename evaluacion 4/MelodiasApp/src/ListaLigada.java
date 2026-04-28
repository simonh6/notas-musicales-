import javax.sound.midi.*;
import java.io.*;
import org.json.*;

public class ListaLigada {

    Nodo cabeza;

    public void agregar(Nota n) {
        Nodo nuevo = new Nodo(n);

        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo temp = cabeza;
            while (temp.siguiente != null) {
                temp = temp.siguiente;
            }
            temp.siguiente = nuevo;
        }
    }

    public void eliminarPrimero() {
        if (cabeza != null) {
            cabeza = cabeza.siguiente;
        }
    }

    public void limpiar() {
        cabeza = null;
    }

    //  REPRODUCIR
    public void reproducir() {
        try {
            Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();
            MidiChannel canal = synth.getChannels()[0];

            Nodo temp = cabeza;

            while (temp != null) {
                int notaMidi = convertirNota(temp.dato.nota, temp.dato.octava);
                int duracion = convertirFigura(temp.dato.figura);

                canal.noteOn(notaMidi, 100);
                Thread.sleep(duracion);
                canal.noteOff(notaMidi);

                temp = temp.siguiente;
            }

            synth.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int convertirNota(String nota, int octava) {
        int base = 0;

        switch (nota.toUpperCase()) {
            case "DO": base = 0; break;
            case "RE": base = 2; break;
            case "MI": base = 4; break;
            case "FA": base = 5; break;
            case "SOL": base = 7; break;
            case "LA": base = 9; break;
            case "SI": base = 11; break;
        }

        return (octava + 1) * 12 + base;
    }

    private int convertirFigura(String figura) {
        switch (figura.toUpperCase()) {
            case "REDONDA": return 1600;
            case "BLANCA": return 800;
            case "NEGRA": return 400;
            case "CORCHEA": return 200;
        }
        return 400;
    }

    //  GUARDAR JSON
    public void guardarJSON() {
        JSONArray array = new JSONArray();

        Nodo temp = cabeza;
        while (temp != null) {
            JSONObject obj = new JSONObject();
            obj.put("nota", temp.dato.nota);
            obj.put("figura", temp.dato.figura);
            obj.put("octava", temp.dato.octava);

            array.put(obj);
            temp = temp.siguiente;
        }

        try {
            FileWriter file = new FileWriter("melodia.json");
            file.write(array.toString(4));
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //  CARGAR JSON
    public void cargarJSON(String ruta) {
    limpiar();

     try {
         BufferedReader br = new BufferedReader(new FileReader(ruta));
         StringBuilder sb = new StringBuilder();
         String linea;

         while ((linea = br.readLine()) != null) {
             sb.append(linea);
         }

         JSONArray array = new JSONArray(sb.toString());

         for (int i = 0; i < array.length(); i++) {
             JSONObject obj = array.getJSONObject(i);

             Nota n = new Nota(
                obj.getString("nota"),
                obj.getString("figura"),
                obj.getInt("octava")
            );

            agregar(n);
          }

        } catch (Exception e) {
        e.printStackTrace();
     }
    }
}