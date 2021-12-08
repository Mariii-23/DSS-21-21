package dss.utilizador;

public abstract class Utilizador {
    private String nome;
    private String id;
    private String passwordHash;

    public Utilizador(String nome, String id, String password) {
        this.nome = nome;
        this.id = id;
        this.passwordHash = UtilizadorDAO.encriptaPassword(password);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

}
