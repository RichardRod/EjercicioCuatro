package sintactico;


public class Estado implements ElementoPila
{
    private int id;

    //constructor
    public Estado(int id) {
        this.id = id;
    }//fin del constrcutor


    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getElemento() {
        return String.valueOf(id);
    }
}//fin de la clase Estado