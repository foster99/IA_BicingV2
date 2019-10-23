package model;

public class Furgoneta {
    int origin;
    int d1;
    int d2;
    int qtt0;
    int qtt1;
    int qtt2;

    Furgoneta(int origin, int d1, int d2, int qtt1, int qtt2) {
        this.origin = origin;
        this.d1 = d1;
        this.d2 = d2;
        this.qtt1 = qtt1;
        this.qtt2 = qtt2;
    }

    int getTotal(){
        return qtt1+qtt2;
    }

    boolean full() {
        return qtt1 + qtt2 == 30;
    }
    boolean hasD1() {
        return d1 != -1;
    }

    boolean hasD2() {
        return  d2 != -1;
    }

    boolean hasOrigin() {
        return  origin != -1;
    }

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

    double totalDistance() {
        if (!this.hasOrigin()) return 0;
        if (!this.hasD1()) {
            if (this.hasD2()) return BicingProblem.distance(origin, d2)/1000.0;
            return 0;
        } else {
            if (!this.hasD2()) return BicingProblem.distance(origin, d1)/1000.0;
            return BicingProblem.distance(origin, d1)/1000.0 + BicingProblem.distance(d1, d2)/1000.0;
        }
    }

    double costeRecorrido() {
        if (!this.hasOrigin()) return 0;
        if (!this.hasD1()) {
            if (this.hasD2()) return (BicingProblem.distance(origin, d2)/1000.0) * (qtt2 + 9)/10;
            return 0;
        } else {
            if (!this.hasD2()) return (BicingProblem.distance(origin, d1)/1000.0) * (qtt1 + 9)/10;
            return (BicingProblem.distance(origin, d1)/1000.0) * (qtt1 + qtt2 + 9)/10 + (BicingProblem.distance(d1, d2)/1000.0) * (qtt2 + 9)/10;
        }
    }

    public Furgoneta clone() {
        return new Furgoneta(origin, d1, d2, qtt1, qtt2);
    }

}
