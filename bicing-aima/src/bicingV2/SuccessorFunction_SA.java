package bicingV2;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import model.Board;
import model.Furgoneta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SuccessorFunction_SA implements SuccessorFunction {

    @Override
    public List getSuccessors(Object state) {
        ArrayList<Successor> retVal = new ArrayList<>();
        BicingState b_state = (BicingState) state;

        Furgoneta[] OldFurgos = b_state.getFurgos();
        Random rand = new Random();

        boolean generated = false;

        do {
            int f = rand.nextInt(OldFurgos.length);
            int dest = rand.nextInt(BicingState.getDemand_Bicis().size());

            // fullD1
            if (!OldFurgos[f].hasD1()) {

                if (Board.distance(OldFurgos[f].origin, BicingState.getDemand_Bicis().get(dest).first)/1000 >= BicingState.getDemand_Bicis().get(dest).second) continue;

                Furgoneta[] NewFurgos1 = new Furgoneta[OldFurgos.length];
                for (int i = 0; i < OldFurgos.length; i++) NewFurgos1[i] = OldFurgos[i].clone();
                String result1 = Operator.fullD1(NewFurgos1[f], dest);
                BicingState succ1 = new BicingState(NewFurgos1);
                retVal.add(new Successor(result1, succ1));
                generated = true;
            }
            // fullD2
            else if (!OldFurgos[f].hasD2()) {

                if (OldFurgos[f].d1 == BicingState.getDemand_Bicis().get(dest).first || Board.distance(OldFurgos[f].d1, BicingState.getDemand_Bicis().get(dest).first)/1000 >= BicingState.getDemand_Bicis().get(dest).second) continue;

                Furgoneta[] NewFurgos2 = new Furgoneta[OldFurgos.length];
                for (int i = 0; i < OldFurgos.length; i++) NewFurgos2[i] = OldFurgos[i].clone();
                String result2 = Operator.fullD2(NewFurgos2[f], dest);
                BicingState succ2 = new BicingState(NewFurgos2);
                retVal.add(new Successor(result2, succ2));
                generated = true;
            }

            // removeD1
            if (OldFurgos[f].hasD1()) {
                Furgoneta[] NewFurgos3 = new Furgoneta[OldFurgos.length];
                for (int i = 0; i < OldFurgos.length; i++) NewFurgos3[i] = OldFurgos[i].clone();
                String result3 = Operator.removeD1(NewFurgos3[f]);
                BicingState succ3 = new BicingState(NewFurgos3);
                retVal.add(new Successor(result3, succ3));
                generated = true;
            }
            // removeD2
            if (OldFurgos[f].hasD2()) {
                Furgoneta[] NewFurgos4 = new Furgoneta[OldFurgos.length];
                for (int i = 0; i < OldFurgos.length; i++) NewFurgos4[i] = OldFurgos[i].clone();
                String result = Operator.removeD2(NewFurgos4[f]);
                BicingState succ4 = new BicingState(NewFurgos4);
                retVal.add(new Successor(result, succ4));
                generated = true;
            }
        } while (!generated);

        return retVal;
    }
}