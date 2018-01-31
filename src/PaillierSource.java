import java.math.BigInteger;
import java.util.Random;

public class PaillierSource {

	private BigInteger pk;
	private BigInteger sk;
	private BigInteger pk2;

	PaillierSource() {
		Random rnd = new Random();
		
		BigInteger p =  BigInteger.probablePrime(512, rnd);
        BigInteger q =  BigInteger.probablePrime(512, rnd);

        this.pk = p.multiply(q);
        this.pk2 = this.pk.pow(2);
        this.sk = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
	}

	public BigInteger getPk() {
		return this.pk;
	}
	
	

    public BigInteger chiffrement(BigInteger value) {
        BigInteger r = new BigInteger(1024, new Random()).mod(pk);
        //out.println("mon r1 : " + r.toString());
        BigInteger chiffre = BigInteger.ONE
        		.add(pk)
				.modPow(value, pk2) 
				.multiply(r.modPow(pk, pk2))
				.mod(pk2);

        return chiffre;
    }

    public BigInteger dechiffrement(BigInteger chiffre) {
        BigInteger r = chiffre.modPow(
    		pk.modPow(
				BigInteger.ZERO.subtract(BigInteger.ONE), // -1
				sk), 
    		pk
    		);
        BigInteger message = chiffre
        		.multiply(r.modPow(pk.negate(), pk2))
        		.mod(pk2)
        		.subtract(BigInteger.ONE)
        		.divide(pk);
        //out.println("mon r2 : " + r.toString());

        return message;
    }
    

    public BigInteger[] dechiffrementPlus(BigInteger chiffre) {
        BigInteger r = chiffre.modPow(
    		pk.modPow(
				BigInteger.ZERO.subtract(BigInteger.ONE), // -1
				sk), 
    		pk
    		);
        BigInteger message = chiffre
        		.multiply(r.modPow(pk.negate(), pk2))
        		.mod(pk2)
        		.subtract(BigInteger.ONE)
        		.divide(pk);
        
        BigInteger[] response = {message, r};
        return response;
    }
    
    
    public BigInteger multiplier(BigInteger X, BigInteger Y) {
    	 BigInteger produit = this.dechiffrement(X).multiply(this.dechiffrement(Y));
    	 return this.chiffrement(produit);
    }
    
    
}
