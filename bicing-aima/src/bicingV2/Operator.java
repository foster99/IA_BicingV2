package bicingV2;

import IA.Bicing.Estacion;
import model.Furgoneta;
import model.Pair;

@SuppressWarnings("ALL")
class Operator {
    static String fullD1(Furgoneta F, int dest) {
        // Operador que a una furgoneta con origen definido, le asigna un D1 con todas las bicicletas posibles.
        Pair destino = BicingState.getDemand_Bicis().get(dest); // Indice sobre Stations de la Estacion destino y su demanda

        Estacion est = BicingState.getStations().get(F.origin);
        F.d1 = destino.first;
        F.qtt1 = Math.min(destino.second, Math.min(est.getNumBicicletasNext() - est.getDemanda(), est.getNumBicicletasNoUsadas()));

        return "[fullD1]: Destino 1 asignado y lleno en furgoneta.";
    }
    static String fullD2(Furgoneta F, int dest) {
        // Operador que a una furgoneta con origen definido, le asigna un D1 con todas las bicicletas posibles.
        Pair destino = BicingState.getDemand_Bicis().get(dest); // Indice sobre Stations de la Estacion destino y su demanda

        Estacion est = BicingState.getStations().get(F.origin);

        F.d2 = destino.first;
        F.qtt2 = Math.min(destino.second, Math.min(est.getNumBicicletasNext() - est.getDemanda(), est.getNumBicicletasNoUsadas())) - F.qtt1;

        return "[fullD2]: Destino 2 asignado y lleno (lo maximo posible) en furgoneta.";
    }
    static String removeD1(Furgoneta F) {
        if (--F.qtt1 <= 0) {
            F.off();
            return "[removeD1]: Off en furgoneta.";
        }
        return "[removeD1]: --Qtt1 en furgoneta.";
    }
    static String removeD2(Furgoneta F) {
        if (--F.qtt2 <= 0) {
            F.d2 = -1;
            F.qtt2 = 0;
            return "[removeD2]: D2 eliminado.";
        }
        return "[removeD2]: --Qtt2 en furgoneta.";
    }
}
