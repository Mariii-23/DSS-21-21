package dss.reparacoes;

import dss.equipamentos.Fase;
import dss.exceptions.ReparacaoNaoExisteException;

import java.time.LocalDateTime;
import java.util.*;

public class ReparacoesFacade {
    private final HashMap<Integer, Reparacao> reparacoesConcluidas;
    private final HashMap<Integer, Reparacao> reparacoesArquivadas;
    private final LinkedHashMap<Integer, ReparacaoProgramada> reparacoesProgramadasAtuais;
    private final HashMap<Integer, ReparacaoExpresso> reparacoesExpressoAtuais;

    public ReparacoesFacade() {
        this.reparacoesConcluidas = new HashMap<>();
        this.reparacoesArquivadas = new HashMap<>();
        this.reparacoesProgramadasAtuais = new LinkedHashMap<>();
        this.reparacoesExpressoAtuais = new HashMap<>();
    }

    public void adicionaReparacaoProgramadaAtual(ReparacaoProgramada reparacao) {
        reparacoesProgramadasAtuais.put(reparacao.getId(), reparacao);
    }

    public void setFase(Integer reparacaoID, Fase fase) throws ReparacaoNaoExisteException {
        if (reparacoesProgramadasAtuais.containsKey(reparacaoID)) {
            ReparacaoProgramada reparacao = reparacoesProgramadasAtuais.get(reparacaoID);
            reparacao.setFase(fase);
        }
        else
            throw new ReparacaoNaoExisteException("Não existe nenhuma reparação " +
                    "programada atual com o id " + reparacaoID + ".");
    }

    public Collection<Reparacao> getReparacoesConcluidas() {
        return reparacoesConcluidas.values();
    }

    public Collection<ReparacaoProgramada> getReparacoesProgramadasAtuais() {
        return reparacoesProgramadasAtuais.values();
    }

    public Collection<ReparacaoExpresso> getReparacoesExpressoAtuais() {
        return reparacoesExpressoAtuais.values();
    }

    public void concluiExpresso(int id) throws ReparacaoNaoExisteException{
        if (reparacoesExpressoAtuais.containsKey(id)) {
            ReparacaoExpresso reparacao = reparacoesExpressoAtuais.remove(id);
            reparacoesConcluidas.put(id, reparacao);
        }
        else
            throw new ReparacaoNaoExisteException("Não existe nenhuma reparação " +
                    "expresso atual com o id " + id + ".");
    }

    public void adicionaReparacaoExpressoAtual(ReparacaoExpresso reparacaoExpresso) {
        reparacoesExpressoAtuais.put(reparacaoExpresso.getId(), reparacaoExpresso);
    }

    public void arquivaReparacoesDeEquipamento(int idEquipamento) {
        Iterator<Map.Entry<Integer, ReparacaoProgramada>> it = reparacoesProgramadasAtuais.entrySet().iterator();
        while (it.hasNext()) {
            ReparacaoProgramada reparacao = it.next().getValue();
            if (reparacao.getEquipamentoAReparar().getIdEquipamento() == idEquipamento)
                it.remove();
            reparacoesArquivadas.put(reparacao.getId(), reparacao);
        }
    }

    public void arquivaReparacoesAntigas() {
        Iterator<Map.Entry<Integer, ReparacaoProgramada>> it = reparacoesProgramadasAtuais.entrySet().iterator();
        LocalDateTime today = LocalDateTime.now();
        while (it.hasNext()) {
            ReparacaoProgramada reparacao = it.next().getValue();
            if (today.isAfter(reparacao.getDataEnvioOrcamento().plusDays(30))
                    && reparacao.getFase().equals(Fase.AEsperaResposta))
                it.remove();
            reparacoesArquivadas.put(reparacao.getId(), reparacao);
        }
    }

}