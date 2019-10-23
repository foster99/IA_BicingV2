package bicingV2;

import IA.Bicing.Estaciones;
import IA.Bicing.Estacion;
import aima.search.framework.Problem;
import model.Board;
import model.Furgoneta;
import model.Pair;


import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map;

public class BicingState {

    // Atributos estaticos del mapa del problema
    private static Estaciones Stations;
    private static ArrayList<Pair> demand_bicis;
    private static TreeMap<Integer,Integer> exceed_bicis;
    private static int max_furgo;

    private Furgoneta[] Furgos;

    public BicingState(Furgoneta[] NewFurgos) {
        Furgos = NewFurgos;
    }

    public BicingState(int nest, int nbic, int demanda, int seed, int num_furgo) {

        max_furgo = num_furgo;
        Stations = new Estaciones(nest, nbic, demanda, seed);

        demand_bicis = new ArrayList<>();   // first -> id || second ->  bicis que faltan hasta la demanda
        exceed_bicis= new TreeMap<>();  // first -> id || second ->  bicis que sobran

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
            else exceed_bicis.put(Math.min(next - dem, noused),i);

        }

        Furgos = new Furgoneta[Math.min(num_furgo,exceed_bicis.size())];
    }

    // SOLUCIONES INICIALES
    public void initialSolution0(){
        //USAR LAS ESTACIONES CON MAYOR NUMERO DE BICIS SOBRANTES

        int i=0, sz=exceed_bicis.size(), aux,j=0;
        aux= sz-max_furgo -1;

        for(Map.Entry<Integer,Integer> entry : exceed_bicis.entrySet()) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();

            if(max_furgo < sz){
                if(i >= aux){
                    Furgos[j++]= new Furgoneta(value, -1,-1, Math.min(key,30),0,0);
                }
            }
            else Furgos[i]= new Furgoneta(value, -1,-1, Math.min(key,30),0,0);

            ++i;
        }
    }
    public void initialSolution1() {}
    public void initialSolution2() {}

    // EVALUACION DEL ESTADO
    double computeBenefits() {

        double Benefits = 0;
        int[] demandas = new int[Stations.size()];

        // Dinero gastado en transporte de bicis
        for (Furgoneta furgo : Furgos) {

            if (!furgo.hasOrigin() && furgo.hasD1()) continue;

            Benefits -= furgo.costeRecorrido();

            demandas[furgo.d1] += furgo.qtt1;

            if (furgo.hasD2())
                demandas[furgo.d2] += furgo.qtt2;
        }

        for (Pair est : demand_bicis)
            Benefits += Math.min(demandas[est.first], est.second);

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
    public static TreeMap<Integer,Integer> getExceed_Bicis() { return exceed_bicis;}
    public static Estaciones getStations() {
        return Stations;
    }
}
