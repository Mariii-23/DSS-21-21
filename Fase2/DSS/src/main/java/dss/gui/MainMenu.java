package dss.gui;

import dss.business.SGR.SGRInterface;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class MainMenu implements Navigatable {
    private SGRInterface sgr;
    private Navigator navigator;

    public MainMenu(SGRInterface sgr, Navigator navigator) {
        this.sgr = sgr;
        this.navigator = navigator;
    }

    public Node getScene() {
        VBox vbox = new VBox();
        Label title = new Label("Menu inicial");
        title.setFont(new Font(24.0));
        vbox.getChildren().add(title);


        Button newUserButton = new Button("Novo Utilizador");
        newUserButton.setOnAction(e -> navigator.navigateTo(new NovoUtilizador(sgr, navigator)));

        Button allUsersButton = new Button("Mostra Utilizadores");
        allUsersButton.setOnAction(e -> navigator.navigateTo(new TodosUtilizadores(sgr, navigator)));

        Button newClientButton = new Button("Novo cliente");
        newClientButton.setOnAction(e -> navigator.navigateTo(new NovoCliente(sgr, navigator)));

        Button allClientsButton = new Button("Mostra Clientes");
        allClientsButton.setOnAction(e -> navigator.navigateTo(new TodosClientes(sgr, navigator)));

        Button testPopUp = new Button("Testar popup");
        testPopUp.setOnAction(s -> navigator.openPopup(new PassoReparacaoPopup(sgr)));

        Button criaReparacaoProgramada = new Button("Cria Reparação Programada");
        criaReparacaoProgramada.setOnAction(e -> navigator.navigateTo(new NovaReparacaoProgramada(sgr, navigator)));
        Button criaReparacaoExpresso = new Button("Cria Reparação Expresso");
        criaReparacaoExpresso.setOnAction(e -> navigator.navigateTo(new NovaReparacaoExpresso(sgr, navigator)));

        Button aguardarOrcamentoButton = new Button("Reparações a aguardar Orçamento");
        aguardarOrcamentoButton.setOnAction(s -> navigator.navigateTo(new AguardarOrcamento(sgr, navigator)));

        Button listaReparacoesButton = new Button("Lista de Reparações");
        listaReparacoesButton.setOnAction(s -> navigator.navigateTo(new TodasReparacoes(sgr, navigator)));


        vbox.getChildren().addAll(newUserButton,allUsersButton, newClientButton, allClientsButton,criaReparacaoProgramada,criaReparacaoExpresso, aguardarOrcamentoButton, listaReparacoesButton, testPopUp);

        return vbox;
    }
}
