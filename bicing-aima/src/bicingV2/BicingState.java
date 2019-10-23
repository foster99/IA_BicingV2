package bicingV2;

import IA.Bicing.Estaciones;
import IA.Bicing.Estacion;
import aima.search.framework.Problem;
import model.Board;
import model.Furgoneta;
import model.Pair;

import java.util.ArrayList;

public class BicingState {

    // Atributos estaticos del mapa del problema
    private static Estaciones Stations;
    private static ArrayList<Pair> demand_bicis;
    private static ArrayList<Pair> exceed_bicis;

    private Furgoneta[] Furgos;

    public BicingState(int nest, int nbic, int demanda, int seed, int num_furgo) {
        Stations = new Estaciones(nest, nbic, demanda, seed);

        demand_bicis = new ArrayList<>();   // first -> id || second ->  bicis que faltan hasta la demanda
        exceed_bicis  = new ArrayList<>();  // first -> id || second ->  bicis que sobran

        Pair P;
        for (int i = 0; i < Stations.size(); ++i) {
            // Separar estaciones con falta y exceso de bicis
            Estacion S = Stations.get(i);
            int dem = S.getDemanda();
            int next = S.getNumBicicletasNext();
            int noused = S.getNumBicicletasNoUsadas();

            if (dem > next) {
                P = new Pair(i, dem-next);
                demand_bicis.add(P);
            }
            else { // solo me puedo llevar de lo que hay (lo no usado)
                P = new Pair(i, Math.min(next - dem, noused));
                exceed_bicis.add(P);
            }
        }

        Furgos = new Furgoneta[Math.min(num_furgo,exceed_bicis.size())];
        for (int i = 0; i < Furgos.length; i++)
            Furgos[i] = new Furgoneta(-1, -1, -1, 0, 0);
    }

    // SOLUCIONES INICIALES
    public void initialSolution0() {

        // No hace nada. Todas las furgonetas estan inactivas.
    }
    public void initialSolution1() {}
    public void initialSolution2() {}

    // EVALUACION DEL ESTADO
    public double computeBenefits() {

        double Benefits = 0;
        Furgoneta[] Furgos = this.getFurgos();
        Estaciones Stations = BicingState.getStations(); // acceso a partir de clase porque es un elemento estatico

        int[] Estaciones = new int[Stations.size()];

        // Suma de las bicis que me puedo llevar
        for (Pair exceed : exceed_bicis) {
            Estaciones[exceed.first] = exceed.second;
        }


        // Dinero gastado en transporte de bicis
        for (Furgoneta furgo : Furgos) {

            if (!furgo.hasOrigin()) continue;

            Benefits -= furgo.costeRecorrido();         // Restamos coste por movimiento de bicicletas.

            if (furgo.hasD1()) {
                Estaciones[furgo.origin] -= furgo.qtt1; // Restamos lo que nos llevamos para ver despues si nos pasamos.
                Estaciones[furgo.d1] += furgo.qtt1;     // Sumamos para ver lo que nos acercamos a la demanda.
            }
            if (furgo.hasD2()) {
                Estaciones[furgo.origin] -= furgo.qtt2;
                Estaciones[furgo.d2] += furgo.qtt2;
            }
        }

        // Sumas el beneficio por acercarte a la demanda
        for (Pair dem : demand_bicis) {
            if (Estaciones[dem.first] > 0) {
                Estacion e = Stations.get(dem.first);
                Benefits +=  Math.min(e.getDemanda()-e.getNumBicicletasNext(), Estaciones[dem.first]);
            }
        }

        // Restas beneficio que corresponde a las bicis que has quitado de mas de donde sobran (no probable)
        for (Pair exceed : exceed_bicis) {
            if (Estaciones[exceed.first] < 0) Benefits += Estaciones[exceed.first];
        }

        return Benefits;
    }
    public double getActive() {

        int active = 0;
        for (Furgoneta furgo : Furgos) {
            if (furgo.hasOrigin()) active++;
        }
        return active;
    }
    public String AsignacionBicisToString() {

        // Hay que programar el printing de las asignaciones de las bicicletas.
        // es decir, poner cuantas furgos hay, cuantas bicis cada furgo, donde dejan las bicis, los km recorridos
        // y tambien el coste (puede ser desglosado o no) y los beneficios.

        // en otras palabras, imprimir la solucion para que veamos que cojones ha encontrado

        boolean linux_tabs = true;
        long time = System.currentTimeMillis() - Board.tick;

        StringBuilder ret = new StringBuilder();
        ret.append("\n\nID\tOriX\tOriY\tQttT\tDes1X\tDes1Y\tQtt1\tDes2X\tDes2Y\tQtt2\n");

        int id = 1;
        for (Furgoneta f : Furgos) {
            String FurgoInfo = f.toString(linux_tabs);
            if (FurgoInfo == null) continue;
            ret.append(id).append("\t").append(FurgoInfo).append('\n');
            ++id;
        }

        double distancia_total = 0;
        for (Furgoneta res : this.Furgos) {
            distancia_total += res.totalDistance();
        }

        ret.append("\n- Beneficios:\t").append(this.computeBenefits()).append(" euros.\n");
        ret.append("- Distancia:\t").append(distancia_total).append(" km.\n");
        ret.append("- Tiempo: \t").append(time).append(" ms.\n");

        return ret.toString();
    }

    // GETTERS
    public Furgoneta[] getFurgos() { return Furgos; }
    public static ArrayList<Pair> getDemand_Bicis() { return demand_bicis;}
    public static ArrayList<Pair> getExceed_Bicis() { return exceed_bicis;}
    public static Estaciones getStations() {
        return Stations;
    }
}
