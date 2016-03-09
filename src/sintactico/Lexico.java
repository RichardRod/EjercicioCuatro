package sintactico;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Stack;

public class Lexico {

    class TipoSimbolo {
        public static final int ERROR = -1;
        public static final int IDENTIFICADOR = 0;
        public static final int OPADIC = 1; //10
        //public static final int OPMULT = 2;
        public static final int PESOS = 2; //1
        public static final int ENTERO = 4; //4
    }//fin de la clase Simbolo

    //atributos
    private String fuente;
    private int ind;
    private boolean continua;
    private char c;
    private int estado;
    public String simbolo;
    public int tipo;


    private int[][] tabla = new int[95][46];
    public int idReglas[] = new int[52];
    public int lonReglas[] = new int[52];
    public String strReglas[] = new String[52];



    public Lexico(String fuente) {
        this.fuente = fuente;
        ind = 0;
        leerArchivo();
    }

    public Lexico() {
        ind = 0;
        leerArchivo();
    }

    public void entrada(String fuente) {
        ind = 0;
        this.fuente = fuente;
    }

    public String tipoAcad(int tipo) {

        String cad = "";

        switch (tipo) {
            case TipoSimbolo.IDENTIFICADOR:
                cad = "Identificador";
                break;

            /*case TipoSimbolo.OPADIC:
                cad = "Op. Adicion";
                break;
           case TipoSimbolo.OPMULT:
                cad= "Op. Multiplicacion";
                break;*/
            case TipoSimbolo.ENTERO:
                cad = "Entero";
                break;

            case TipoSimbolo.PESOS:
                cad = "Fin de la Entrada";
                break;


        }

        return cad;

    }

    public int sigSimbolo() {
        estado = 0;
        continua = true;
        simbolo = "";

        //Inicio del Automata
        while (continua) {
            c = sigCaracter();

            if (esDigito(c)) aceptacion(1);
            else if (c == '+' || c == '-') aceptacion(2);
            else if (esLetra(c)) aceptacion(3);
            else if (c == '$') aceptacion(4);
            break;


        }
        //Fin del Automata

        switch (estado) {

            case 1:
                tipo = TipoSimbolo.ENTERO;
                break;
            case 2:
                tipo = TipoSimbolo.OPADIC;
                break;

            case 3:
                tipo = TipoSimbolo.IDENTIFICADOR;
                break;

            case 4:
                tipo = TipoSimbolo.PESOS;
                break;

            default:
                tipo = TipoSimbolo.ERROR;
        }

        return tipo;
    }

    public boolean terminado() {
        return ind >= fuente.length();
    }

    private char sigCaracter() {
        if (terminado()) return '$';

        return fuente.charAt(ind++);
    }

    private void sigEstado(int estado) {
        this.estado = estado;
        simbolo += c;
    }

    private void aceptacion(int estado) {
        sigEstado(estado);
        continua = false;
    }

    private boolean esLetra(char c) {
        return Character.isLetter(c) || c == '_';
    }

    private boolean esDigito(char c) {
        return Character.isDigit(c);
    }

    private boolean esEspacio(char c) {
        return c == ' ' || c == '\t';
    }

    private boolean esSimboloFinalCadena(char c) {
        return c == ' ' || c == '+' || c == '-' || c == '$';
    }

    private void retroceso() {
        if (c != '$') ind--;
        continua = false;
    }

    public void ejercicioCuatro()
    {
        Stack<ElementoPila> pila = new Stack<>();
        int fila, columna, accion = 0;
        boolean aceptacion = false;
        Lexico lexico = new Lexico("int main;");

        pila.push(new Terminal(TipoSimbolo.PESOS, "$"));
        pila.push(new Estado(0));
        while (true)
        {
            lexico.sigSimbolo();

            fila = pila.peek().getId();
            columna = lexico.tipo;
            accion = tabla[fila][columna];

            ElementoPila[] elementos = new ElementoPila[pila.size()];
            String elementosEnPila = "";
            for(int i = 0; i < elementos.length; i++){
                elementos[i] = pila.get(i);
            }

            for(int i = elementos.length - 1; i >= 0; i--){
                elementosEnPila += elementos[i].getElemento();
            }

            System.out.println(elementosEnPila + " - " + lexico.simbolo + " - " + accion);


            if(accion > 0)
            {
                pila.push(new Terminal(lexico.tipo, lexico.simbolo));
                pila.push(new Estado(accion));
            }//fin de if
            else if(accion < 0)
            {
                if(accion == -1)
                {
                    fila = pila.peek().getId();
                    columna = lexico.tipo;
                    accion = tabla[fila][columna];
                    System.out.println("Aceptacion");
                    break;
                }//fin de if

                int posicionReduccion = (accion * -1) -2;

                for(int i = 0; i < lonReglas[posicionReduccion] * 2; i++){
                    pila.pop();
                }

                fila = pila.peek().getId();
                columna = idReglas[posicionReduccion];
                accion = tabla[fila][columna];
                pila.push(new NoTerminal(idReglas[posicionReduccion], strReglas[posicionReduccion]));
                pila.push(new Estado(accion));
            }//fin de else if

            if(accion == 0){
                return;
            }

        }//fin de while


    }//fin del metodo ejercicio

    private void leerArchivo(){

        try
        {
            FileReader fr = new FileReader("compilador.lr");
            BufferedReader br = new BufferedReader(fr);
            String linea = "";
            int contadorLinea = 0;

            while((linea = br.readLine()) != null)
            {
                String arreglo[] = linea.split("\\s+");


                System.out.println("Linea: " + contadorLinea);

                for(int i = 0; i < arreglo.length && contadorLinea < 53; i++){
                    //System.out.println("Pos: " + i + " - " + arreglo[i]);
                    if(contadorLinea > 0)
                    {
                        if(i == 0)
                        {
                            idReglas[contadorLinea - 1] = Integer.valueOf(arreglo[i]);
                        }
                        if(i == 1)
                        {
                            lonReglas[contadorLinea - 1] = Integer.valueOf(arreglo[i]);
                        }
                        if(i == 2)
                        {
                            strReglas[contadorLinea - 1] = arreglo[i];
                        }

                    }//fin de if
                }//fin de for

                for(int i = 0; i < arreglo.length; i++){
                    if(contadorLinea > 53)
                    {
                        //System.out.println("Pos: " + i + " - " + arreglo[i]);
                        tabla[contadorLinea - 54][i] = Integer.valueOf(arreglo[i]);
                    }

                }




                contadorLinea++;


            }//fin de while

            /*for(int i = 0; i < strReglas.length; i++){
                System.out.println(strReglas[i]);
            }*/

            for(int i = 0; i < 95; i++)
            {
                for(int j = 0; j < 46; j++){
                    System.out.print(tabla[i][j]);
                }
                System.out.println();
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }//fin de metodo leerArchivo


}//fin de la clase Lexico