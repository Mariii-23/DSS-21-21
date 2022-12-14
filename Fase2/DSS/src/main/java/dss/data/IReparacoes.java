package dss.data;

import dss.business.reparacao.Reparacao;
import dss.business.reparacao.ReparacaoExpresso;
import dss.business.reparacao.ReparacaoProgramada;
import dss.exceptions.NaoExisteException;
import dss.exceptions.ReparacaoDesteClienteJaExisteException;
import dss.exceptions.ReparacaoNaoExisteException;
import dss.business.equipamento.Fase;
import java.util.Collection;
import java.time.Duration;

public interface IReparacoes extends IDAO<Reparacao,Integer> {
    void adicionaReparacaoProgramadaAtual(ReparacaoProgramada reparacao) throws ReparacaoDesteClienteJaExisteException;
    void setFase(Integer reparacaoID, Fase fase) throws ReparacaoNaoExisteException ;
    Collection<Reparacao> getReparacoesConcluidas();
    Collection<ReparacaoProgramada> getReparacoesProgramadasAtuais();
    Collection<ReparacaoExpresso> getReparacoesExpressoAtuais();
    void concluiExpresso(int id, Duration duracaoReal) throws ReparacaoNaoExisteException;
    void marcarComoConcluido(Reparacao reparacao);
    void adicionaReparacaoExpressoAtual(ReparacaoExpresso reparacaoExpresso) throws ReparacaoDesteClienteJaExisteException;
    void arquivaReparacoesDeEquipamento(int idEquipamento);
    void marcarOrcamentoComoArquivado(ReparacaoProgramada reparacao);
    void arquivaReparacoesAntigas();

    void marcaComoEntregue(int idReparacao) throws ReparacaoNaoExisteException;


    void updateLastID();
}
