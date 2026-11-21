package Model;

public class Cliente {
    private int id;
    private String ruc;
    private String razonSocial;
    private String direccion;
    private String telefono;
    private String correo;
    private String estadoSunat;    
    private String condicionSunat;  

    public Cliente() {}

    public Cliente(int id, String ruc, String razonSocial, String direccion, String telefono, String correo) {
        this.id = id;
        this.ruc = ruc;
        this.razonSocial = razonSocial;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }

    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getEstadoSunat() { return estadoSunat; }
    public void setEstadoSunat(String estadoSunat) { this.estadoSunat = estadoSunat; }

    public String getCondicionSunat() { return condicionSunat; }
    public void setCondicionSunat(String condicionSunat) { this.condicionSunat = condicionSunat; }
}