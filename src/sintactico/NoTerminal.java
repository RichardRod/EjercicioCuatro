package sintactico;

public class NoTerminal implements ElementoPila
{
    private int id;
    private String elemento;

    //constructor
    public NoTerminal(int id, String elemento) {
        this.id = id;
        this.elemento = elemento;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getElemento() {
        return elemento;
    }
}//fin de la clase NoTerminal