package GUI;

import ServerAPIs.PartRepository;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;

// Exemplo de acesso ao item selecionado
// String selectedItem = listViewServers.getSelectionModel().getSelectedItem();

public class GUIClientController implements Initializable
{
    private Registry registry;
    private PartRepository objRP;

    private int selectedID = -1;

    private int searchedID;
    private String searchedName;
    private String searchedDescription;

    //=====================================================================================
    // Bloco Servidor
    @FXML private TextField tf_serverAdress;
    @FXML private TextField tf_serverPort;
    @FXML private Label lbl_serverName;
    @FXML private Button bt_connectServer;

    //=====================================================================================
    // Bloco Repositório
    @FXML private Label lbl_repoName;
    @FXML private Label lbl_quantity;
    @FXML private ListView<String> lv_serverRepository;

    // Inserir
    @FXML private Button bt_insert;

    // Remover
    @FXML private Button bt_remove;

    //=====================================================================================
    // Terceiro bloco
    // Campo de busca
    @FXML private TextField tf_searchField;
    @FXML private Button bt_search;

    // Bloco Descrição
    @FXML private Label label_description;
    @FXML private Label label_partDescription;
    @FXML private Label label_serverDescription;

    // Bloco subcomponentes
    @FXML private ListView lv_subpartsName;
    @FXML private ListView lv_subpartsQuantity;
    @FXML private TextField tf_quantity;
    @FXML private Button bt_addToSubpart;
    @FXML private Button bt_removeFromSubpart;

    /**
     * Ao inserir uma porta de servidor válida e clicar em conectar, o método conecta ao servidor.
     *
     * Erros tratados:
     * Para getRegistry (Registry registry = LocateRegistry.getRegistry(host);
     * Para APIgeneralOperations objAPI = (APIgeneralOperations) registry.lookup
     * Para String msg = objAPI.apiPrintFunction()
     */
    @FXML
    public void onConnectButtonClicked()
    {
        // Tratamento : Caso o usuário não tenha inserido nenhum valor.
        if (tf_serverPort.getText().equals("")) {
            GUIAlerts.showAlert("Conexão inválida", "Por favor insira uma porta válida.", "Conexão não efetuada.", Alert.AlertType.ERROR);
            System.out.println("Por favor insira uma porta válida.");
        }

        int port = Integer.parseInt(tf_serverPort.getText());

        try {
            // Estabelecendo conexão com a instância de servidor (de acordo com a porta).
            this.registry = LocateRegistry.getRegistry(port);
            this.objRP = (PartRepository) this.registry.lookup("ServerAPIs.PartRepository");
            String response = this.objRP.partRepositoryPrintTest();
            System.out.println(this.objRP.getServerName() + " : " + response);

            //==========================================================================================================

            showListViewServerRepository(this.objRP);

            //==========================================================================================================

        } catch (NotBoundException | RemoteException e) {
            e.printStackTrace();
            GUIAlerts.showAlert("Servidor inexistente", "A porta parece não existir. Por favor, verifique uma porta válida.", "Porta " + port + " inválida.", Alert.AlertType.ERROR);
            this.lbl_serverName.setText("");
            this.lbl_repoName.setText("");
            this.lbl_quantity.setText("");
        }
    }

    // Útil para atualização da tela
    @FXML
    public void showListViewServerRepository(PartRepository ojbRP) throws RemoteException
    {
        this.lv_serverRepository.getItems().setAll();

        // Atualização dos componentes da tela.
        this.lbl_serverName.setText("Conectado ao " + this.objRP.getServerName());
        this.lbl_repoName.setText(this.objRP.getRepositoryName());
        this.lbl_quantity.setText("Peças: " + this.objRP.getNumberOfParts());

        String toListView[][] = objRP.getRepoMatrix();

        for (int i = 0; i < objRP.getNumberOfParts(); i++)
        {
            if (Integer.parseInt(toListView[i][0]) < 10)
                this.lv_serverRepository.getItems().addAll("    " + toListView[i][0] + "                    " + toListView[i][1]);

            else
                this.lv_serverRepository.getItems().addAll("   " + toListView[i][0] + "                   " + toListView[i][1]);
        }
    }

    @FXML
    public void getSelectedItemID() {
        String partValues[] = this.lv_serverRepository.getSelectionModel().getSelectedItem().split("");
        this.selectedID = getIntegerID(partValues);
        System.out.println(this.selectedID);
    }

    @FXML
    public void itemDescription(int id)
    {
        String description = "";

        try {
            description = this.objRP.getPartDescription(id);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (!description.equals("")) {
            this.label_description.setText(description);

            try {
                this.label_partDescription.setText(this.objRP.getPartName(id) + "  do");
                this.label_serverDescription.setText(this.objRP.getRepositoryName());

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Este método tem a função de criar uma nova tela para a inserção de novas peças,
     * onde o usuário através do Cliente passa o nome e a descrição da peça a ser
     * adicionada, isto no segundo bloco do código, a grosso modo.
     *
     * Na primeira parte do código, é passado a instância da interface repositório,
     * para que assim, na nova janela aberta, esta execute as ações referente ao
     * repositório selecionado.
     *
     * @throws IOException
     */
    @FXML
    public void onInsertButtonClicked() throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GUIInsertPart.fxml"));
        Parent root = fxmlLoader.load();
        GUIInsertPartController controller = fxmlLoader.getController();
        controller.receivePartRepositoryObject(this.objRP);

        Stage newStage = new Stage();
        Scene scene = new Scene(root);
        newStage.setTitle("Inserção de nova peça");
        newStage.setScene(scene);
        newStage.show();

        newStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                try {
                    showListViewServerRepository(objRP);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void clearDescriptionView() {
        this.label_description.setText("");
        this.label_partDescription.setText("");
        this.label_serverDescription.setText("");
    }

    public int getIntegerID(String stringValues[])
    {
        int id = 0;
        int iID[];
        String sID[];

        //=======================================================
        // Bloco para tirar os primeiros espaços da string

        int size = stringValues.length;

        int spaces = 0;
        int i = 0;
        while (stringValues[i].equals(" ")) {
            spaces++;
            i++;
        }

        int n = size - spaces;
        String sValues[] = new String[n];
        for (int j = 0; j < n; j++) {
            sValues[j] = stringValues[spaces];
            spaces++;
        }

        //=======================================================
        // Atribuição do(s) primeiro(s) número(s) do vetor
        // sValues para o vetor sID, em forma de string.

        int integers = 0;
        for (int j = 0; j < sValues.length; j++) {
            if (!sValues[j].equals(" ")) integers++;
            else j = sValues.length;
        }

        sID = new String[integers];

        for (int j = 0; j < sID.length; j++)
            sID[j] = sValues[j];

        //=======================================================
        // Passando os números da string como inteiros para iID.

        iID = new int[sID.length];

        for (int j = 0; j < iID.length; j++)
            iID[j] = Integer.parseInt(sValues[j]);

        int exponent = 0;
        for (int j = iID.length - 1; j >= 0; j--) {
            id = id + iID[j] * (int) Math.pow(10, exponent);
            exponent++;
        }

        return id;
    }

    @FXML
    public void onRemoveButtonClicked() throws IOException
    {
        String partValues[] = lv_serverRepository.getSelectionModel().getSelectedItem().split("");
        int id = getIntegerID(partValues);
        this.objRP.removePartFromRepository(id);
        showListViewServerRepository(this.objRP);
        clearDescriptionView();
    }

    @FXML
    public void onSearchButtonClicked()
    {
        if (this.tf_searchField.getText().equals(""))
            GUIAlerts.showAlert("Valor não inserido", "Não foi inserido nenhum valor.", " Por favor, insira um ID válido.", Alert.AlertType.ERROR);

        else {
            String stringID = tf_searchField.getText();
            this.searchedID = Integer.parseInt(stringID);
        }

        try
        {
            if (this.objRP.verifyIfIDAlreadyExist(this.searchedID) == -1) {
                clearDescriptionView();
                GUIAlerts.showAlert("Peça inexistente", "A peça parece não existir.", " Por favor, verifique uma peça existente.", Alert.AlertType.ERROR);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        itemDescription(this.searchedID);

        //========================================================================
        // Bloco de subcomponentes
        try {
            showListViewSubpartList(this.searchedID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    //==========================================================================================
    // Bloco para operações em subcomponentes

    @FXML
    public void showListViewSubpartList(int partID) throws RemoteException
    {
        this.lv_subpartsName.getItems().setAll();
        this.lv_subpartsQuantity.getItems().setAll();

        this.objRP.showListOfSubparts(this.searchedID);

        String subpartMatrix[][] = this.objRP.getSubpartMatrix(partID);
        for (int i = 0; i < subpartMatrix.length; i++) {
                this.lv_subpartsName.getItems().addAll(subpartMatrix[i][0]);
                this.lv_subpartsQuantity.getItems().addAll(subpartMatrix[i][1]);
        }
    }

    @FXML
    public void onAddSubpartList() throws RemoteException
    {
        //System.out.println("selectedID: " + this.selectedID);

        if (tf_quantity.getText().equals("") || tf_searchField.getText().equals("") || this.selectedID == -1)
        {
            if (tf_quantity.getText().equals("")) {
                GUIAlerts.showAlert("Quantidade não especificada", "A quantidade não foi especificada.", "Por favor especifique uma quantidade.", Alert.AlertType.ERROR);
            }
            if (tf_searchField.getText().equals("")) {
                GUIAlerts.showAlert("Subcomponente não especificado", "O subcomponente a ser adicionado na peça de ID: " + this.selectedID + " não foi inserido no campo de busca.", "Por favor insira o id de uma peça válida.", Alert.AlertType.ERROR);
            }
            if (this.selectedID == -1)
                GUIAlerts.showAlert("Peça não selecionada", "Não há nenhuma peça selecionada no campo 'Repositório' para receber um componente.", "Por favor selecione uma peça no campo 'Repositório' para que um componente possa ser adicionado.", Alert.AlertType.ERROR);
        }

        else
        {
            this.searchedName = this.objRP.getPartName(this.searchedID);
            this.searchedDescription = this.objRP.getPartDescription(this.searchedID);
            int quantity = Integer.parseInt(tf_quantity.getText());
            this.objRP.addToSubpartList(this.searchedID, this.selectedID, this.searchedName, this.searchedDescription, quantity);
            this.tf_quantity.setText("");
            GUIAlerts.showAlert("Subcomponente adicionado", "Peça de ID: " + this.searchedID + " foi adicionado como subcomponente da peça de ID: " + this.selectedID + " com sucesso.", "Operação realizada com sucesso.", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    public String getSelectedSubpartName() {
        String subpartName = "";

        subpartName = this.lv_subpartsName.getSelectionModel().getSelectedItem().toString();
        //String subpartQuantity = this.lv_subpartsQuantity.getSelectionModel().getSelectedItem().toString();

        System.out.println(subpartName);

        return subpartName;
    }

    @FXML
    public void onRemoveSubpartList()
    {
        String nameToRemove = getSelectedSubpartName();
        int idPart = -1;
        int quantity = 0;

        if (this.tf_quantity.getText().equals(""))
            GUIAlerts.showAlert("Quantidade não inserida", "A quantidade de peças a ser removida não foi indicada.", "Por favor indique a quantidade de componentes a ser removido.", Alert.AlertType.ERROR);

        else
            quantity = Integer.parseInt(this.tf_quantity.getText());

        if (lv_subpartsName.getSelectionModel().getSelectedItem() == null)
            GUIAlerts.showAlert("Nenhum item selecionado", "Não há nenhum componente selecionado para remoção.", "Por favor selecione um componente a ser removido.", Alert.AlertType.ERROR);

        else {
            idPart = Integer.parseInt(this.tf_searchField.getText());

            try {
                this.objRP.removeFromSubpartList(nameToRemove, idPart, quantity);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        try {
            showListViewSubpartList(this.searchedID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    //==========================================================================================

    /**
     * Este método inicializa as instâncias de servidor e seus
     * respectivos repositórios.
     * <p>
     * @param url : Local utilizado para resolver caminhos relativos para o objeto raiz ou nulo se este for indefinido.
     * @param resourceBundle : Recurso utilizado para localizar o objeto raiz, ou nulo se o objeto raiz não tenha sido localizado.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        //...
    }
}
