package Model;

public class Categoria {
    private int id;
    private String nombre;
    private int cantidadProductos;

    public Categoria(int id, String nombre, int cantidadProductos) {
        this.id = id;
        this.nombre = nombre;
        this.cantidadProductos = cantidadProductos;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public int getCantidadProductos() { return cantidadProductos; }

    @Override
    public String toString() {
        return nombre;
    }
}
