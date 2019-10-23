package model;

import IA.Bicing.Estacion;
import bicingV2.BicingState;

public class Furgoneta {
    int origin;
    int d1;
    int d2;
    int qtt0;
    int qtt1;
    int qtt2;

    public Furgoneta(int origin, int d1, int d2, int qtt1, int qtt2) {
        this.origin = origin;
        this.d1 = d1;
        this.d2 = d2;
        this.qtt1 = qtt1;
        this.qtt2 = qtt2;
    }

    public int getTotal(){
        return qtt1+qtt2;
    }
    public boolean full() {
        return qtt1 + qtt2 == 30;
    }
    public boolean hasD1() {
        return d1 != -1;
    }
    public boolean hasD2() {
        return  d2 != -1;
    }
    public boolean hasOrigin() { return origin != -1; }

    void swapDest() {
        if(!hasD1() && hasD2()){
            d1 = d2;
            qtt1 = qtt2;
            d2 = -1;
            qtt2 = 0;
        }
        else if(hasD1() && hasD2()){
            int daux = d1, qttaux = qtt1;
            d1= d2;
            qtt1=qtt2;
            d2=daux;
            qtt2=qttaux;
        }
    }

    public double totalDistance() {
        if (!this.hasOrigin()) return 0;
        if (!this.hasD1()) {
            if (this.hasD2()) return Board.distance(origin, d2)/1000.0;
            return 0;
        } else {
            if (!this.hasD2()) return Board.distance(origin, d1)/1000.0;
            return Board.distance(origin, d1)/1000.0 + Board.distance(d1, d2)/1000.0;
        }
    }

    public double costeRecorrido() {
        if (!this.hasOrigin()) return 0;
        if (!this.hasD1()) {
            if (this.hasD2()) return (Board.distance(origin, d2)/1000.0) * (qtt2 + 9)/10;
            return 0;
        } else {
            if (!this.hasD2()) return (Board.distance(origin, d1)/1000.0) * (qtt1 + 9)/10;
            return (Board.distance(origin, d1)/1000.0) * (qtt1 + qtt2 + 9)/10 + (Board.distance(d1, d2)/1000.0) * (qtt2 + 9)/10;
        }
    }

    public Furgoneta clone() {
        return new Furgoneta(origin, d1, d2, qtt1, qtt2);
    }
    public String toString(boolean linux_tabs) {

        if (this.hasD2() || this.hasD1()) {

            StringBuilder ret = new StringBuilder();
            Estacion o = BicingState.getStations().get(this.origin);

            int d1x = -1, d1y = -1, d2x = -1, d2y = -1;

            if (this.hasD1()) {
                d1x = BicingState.getStations().get(this.d1).getCoordX();
                d1y = BicingState.getStations().get(this.d1).getCoordY();
            }

            if (this.hasD2()) {
                d2x = BicingState.getStations().get(this.d2).getCoordX();
                d2y = BicingState.getStations().get(this.d2).getCoordY();
            }

            ret.append(o.getCoordX());
            if (linux_tabs && o.getCoordX() < 1000) ret.append("\t");
            ret.append("\t");
            ret.append(o.getCoordY());
            if (linux_tabs && o.getCoordY() < 1000) ret.append("\t");
            ret.append("\t");
            ret.append(this.getTotal());
            if (linux_tabs && this.getTotal() < 1000) ret.append("\t");
            ret.append("\t");
            ret.append(d1x);
            if (linux_tabs && d1x < 1000) ret.append("\t");
            ret.append("\t");
            ret.append(d1y);
            if (linux_tabs && d1y < 1000) ret.append("\t");
            ret.append("\t");
            ret.append(this.qtt1);
            if (linux_tabs && this.qtt1 < 1000) ret.append("\t");
            ret.append("\t");
            ret.append(d2x);
            if (linux_tabs && d2x < 1000) ret.append("\t");
            ret.append("\t");
            ret.append(d2y);
            if (linux_tabs && d2y < 1000) ret.append("\t");
            ret.append("\t");
            ret.append(this.qtt2);
            if (linux_tabs && this.qtt2 < 1000) ret.append("\t");

            return ret.toString();
        }
        return null;
    }

}
