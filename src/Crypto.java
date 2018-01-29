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
        //listeElementZn(53);
        //listeElementZn(55);

        //elementsInversiblesZn(53);
        //elementsInversiblesZn(55);

        //System.out.print(testPrimalite(BigInteger.valueOf(53)));
        //System.out.print(testFermat(BigInteger.valueOf(53)));

        //erreurFermat(20000);

        /*// 5)
        BigInteger p = new BigInteger(512, Integer.MAX_VALUE, new Random());
        BigInteger q = new BigInteger(512, Integer.MAX_VALUE, new Random());
        System.out.println("p = " + p);
        System.out.println("q = " + q);

        // 6)
        BigInteger n = p.multiply(q);
        BigInteger phi_n = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        System.out.println("n = " + n);
        System.out.println("phi_n = " + phi_n);

        // 7)
        BigInteger e = new BigInteger(16, Integer.MAX_VALUE, new Random());
        System.out.println("e = " + e);

        // 8)
        BigInteger d = e.modInverse(phi_n);
        System.out.println("d = " + d);
        System.out.println("e*d = " + e.multiply(d).mod(phi_n));

        // 9)
        BigInteger r = new BigInteger(512, new Random());
        BigInteger X = r.modPow(e, n);
        BigInteger Xd = X.modPow(d, n);
        System.out.println("r  = " + r);
        System.out.println("X = " + X);
        System.out.println("Xd = " + Xd);*/

        /*BigInteger[] tab;
        tab = KeyGen(512);

        BigInteger r = new BigInteger(512, new Random());
        System.out.println("r  = " + r);
        BigInteger X = Encrypt(r, tab[1], tab[0]);
        X = Decrypt(X, tab[2], tab[0]);
        System.out.println("X  = " + X);*/

        /*BigInteger p=  BigInteger.probablePrime(512,new Random());
        BigInteger q=  BigInteger.probablePrime(512,new Random());

        BigInteger n = p.multiply(q);
        BigInteger phi_n = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        BigInteger m = new BigInteger(1024,new Random());
        m = m.mod (n);
        System.out.println("mon message : " + m.toString());
        BigInteger c = chiffrement(m,n);
        System.out.println("message crypté : " + c.toString());
        BigInteger message = dechiffrement(c,phi_n,n);
        System.out.println("mon message : " + message.toString());*/

        BigInteger x = new BigInteger(512,new Random());
        BigInteger y = new BigInteger(512,new Random());

        out.println("x = "+ x + "\ny = " + y + "\nx*y = " +x.multiply(y));

        BigInteger p=  BigInteger.probablePrime(256,new Random());
        BigInteger q=  BigInteger.probablePrime(256,new Random());

        BigInteger n = p.multiply(q);
        BigInteger phi_n = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        BigInteger X = chiffrement(x,n);
        BigInteger Y = chiffrement(y,n);
// bob
        BigInteger r = new BigInteger(512,new Random());
        BigInteger s = new BigInteger(512,new Random());

        BigInteger R = chiffrement(r,n);
        BigInteger S = chiffrement(s,n);

        BigInteger X1 = X.multiply(R).modPow(n,new BigInteger("2"));
        BigInteger Y1 = Y.multiply(S).modPow(n,new BigInteger("2"));
//Alice

        //BigInteger X1 = chiffrement(X.add(R),n);
        //BigInteger Y1 = chiffrement(Y.add(S),n);

        out.println("X1 = "+ X1 + "\nx1 = " + dechiffrement(X1,phi_n,n) + "\nr+x = " + (r.add(x)));

        BigInteger x1 = dechiffrement(X1,phi_n,n);
        BigInteger y1 = dechiffrement(Y1,phi_n,n);

        BigInteger res = chiffrement(x1.multiply(y1),n);

        out.println("x1 : " + x1 + "\n" + "y1 : " + y1 );

        out.println("X+R * Y+S = " + dechiffrement(res, phi_n,n));

        BigInteger Z = res.add(s.multiply(X).negate()).add(r.multiply(Y).negate()).add(r.multiply(S).negate());

        out.println("Z :" + Z);

        out.println("Decrypt :" + dechiffrement(Z,phi_n,n));
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
