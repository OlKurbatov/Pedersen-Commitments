package crypto.commit;

import crypto.ecc.EC;
import java.math.BigInteger;
import java.security.spec.ECPoint;

public class PCommitment {

    public static class Commit {
        private ECPoint commitment;

        public Commit(ECPoint point){
            this.setCommitment(point);
        }

        public ECPoint getCommitment() {
            return commitment;
        }

        public void setCommitment(ECPoint commitment) {
            this.commitment = commitment;
        }

        public static void printCommitment(Commit commit){
            System.out.println("Commitment: ");
            EC.Points.printEPoint(commit.getCommitment());
        }

        public static String CommitmentToString(Commit commit){
            return EC.Points.EPointToString(commit.getCommitment());
        }
    }

    public static class Witness {
        private BigInteger vote;
        private BigInteger nonce;

        public BigInteger getNonce() {
            return nonce;
        }

        public BigInteger getVote() {
            return vote;
        }

        public void setNonce(BigInteger nonce) {
            this.nonce = nonce;
        }

        public void setVote(BigInteger vote) {
            this.vote = vote;
        }

        public Witness (BigInteger vote, BigInteger nonce) {
            this.vote = vote;
            this.nonce = nonce;
        }

        public static void printWitness(Witness witness){
            System.out.println("Witness: ");
            System.out.println("Vote: \t" + witness.getVote());
            System.out.println("Nonce: \t" + witness.getNonce());
        }

        public static String WitnessToString(Witness witness){
            return witness.getVote() + " " + witness.getNonce();
        }

    }

    public static Commit genCommitment (Witness witness){
        return new Commit(EC.Points.addPoint(EC.Points.scalmult(EC.Constants.G, witness.getVote()),EC.Points.scalmult(EC.Constants.H, witness.getNonce())));
    }

    public static boolean verCommitment (Commit commitment, Witness witness){
        ECPoint point = EC.Points.addPoint(EC.Points.scalmult(EC.Constants.G, witness.getVote()),EC.Points.scalmult(EC.Constants.H, witness.getNonce()));
        if (point.getAffineX().equals(commitment.getCommitment().getAffineX()) && point.getAffineY().equals(commitment.getCommitment().getAffineY()))
            return true;
        else return false;
    }
}
