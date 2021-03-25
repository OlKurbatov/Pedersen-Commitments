package crypto;

import crypto.commit.PCommitment;
import crypto.commit.PCommitment.Witness;
import crypto.commit.PCommitment.Commit;
import crypto.ecc.EC;
import crypto.rand.Random;
import java.math.BigInteger;
import java.security.spec.ECPoint;

public class CryptoMod {

    public static BigInteger generateRandomNonce(){   //Nonce generation
        return Random.randomGen();
    }

    public static Witness generateWitness(BigInteger vote, BigInteger nonce){   //Witness generation
        return new Witness(vote, nonce);
    }

    public static Commit generateCommitment(Witness witness){   //Commitment generation
        return PCommitment.genCommitment(witness);
    }

    public static boolean verifyCommitment(Commit commitment, Witness witness){   //Commitment verification
        return PCommitment.verCommitment(commitment, witness);
    }

    public  static void genH(){
        EC.regenH();
    }

    public static void main(String[] args) {
        BigInteger nonce = generateRandomNonce();
        BigInteger vote = new BigInteger("12", 10);
        Witness witness = generateWitness(vote, nonce);
        Commit commit = generateCommitment(witness);
        System.out.println(verifyCommitment(commit, witness));
    }
}
