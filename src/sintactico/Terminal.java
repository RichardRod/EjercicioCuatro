package sintactico;

public class Terminal implements ElementoPila
{
    private int id;
    private String elemento;

    //constructor
    public Terminal(int id){
        this.id = id;
        elemento = "";
    }//fin del constructor

    public Terminal(int id, String elemento) {
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
}//fin de la clase Terminal