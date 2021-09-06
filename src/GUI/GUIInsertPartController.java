package GUI;

import ServerAPIs.PartRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.rmi.RemoteException;

public class GUIInsertPartController
{
    //private PartRepositoryConcrete repository;

    PartRepository objPR;

    @FXML AnchorPane scenePane;
    Stage stage;

    @FXML Button bt_Add;
    @FXML TextField tf_partName;
    @FXML TextField tf_partDescription;

    @FXML
    public void onCloseWindow(ActionEvent event) {
        stage = (Stage) scenePane.getScene().getWindow();
        stage.close();
    }

    @FXML
    public boolean onAddButtonClicked()
    {
        if (tf_partName.getText().equals("")) {
            GUIAlerts.showAlert("Peça não adicionada", "A peça precisa ter um nome.", "Adição inválida.", Alert.AlertType.ERROR);
            return false;
        }

        if (tf_partDescription.getText().equals("")) {
            GUIAlerts.showAlert("Peça não adicionada", "A peça precisa ter uma descrição.", "Adição inválida.", Alert.AlertType.ERROR);
            return false;
        }

        //==============================================================================================================
        // Adição da peça ao repositório

        try {
            this.objPR.addPartToRepository(tf_partName.getText(), tf_partDescription.getText());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        //==============================================================================================================

        GUIAlerts.showAlert("Peça adicionada", "A peça " + tf_partName.getText() + " foi adicionada ao repositório.", "Peça adicionada com sucesso!", Alert.AlertType.INFORMATION);
        tf_partName.setText("");
        tf_partDescription.setText("");

        return true;
    }

    @FXML
    public void receivePartRepositoryObject(PartRepository obj){
        this.objPR = obj;
    }
}
