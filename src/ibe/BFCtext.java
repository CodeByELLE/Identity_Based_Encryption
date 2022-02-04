//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ibe;
import java.io.Serializable;
import uk.ac.ic.doc.jpair.pairing.Point;

public class BFCtext implements Serializable {
    final Point U;
    final byte[] V;
    final byte[] W;

    public BFCtext(Point u, byte[] v, byte[] w) {
        this.U = u;
        this.V = v;
        this.W = w;
    }

    public Point getU() {
        return this.U;
    }

    public byte[] getV() {
        return this.V;
    }

    public byte[] getW() {
        return this.W;
    }
}
