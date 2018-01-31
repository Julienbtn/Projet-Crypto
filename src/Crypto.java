/**
 * Created by Julien on 21/12/2017.
 */

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.System.out;

public class Crypto {

    public static void main(String[] args)
    {
        PaillierSource alice = new PaillierSource();
        PaillierUser bob = new PaillierUser(alice.getPk());
        
        Random rnd = new Random();
        
        for (int i=0; i<100; i++) {

        	BigInteger x = new BigInteger(1024, rnd).mod(alice.getPk());
        	BigInteger y = new BigInteger(1024, rnd).mod(alice.getPk());
        	BigInteger X = alice.chiffrement(x);
        	BigInteger Y = alice.chiffrement(y);
        	
        	BigInteger attendu = x.multiply(y).mod(alice.getPk());
        	
        	BigInteger[] ajoutes = bob.ajouteAlea(X, Y);
        	
        	
        	BigInteger trouve = alice.dechiffrement(
    			bob.retireAleas(
					alice.multiplier(ajoutes[0], ajoutes[1])
				)
			);

        	System.out.println(String.format("cas %d : %b", i, trouve.equals(attendu)));
        	//System.out.println(String.format("attendu\t%s \ntrouve\t%s\n", attendu, trouve));
        }
}

    public static BigInteger chiffrement(BigInteger m, BigInteger n)
    {
        BigInteger r = new BigInteger(1024,new Random());
        r = r.mod (n);
        //out.println("mon r1 : " + r.toString());
        BigInteger c = ((BigInteger.ONE.add(n)).modPow(m,n.pow(2))).multiply(r.modPow(n,n.pow(2)));

        return c;
    }

    public static BigInteger dechiffrement(BigInteger c, BigInteger phi_n, BigInteger n)
    {
        BigInteger r = c.modPow(n.modPow(BigInteger.ZERO.subtract(BigInteger.ONE),phi_n),n);
        BigInteger message = (((c.multiply(r.modPow(n.negate(),n.pow(2))).mod(n.pow(2)))).subtract(BigInteger.ONE)).divide(n);
        //out.println("mon r2 : " + r.toString());

        return message;
    }

    public static List<BigInteger> dechiffrementPlus(BigInteger c, BigInteger phi_n, BigInteger n)
    {
        BigInteger r = c.modPow(n.modPow(BigInteger.ZERO.subtract(BigInteger.ONE),phi_n),n);
        BigInteger message = (((c.multiply(r.modPow(n.negate(),n.pow(2))).mod(n.pow(2)))).subtract(BigInteger.ONE)).divide(n);
        //out.println("mon r2 : " + r.toString());
        List retour = new ArrayList<BigInteger>();
        retour.add(message);
        retour.add(r);

        return retour;
    }

    // return n, e, d
    public static BigInteger[] KeyGen(int nBits)
    {
        BigInteger p = new BigInteger(nBits, Integer.MAX_VALUE, new Random());
        BigInteger q = new BigInteger(nBits, Integer.MAX_VALUE, new Random());
        BigInteger n = p.multiply(q);
        BigInteger phi_n = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        BigInteger e = new BigInteger(16, Integer.MAX_VALUE, new Random());
        BigInteger d = e.modInverse(phi_n);

        BigInteger[] tab = new BigInteger[3];
        tab[0] = n;
        tab[1] = e;
        tab[2] = d;

        return tab;
    }

    public static BigInteger Encrypt(BigInteger x, BigInteger e, BigInteger n)
    {
        return x.modPow(e, n);
    }

    public static BigInteger Decrypt(BigInteger x, BigInteger d, BigInteger n)
    {
        return x.modPow(d, n);
    }

    public static void listeElementZn(int n)
    {
        for(int i = 1 ; i < n ; i++)
        {
            for(int j = 1 ; j < n ; j++)
            {
                if(i == (j*j)%n)
                {
                    out.print(i+" ");
                    break;
                }
            }
        }
        out.print("\n \n");
    }

    public static void elementsInversiblesZn(int n)
    {
        for(int i = 1 ; i < n ; i++)
        {
            for(int j = 1 ; j < n ; j++)
            {
                if((i*j)%n == 1)
                {
                    out.print(i+" ");
                    break;
                }
            }
        }
        out.print("\n \n");
    }

    public static boolean testPrimalite(BigInteger n)
    {
        for(BigInteger i = BigInteger.valueOf(2) ; i.compareTo(n) == -1 ; i = i.add(BigInteger.ONE))
        {
            if(n.mod(i).equals(BigInteger.ZERO)) return false;
        }
        return true;
    }

    public static boolean testFermat(BigInteger nMax)
    {
        return BigInteger.valueOf(2).modPow(nMax.subtract(BigInteger.valueOf(1)), nMax).equals(BigInteger.ONE);
    }

    public static void erreurFermat(int n)
    {
        int count = 0 ;
        for(BigInteger i = BigInteger.valueOf(2) ; i.compareTo(BigInteger.valueOf(n)) == -1 ; i = i.add(BigInteger.ONE))
        {
            if(testPrimalite(i) != testFermat(i))
            {
                out.println(i.toString());
                count++;
            }
        }
        out.println("nb erreurs : "+count);
    }


}
