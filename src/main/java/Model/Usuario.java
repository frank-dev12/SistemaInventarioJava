package Model;

public class Usuario {
    private int id;
    private String usuario;
    private String nombre;
    private String rol;
    private boolean activo;

    public Usuario(String usuario, String nombre, String rol) {
        this(0, usuario, nombre, rol, true);
    }

    public Usuario(int id, String usuario, String nombre, String rol, boolean activo) {
        this.id = id;
        this.usuario = usuario;
        this.nombre = nombre;
        this.rol = rol;
        this.activo = activo;
    }

    public int getId() { return id; }
    public String getUsuario() { return usuario; }
    public String getNombre() { return nombre; }
    public String getRol() { return rol; }
    public boolean isActivo() { return activo; }
}
