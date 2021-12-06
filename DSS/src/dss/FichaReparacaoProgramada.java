package dss;

import java.util.List;

public class FichaReparacaoProgramada extends FichaReparacao {
    Equipamento equipamentoAReparar;
    PlanoReparacao planoReparacao;
    Orcamento orcamento;
    // pausado indica se esta a ser reparado neste preciso momento ou nao
    boolean pausado ;
    Fase fase;

    public FichaReparacaoProgramada(String idCliente, String funcionarioCriador) {
        super(idCliente, funcionarioCriador);
        this.equipamentoAReparar = new Equipamento(idCliente);
        pausado = true;
        this.orcamento = null;
        fase = Fase.AEsperaOrcamento;
    }

    @Override
    public boolean efetuaReparacao(String id, int custoReal, int tempoReal) {
        if(!tecnicosQueRepararam.contains(id))
            tecnicosQueRepararam.add(id);
        return this.planoReparacao.repara(custoReal, tempoReal);
    }

    public void realizaOrcamento(String id) {
        this.tecnicosQueRepararam.add(id);
        this.orcamento = new Orcamento(planoReparacao);
    }

    public void notificaOrcamento() {
        //enviarEmailAoCliente();
        this.fase = Fase.AEsperaResposta;
    }

    public void aprovaOrcamento() {
        this.fase = Fase.EmReparacao;
    }

    public void rejeitaOrcamento() {
        this.fase = Fase.Recusada;
    }

    public boolean ultrapassaOrcamento() {
        boolean ultrapassa = false;
        if (this.orcamento != null) {
            Orcamento orcamentoReal = new Orcamento(planoReparacao);
            //Não pode exceder orçamento aprovado por 120%
            if (orcamentoReal.getPreco() > this.orcamento.getPreco()*1.2)
                ultrapassa = true;
        }
        return ultrapassa;
    }

    public void togglePausarReparacao() {
        pausado = !pausado;
    }

    public boolean podeSerReparadaAgora() {
        return fase.equals(Fase.EmReparacao) && pausado;
    }
}
