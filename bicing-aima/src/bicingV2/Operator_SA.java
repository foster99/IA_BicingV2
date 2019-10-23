package bicingV2;

import model.Furgoneta;
import model.Pair;

import java.util.Random;

class Operator_SA {

   static String fullD1(Furgoneta[] NewFurgos, int f, int dest) {
       // Operador que a una furgoneta con origen definido, le asigna un D1 con todas las bicicletas posibles.
       setD1(NewFurgos[f], dest);
       return "Se ha llenado el D1 (y eliminado D2 si tenia).";
   }

   private static void setD1(Furgoneta f, int index) {
       Pair destino = BicingState.getDemand_Bicis().get(index); // Indice sobre Stations de la Estacion destino y su demanda
       f.d1 = destino.first;
       f.qtt1 = Math.min(destino.second, f.qtt0);
       f.d2 = -1;
       f.qtt2 = 0;
   }

}
