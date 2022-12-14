package dss.data;

import dss.business.equipamento.Fase;
import dss.exceptions.ReparacaoDesteClienteJaExisteException;
import dss.exceptions.JaExisteException;
import dss.exceptions.NaoExisteException;
import dss.exceptions.ReparacaoNaoExisteException;
import dss.business.reparacao.Reparacao;
import dss.business.reparacao.ReparacaoExpresso;
import dss.business.reparacao.ReparacaoProgramada;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReparacoesDAO implements IReparacoes, Serializable {
    private final HashMap<Integer, Reparacao> reparacoesConcluidas;
    private final HashMap<Integer, Reparacao> reparacoesArquivadas;
    private final LinkedHashMap<Integer, ReparacaoProgramada> reparacoesProgramadasAtuais;
    private final HashMap<Integer, ReparacaoExpresso> reparacoesExpressoAtuais;

    public ReparacoesDAO() {
        this.reparacoesConcluidas = new HashMap<>();
        this.reparacoesArquivadas = new HashMap<>();
        this.reparacoesProgramadasAtuais = new LinkedHashMap<>();
        this.reparacoesExpressoAtuais = new HashMap<>();
    }

    public static ReparacoesDAO lerReparacoes(String ficheiro) {
        try {
            FileInputStream fis = new FileInputStream(ficheiro);
            ObjectInputStream ois = new ObjectInputStream(fis);

            return (ReparacoesDAO) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ReparacoesDAO();
    }

    public static void escreverReparacoes(String ficheiro, ReparacoesDAO reparacoes) {
        try {
            FileOutputStream fos = new FileOutputStream(ficheiro);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(reparacoes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void adicionaReparacaoProgramadaAtual(ReparacaoProgramada reparacao) throws ReparacaoDesteClienteJaExisteException {
        if(jaExisteReparaoDoCliente(reparacao.getIdCliente()))
            throw new ReparacaoDesteClienteJaExisteException();
        reparacoesProgramadasAtuais.put(reparacao.getId(), reparacao);
    }

    private boolean jaExisteReparaoDoCliente(String idCliente) {
        return(reparacoesProgramadasAtuais.values().stream().anyMatch(r -> r.getIdCliente().equals(idCliente))
                || reparacoesExpressoAtuais.values().stream().anyMatch(r -> r.getIdCliente().equals(idCliente)));
    }

    public void setFase(Integer reparacaoID, Fase fase) throws ReparacaoNaoExisteException {
        if (reparacoesProgramadasAtuais.containsKey(reparacaoID)) {
            ReparacaoProgramada reparacao = reparacoesProgramadasAtuais.get(reparacaoID);
            reparacao.setFase(fase);
        } else
            throw new ReparacaoNaoExisteException("N??o existe nenhuma repara????o " +
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

    public void concluiExpresso(int id, Duration duracaoReal) throws ReparacaoNaoExisteException {
        if (reparacoesExpressoAtuais.containsKey(id)) {
            ReparacaoExpresso reparacao = reparacoesExpressoAtuais.remove(id);
            reparacao.setDuracaoReal(duracaoReal);
            reparacoesConcluidas.put(id, reparacao);
        } else
            throw new ReparacaoNaoExisteException("N??o existe nenhuma repara????o " +
                    "expresso atual com o id " + id + ".");
    }

    public void adicionaReparacaoExpressoAtual(ReparacaoExpresso reparacaoExpresso) throws ReparacaoDesteClienteJaExisteException {
        if(jaExisteReparaoDoCliente(reparacaoExpresso.getIdCliente()))
            throw new ReparacaoDesteClienteJaExisteException();
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

    public void marcarComoConcluido(Reparacao reparacao){
        if (reparacao instanceof ReparacaoExpresso)
            reparacoesExpressoAtuais.remove(reparacao.getId());
        else
            reparacoesProgramadasAtuais.remove(reparacao.getId());
        reparacoesConcluidas.put(reparacao.getId(),reparacao);
    }

    public void marcarOrcamentoComoArquivado(ReparacaoProgramada reparacao){
        reparacoesProgramadasAtuais.remove(reparacao.getId());
        reparacoesArquivadas.put(reparacao.getId(),reparacao);
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

    @Override
    public void marcaComoEntregue(int idReparacao) throws ReparacaoNaoExisteException {
        Reparacao re = reparacoesExpressoAtuais.remove(idReparacao);
        if (re == null) {
            re = reparacoesProgramadasAtuais.remove(idReparacao);
        }
        if( re == null) throw new ReparacaoNaoExisteException();
        reparacoesConcluidas.put(re.getId(), re);


    }
    @Override
    public void updateLastID() {
        int max = Stream.concat(reparacoesExpressoAtuais.keySet().stream(),
                reparacoesProgramadasAtuais.keySet().stream())
                .max(Comparator.naturalOrder())
                .orElse(-1);
        Reparacao.updateLastID(max);
    }

    @Override
    public Reparacao get(Integer id) throws NaoExisteException {
        return getAll().stream().filter(c -> c.getId() == id).findAny().orElseThrow(NaoExisteException::new);
    }

    @Override
    public void add(Reparacao item) throws JaExisteException {
        reparacoesProgramadasAtuais.put(item.getId(), (ReparacaoProgramada) item);
    }

    @Override
    public void remove(Integer id) throws NaoExisteException {
        if(reparacoesProgramadasAtuais.containsKey(id)) {
            reparacoesProgramadasAtuais.remove(id);

        }
        else if (reparacoesExpressoAtuais.containsKey(id)) {
            reparacoesExpressoAtuais.remove(id);

        }
        else if (reparacoesArquivadas.containsKey(id)) {
            reparacoesArquivadas.remove(id);

        }
        else if (reparacoesConcluidas.containsKey(id)) {
            reparacoesConcluidas.remove(id);
        }
         else {
             throw  new ReparacaoNaoExisteException();
        }
    }

    @Override
    public <C> Collection<C> getByClass(Class<C> classe) {
        return getAll().stream().filter(classe::isInstance).map(classe::cast).collect(Collectors.toList());
    }

    @Override
    public Collection<Reparacao> getAll() {
        ArrayList<Reparacao> r = new ArrayList<> ();
        r.addAll(reparacoesConcluidas.values());
        r.addAll(reparacoesArquivadas.values());
        r.addAll(reparacoesProgramadasAtuais.values());
        r.addAll(reparacoesExpressoAtuais.values());
        return r;
    }
}
