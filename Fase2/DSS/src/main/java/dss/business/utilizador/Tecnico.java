package dss.business.utilizador;

public class Tecnico extends Utilizador {
    private boolean ocupado;

    public Tecnico(String nome, String id, String password) {
        super(nome, id, password);
        this.ocupado = false;
    }

    public boolean estaOcupado(){
        return ocupado;
    }

    public void ocupaTecnico() {
        ocupado = true;
    }

    public  void libertaTecnico() {
        ocupado = false;
    }

}
