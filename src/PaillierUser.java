import java.math.BigInteger;
import java.util.Random;

public class PaillierUser {


	private BigInteger pk;
	private BigInteger pk2;
	public BigInteger r;
	public BigInteger s;
	private BigInteger X;
	private BigInteger Y;

	PaillierUser(BigInteger pk) {
		this.pk = pk;
		this.pk2 = pk.pow(2);
	}
	

    public BigInteger chiffrement(BigInteger value) {
        BigInteger alea = new BigInteger(1024, new Random()).mod(pk);
        //out.println("mon r1 : " + r.toString());
        BigInteger chiffre = BigInteger.ONE
        		.add(pk)
				.modPow(value, pk2) 
				.multiply(alea.modPow(pk, pk2))
				.mod(pk2);

        return chiffre;
    }
    
    
    public BigInteger[] ajouteAlea(BigInteger X, BigInteger Y) {
    	Random rnd = new Random();
    	this.r = new BigInteger(1024, rnd).mod(this.pk);
    	this.s = new BigInteger(1024, rnd).mod(this.pk);
    	this.X = X;
    	this.Y = Y;
    	

    	BigInteger XR = X.multiply(this.chiffrement(r)).mod(pk2);
    	BigInteger YS = Y.multiply(this.chiffrement(s)).mod(pk2);
    			
    	BigInteger[] response = {XR, YS};
    	return response;
    }
    
    public BigInteger retireAleas(BigInteger chiffre) {
    	BigInteger value = chiffre // (x+r)(y+s)
    			.multiply(X.modPow(s.negate(), pk2)) // - xs
    			.multiply(Y.modPow(r.negate(), pk2)) // - yr
    			.multiply(this.chiffrement(r).modPow(s.negate(), pk2)) // - rs
    			.mod(pk2);
    	
    	return value;
    }
	
}
