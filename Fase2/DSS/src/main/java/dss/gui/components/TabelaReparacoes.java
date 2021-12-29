package dss.gui.components;

import dss.SGR;
import dss.clientes.Cliente;
import dss.exceptions.NaoExisteException;
import dss.exceptions.UtilizadorJaExisteException;
import dss.exceptions.UtilizadorNaoExisteException;
import dss.reparacoes.Reparacao;
import dss.utilizador.Utilizador;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TabelaReparacoes extends TableView<Reparacao> {
    private SGR sgr;

    public TabelaReparacoes(SGR sgr) {
        super();

        this.sgr = sgr;

        TableColumn<Reparacao, String> cliente = new TableColumn<>("Cliente");
        cliente.setCellValueFactory(cellData -> {
            try {
                Cliente c = sgr.getCliente(cellData.getValue().getIdCliente());
                return new SimpleStringProperty(c.getNome());
            } catch (NaoExisteException e) {
                e.printStackTrace();
                return null;
            }
        });

        TableColumn<Reparacao, String> funcionarioCriador = new TableColumn<>("Funcionário criador");
        funcionarioCriador.setCellValueFactory(cellData -> {
            try {
                Utilizador f = sgr.getUtilizador(cellData.getValue().getFuncionarioCriador());
                return new SimpleStringProperty(f.getNome());
            } catch (NaoExisteException e) {
                e.printStackTrace();
                return null;
            }
        });

        this.getColumns().addAll(cliente, funcionarioCriador);
    }
}