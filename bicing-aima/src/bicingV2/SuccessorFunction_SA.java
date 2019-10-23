package bicingV2;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SuccessorFunction_SA implements SuccessorFunction {

    @Override
    public List getSuccessors(Object state) {
        ArrayList<Successor> retVal = new ArrayList<>();
        BicingState b_state = (BicingState) state;

        Furgoneta[] OldFurgos = b_state.getFurgos();
        final ArrayList<Pair> Exceed_Bicis = BicingState.getExceed_Bicis();
        final ArrayList<Pair> Demand_Bicis = BicingState.getDemand_Bicis();

        // Clonacion
        Furgoneta[] NewFurgos = new Furgoneta[OldFurgos.length];
        for (int f = 0; f < OldFurgos.length; f++) NewFurgos[f] = OldFurgos[f].clone();

        // Cambio iesimo a furgoneta ieasima
        Random random = new Random();
        int f = random.nextInt(NewFurgos.length);
        String result = "No ha entrado.";

        if (NewFurgos[f].hasOrigin()) {
            switch (random.nextInt(6)) {
                case 0: result = BicingOperatorSA.add_Bici(NewFurgos,f,Exceed_Bicis,Demand_Bicis, 1); break;
                case 1: result = BicingOperatorSA.add_Bici(NewFurgos,f,Exceed_Bicis,Demand_Bicis, 2); break;
                case 2: result = BicingOperatorSA.remove_Bici(NewFurgos,f,1); break;
                case 3: result = BicingOperatorSA.remove_Bici(NewFurgos,f,2); break;
                case 4: result = BicingOperatorSA.swap(NewFurgos,f); break;
                case 5: result = BicingOperatorSA.changeOrigin(NewFurgos,f,Exceed_Bicis); break;
            }
        }
        else result = BicingOperatorSA.changeOrigin(NewFurgos,f,Exceed_Bicis);

        // Anade el array al conjunto de sucesores.
        BicingState succ = new BicingState(NewFurgos);
        retVal.add(new Successor(result, succ));

        return retVal;
    }
}