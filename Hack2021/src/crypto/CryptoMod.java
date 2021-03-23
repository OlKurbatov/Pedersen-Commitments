package crypto;

import crypto.commit.PCommitment;
import crypto.commit.PCommitment.Witness;
import crypto.commit.PCommitment.Commit;
import crypto.rand.Random;
import java.math.BigInteger;

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

    public static void main(String[] args) {

    }
}
