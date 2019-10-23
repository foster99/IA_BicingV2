package bicingV2;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import model.*;
import java.util.ArrayList;
import java.util.List;

public class SuccessorFunction_HC implements SuccessorFunction {

    public List getSuccessors(Object state) {
        ArrayList<Successor> retVal = new ArrayList<>();
        BicingState b_state = (BicingState) state;

        Furgoneta[] Furgos = b_state.getFurgos();
        final ArrayList<Pair> Exceed_Bicis = BicingState.getExceed_Bicis();
        final ArrayList<Pair> Demand_Bicis = BicingState.getDemand_Bicis();

        // Si no tiene origen
        //BicingOperatorHC.changeOriginFull(Furgos, retVal, Exceed_Bicis, Demand_Bicis);
        BicingOperatorHC.changeOrigin(Furgos, retVal, Exceed_Bicis);

        // Cuando le falta un destino
        BicingOperatorHC.fill_SinDest(Furgos, retVal, Demand_Bicis, 1);
        BicingOperatorHC.fill_SinDest(Furgos, retVal, Demand_Bicis, 2);
        BicingOperatorHC.swap_anyway(Furgos, retVal, Demand_Bicis);

        // add/remove
        BicingOperatorHC.add_Bici_ConDest(Furgos, retVal, 1, Exceed_Bicis);
        BicingOperatorHC.add_Bici_ConDest(Furgos, retVal, 2, Exceed_Bicis);
        BicingOperatorHC.remove_Bici(Furgos, retVal, 1);
        BicingOperatorHC.remove_Bici(Furgos, retVal, 2);
        //Operator.swap_Dest(Furgos, retVal);

        return retVal;
    }


}