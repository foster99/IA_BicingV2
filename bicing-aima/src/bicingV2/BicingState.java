package bicingV2;

import IA.Bicing.Estaciones;
import IA.Bicing.Estacion;
import model.Board;
import model.Furgoneta;
import model.Pair;


import java.util.*;

public class BicingState {

    // Atributos estaticos del mapa del problema
    private static Estaciones Stations;
    private static ArrayList<Pair> demand_bicis;
    private static ArrayList<Pair> exceed_bicis;
    private static TreeMap<Double,Integer> sorted_exceed;
    private static int max_furgo;

    private Furgoneta[] Furgos;

    public BicingState(Furgoneta[] NewFurgos) {
        Furgos = NewFurgos;
    }

    public BicingState(int nest, int nbic, int demanda, int seed, int num_furgo, int init_sol) {

        Stations = new Estaciones(nest, nbic, demanda, seed);

        demand_bicis = new ArrayList<>();   // first -> id || second ->  bicis que faltan hasta la demanda
        exceed_bicis = new ArrayList<>();
        sorted_exceed = new TreeMap<>();  // key -> disponible || value ->  origin index

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
            else {
                int value = Math.min(next - dem, noused);
                if (value > 0) {
                    exceed_bicis.add(new Pair(i, value));
                    if (init_sol == 0) sorted_exceed.put((double) value, i);
                }
            }

        }
        max_furgo = Math.min(num_furgo, exceed_bicis.size());
        Furgos = new Furgoneta[max_furgo];
        Collections.sort(exceed_bicis, new Pair.SortBySecond());
    }

    // SOLUCIONES INICIALES
    public void initialSolution0() {
        //USAR LAS ESTACIONES CON MAYOR NUMERO DE BICIS SOBRANTES

        int i = 0, j = 0, sz = exceed_bicis.size(), aux = sz - max_furgo;

        for(Map.Entry<Double,Integer> entry : sorted_exceed.entrySet()) {

            if(max_furgo < sz) {
                if (--aux > 0) Furgos[j++] = new Furgoneta(entry.getValue(), -1, -1, 0, 0);
            }
            else Furgos[i++]= new Furgoneta(entry.getValue(), -1,-1,0,0);
        }
    }
    public void initialSolution1() {

        for (Pair origin : exceed_bicis) {

            double points = 0;
            int[][] stats = new int[40][2];
            for (Pair dem : demand_bicis) {
                int rango = Board.distance(dem.first, origin.first)/1000;
                stats[rango][0]++;
                stats[rango][1] += dem.second;
            }
/*
            for(Map.Entry<Integer,Integer> other_origin : exceed_bicis.entrySet()) {
                if (origin == other_origin) continue;
                points -= Board.distance()
            }*/

            for (int i = 0; i < stats.length; i++) {
                int[] range_value = stats[i];
                if (range_value[0] == 0) continue;
                points += ((double) range_value[1] / (double) range_value[0])/ Math.log(i+0.5);
            }

            double value = 0.2;
            sorted_exceed.put(value*points + (1-value)*points, origin.first);

        }//25 1250 5 0 1234 1 0


        int i=0, j = 0, sz = sorted_exceed.size(), aux = sz - max_furgo;

        for(Map.Entry<Double,Integer> origin : sorted_exceed.entrySet()) {

            if(max_furgo < sz) {
                if (i >= aux) Furgos[j++] = new Furgoneta(origin.getValue(), -1, -1, 0, 0);
            }
            else Furgos[i]= new Furgoneta(origin.getValue(), -1,-1,0,0);

            ++i;
        }
    }
    public void initialSolution2() {
        // ASIGNACION RANDOM
        Random rand = new Random();
        int origin;
        boolean[] used = new boolean[max_furgo];
        for (int i = 0; i < max_furgo; i++) {
            do origin = rand.nextInt(max_furgo); while(!used[origin]);
            Furgos[i] = new Furgoneta(origin,-1,-1,0,0);
        }
    }

    // EVALUACION DEL ESTADO
    double computeBenefits() {

        double Benefits = 0;
        int[] demandas = new int[Stations.size()];

        // Dinero gastado en transporte de bicis
        for (Furgoneta furgo : Furgos) {

            if (!furgo.hasOrigin() || !furgo.hasD1()) continue;

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
            if (furgo.isActive()) active++;
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
    public static TreeMap<Double,Integer> getSorted_exceed() { return sorted_exceed;}
    public static ArrayList<Pair> getExceed_Bicis() { return exceed_bicis;}
    public static Estaciones getStations() {
        return Stations;
    }
    public static int getMax_furgo() {
        return max_furgo;
    }
}
