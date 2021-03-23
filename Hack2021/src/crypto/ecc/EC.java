package crypto.ecc;

import java.math.BigInteger;
import java.security.spec.ECPoint;

public class EC {

    public static class Constants {

        public static BigInteger TWO = new BigInteger("2");
        public static BigInteger THREE = new BigInteger("3");

        // m is our prime field
        public static BigInteger m = new BigInteger("fffffffffffffffffffffffffffffffeffffffffffffffff", 16);
        public static BigInteger a = new BigInteger("fffffffffffffffffffffffffffffffefffffffffffffffc", 16);

        public static BigInteger gx = new BigInteger("188da80eb03090f67cbf20eb43a18800f4ff0afd82ff1012", 16);
        public static BigInteger gy = new BigInteger("07192b95ffc8da78631011ed6b24cdd573f977a11e794811", 16);

        public static ECPoint G = new ECPoint(gx, gy); // generator G

        public static BigInteger hx = new BigInteger("ff68aac45cbf5b877b6bc8800d423cf1aaa399a7d4c28ffa", 16);
        public static BigInteger hy = new BigInteger("cb198b1f8424169290be684df926620dd2790d02f52c6274", 16);

        public static ECPoint H = new ECPoint(gx, gy); // generator H
    }

    public static class Modular {

        // return a - b % m
        public static BigInteger modSub(BigInteger a, BigInteger b) {
            return a.subtract(b).mod(Constants.m);
        }

        // return a + b % m
        public static BigInteger modAdd(BigInteger a, BigInteger b) {
            return a.add(b).mod(Constants.m);
        }

        // return a * b % m
        public static BigInteger modMul(BigInteger a, BigInteger b) {
            return a.multiply(b).mod(Constants.m);
        }

        // return a ^ -1 % m
        public static BigInteger modInv(BigInteger a) {
            return a.modInverse(Constants.m);
        }

        // return a ^ e % m
        public static BigInteger modPow(BigInteger a, BigInteger e) {
            return a.modPow(e, Constants.m);
        }
    }

    public static class Points
    {
        // add points
        public static ECPoint addPoint(ECPoint p, ECPoint q) {

            BigInteger rx, ry, s, t1, t2;
            BigInteger px, py, qy, qx;

            px = p.getAffineX(); py = p.getAffineY();
            qx = q.getAffineX(); qy = q.getAffineY();

            if (p.equals(q)) {
                return doublePoint(p);
            } else if (p.equals(ECPoint.POINT_INFINITY)) {
                return q;
            } else if (q.equals(ECPoint.POINT_INFINITY)) {
                return p;
            }

            // t1 = py - qy
            t1 = Modular.modSub(py, qy);

            // t2 = px - qx
            t2 = Modular.modSub(px, qx);

            // s = (t1 * (t2 ^ -1))
            s = Modular.modMul(t1, Modular.modInv(t2));

            // rx = (((s ^ 2) - px) - qx)
            rx = Modular.modSub(Modular.modSub(Modular.modPow(s, Constants.TWO), px), qx);

            // ry = (s * (px - rx)) - py
            ry = Modular.modSub(Modular.modMul(s, Modular.modSub(px, rx)), py);

            return new ECPoint(rx, ry);
        }

        // double point
        public static ECPoint doublePoint(ECPoint p) {
            BigInteger rx, ry, s;
            BigInteger px, py;

            px = p.getAffineX(); py = p.getAffineY();

            if (p.equals(ECPoint.POINT_INFINITY)) {
                return p;
            }

            // s = ((px ^ 2) * 3) + a
            s = Modular.modAdd(Modular.modMul(Modular.modPow(px, Constants.TWO), Constants.THREE), Constants.a);

            // s = s * ((py * 2) ^ -1)
            s = Modular.modMul(s, Modular.modInv(Modular.modMul(py, Constants.TWO)));

            // rx = (s ^ 2) - 2px
            rx = Modular.modSub(Modular.modPow(s, Constants.TWO), Modular.modMul(px, Constants.TWO));

            // ry = s * (px - rx) - py
            ry = Modular.modSub(Modular.modMul(s, Modular.modSub(px, rx)), py);

            return new ECPoint(rx, ry);
        }

        // R = k * A
        public static ECPoint scalmult(ECPoint A, BigInteger kin){
            ECPoint    R = ECPoint.POINT_INFINITY, N = A;
            BigInteger k = kin.mod(Constants.m);
            int        i, bitLength = k.bitLength();

            for (i=0; i<bitLength; i++){
                if (k.testBit(i)) {
                    R = addPoint(R, N);
                }
                N = doublePoint(N);
            }
            return R;
        }
        public static void printEPoint (ECPoint point){
            System.out.println("X: " + point.getAffineX().toString(16) + "\n" +
                    "Y: " + point.getAffineY().toString(16));
        }
        public static String EPointToString(ECPoint point){
            return "X: " + point.getAffineX().toString(16) + "\n" +
                    "Y: " + point.getAffineY().toString(16);
        }
    }
}