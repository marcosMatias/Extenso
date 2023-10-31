
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

public class Extenso {

    private ArrayList nro;
    private BigInteger num;
    private String[][] Qualificadores = {
        {"centavo", "centavos"}, {"", ""}, {"mil", "mil"}, {"milhão", "milhões"}, {"bilhão", "bilhões"}, {"trilhão", "trilhões"}, {"quatrilhão", "quatrilhões"}, {"quintilhão", "quintilhões"}, {"sextilhão", "sextilhões"}, {"septilhão", "septilhões"}};

    private String[][] Numeros = {{"zero", "um", "dois", "três", "quatro", "cinco", "seis", "sete", "oito", "nove", "dez", "onze", "doze", "treze", "quatorze", "quinze", "desesseis", "desessete", "dezoito", "desenove"}, {"vinte", "trinta", "quarenta", "cinquenta", "sessenta", "setenta", "oitenta", "noventa"}, {"cem", "cento", "duzentos", "trezentos", "quatrocentos", "quinhentos", "seiscentos", "setecentos", "oitocentos", "novecentos"}};

    public Extenso() {
        this.nro = new ArrayList();
    }

    public Extenso(BigDecimal paramBigDecimal) {
        this();
        setNumber(paramBigDecimal);
    }

    public Extenso(double paramDouble) {
        this();
        setNumber(paramDouble);
    }

    public void setNumber(BigDecimal paramBigDecimal) {
        this.num = paramBigDecimal.setScale(2, 4).multiply(BigDecimal.valueOf(100L)).toBigInteger();

        this.nro.clear();
        if (this.num.equals(BigInteger.ZERO)) {
            this.nro.add(new Integer(0));

            this.nro.add(new Integer(0));
        } else {
            addRemainder(100);

            while (!this.num.equals(BigInteger.ZERO)) {
                addRemainder(1000);
            }
        }
    }

    public void setNumber(double paramDouble) {
        setNumber(new BigDecimal(paramDouble));
    }

    public void show() {
        Iterator localIterator = this.nro.iterator();

        while (localIterator.hasNext()) {
            System.out.println(((Integer) localIterator.next()).intValue());
        }
        System.out.println(toString());
    }

    public String toString() {
        StringBuffer localStringBuffer = new StringBuffer();

        int i = ((Integer) this.nro.get(0)).intValue();

        for (int j = this.nro.size() - 1; j > 0; j--) {
            if ((localStringBuffer.length() > 0) && (!ehGrupoZero(j))) {
                localStringBuffer.append(" e ");
            }
            localStringBuffer.append(numToString(((Integer) this.nro.get(j)).intValue(), j));
        }
        if (localStringBuffer.length() > 0) {
            if (ehUnicoGrupo()) {
                localStringBuffer.append(" de ");
            }
            while (localStringBuffer.toString().endsWith(" ")) {
                localStringBuffer.setLength(localStringBuffer.length() - 1);
            }
            if (((Integer) this.nro.get(0)).intValue() != 0) {
                localStringBuffer.append(" e ");
            }
        }
        if (((Integer) this.nro.get(0)).intValue() != 0) {
            localStringBuffer.append(numToString(((Integer) this.nro.get(0)).intValue(), 0));
        }
        if (localStringBuffer.toString().equals(Numeros[0][1])) {
            localStringBuffer.append(" real");
        } else {
            localStringBuffer.append(" reais");
        }
        return localStringBuffer.toString();
    }

    private boolean ehPrimeiroGrupoUm() {
        return ((Integer) this.nro.get(this.nro.size() - 1)).intValue() == 1;
    }

    private void addRemainder(int paramInt) {
        BigInteger[] arrayOfBigInteger = this.num.divideAndRemainder(BigInteger.valueOf(paramInt));

        this.nro.add(new Integer(arrayOfBigInteger[1].intValue()));

        this.num = arrayOfBigInteger[0];
    }

    private boolean temMaisGrupos(int paramInt) {
        for (; paramInt > 0; paramInt--) {
            if (((Integer) this.nro.get(paramInt)).intValue() != 0) {
                return true;
            }
        }

        return false;
    }

    private boolean ehUltimoGrupo(int paramInt) {
        return (paramInt > 0) && (((Integer) this.nro.get(paramInt)).intValue() != 0) && (!temMaisGrupos(paramInt - 1));
    }

    private boolean ehUnicoGrupo() {
        if (this.nro.size() <= 3) {
            return false;
        }
        if ((!ehGrupoZero(1)) && (!ehGrupoZero(2))) {
            return false;
        }
        int i = 0;
        for (int j = 3; j < this.nro.size(); j++) {
            if (((Integer) this.nro.get(j)).intValue() != 0) {
                if (i != 0) {
                    return false;
                }
                i = 1;
            }
        }
        return true;
    }

    boolean ehGrupoZero(int paramInt) {
        if ((paramInt <= 0) || (paramInt >= this.nro.size())) {
            return true;
        }
        return ((Integer) this.nro.get(paramInt)).intValue() == 0;
    }

    private String numToString(int paramInt1, int paramInt2) {
        int i = paramInt1 % 10;
        int j = paramInt1 % 100;
        int k = paramInt1 / 100;
        StringBuffer localStringBuffer = new StringBuffer();

        if (paramInt1 != 0) {
            if (k != 0) {
                if ((j == 0) && (k == 1)) {
                    localStringBuffer.append(this.Numeros[2][0]);
                } else {
                    localStringBuffer.append(this.Numeros[2][k]);
                }
            }

            if ((localStringBuffer.length() > 0) && (j != 0)) {
                localStringBuffer.append(" e ");
            }
            if (j > 19) {
                j /= 10;
                localStringBuffer.append(this.Numeros[1][(j - 2)]);
                if (i != 0) {
                    localStringBuffer.append(" e ");
                    localStringBuffer.append(this.Numeros[0][i]);
                }
            } else if ((k == 0) || (j != 0)) {
                localStringBuffer.append(this.Numeros[0][j]);
            }

            localStringBuffer.append(" ");
            if (paramInt1 == 1) {
                localStringBuffer.append(this.Qualificadores[paramInt2][0]);
            } else {
                localStringBuffer.append(this.Qualificadores[paramInt2][1]);
            }
        }

        return localStringBuffer.toString();
    }

    public static void main(String[] paramArrayOfString) {
        if (paramArrayOfString.length == 0) {
            System.out.println("Sintax : ...Extenso <numero>");
            return;
        }
        Extenso localExtenso = new Extenso(new BigDecimal(paramArrayOfString[0]));
        System.out.println(localExtenso.toString());
    }
}
