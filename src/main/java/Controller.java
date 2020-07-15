import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {


    public TableView main_table;
    public TableColumn symbol_column;
    public TableColumn price_column;
    public TableColumn shares_column;
    public TableColumn time_column;
    public Button start_button;
    public TextArea symbols_area;
    public TextArea sizes_area;
    public Button save_button;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("hello world, we're started...");

        List<Symbol> symbolList = new ArrayList<>();
        List<String> namesList = new ArrayList<>();
        List<String> sizesList = new ArrayList<>();

        if (new File("symbols.txt").exists()) {
            try {
                namesList = Files.readAllLines(Paths.get("symbols.txt"));
                StringBuilder stringBuilder = new StringBuilder();
                namesList.forEach(name -> stringBuilder.append(name).append("\n"));
                symbols_area.setText(stringBuilder.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (new File("sizes.txt").exists()) {
            try {
                sizesList = Files.readAllLines(Paths.get("sizes.txt"));
                StringBuilder stringBuilder = new StringBuilder();
                sizesList.forEach(size -> stringBuilder.append(size).append("\n"));
                sizes_area.setText(stringBuilder.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


//        symbolList.add(new Symbol("AZN.NY"));
//        symbolList.add(new Symbol("CCL.NY"));
//        symbolList.add(new Symbol("SNAP.NY"));
//        symbolList.add(new Symbol("T.NY"));

        List<Integer> checkList = new ArrayList<>();
        checkList.add(40);
        checkList.add(120);

        APIService apiService = new APIService(symbolList, checkList);
        apiService.regSymbolTOSStream(30002);
        apiService.readerTOSStream(30002);

    }

    public void saveButtonClicked(ActionEvent actionEvent) {
        try (FileWriter fileWriter = new FileWriter("symbols.txt", false)) {
            fileWriter.write(symbols_area.getText());
        } catch (Exception e ) {
            e.printStackTrace();
        }

        try (FileWriter fileWriter = new FileWriter("sizes.txt", false)) {
            fileWriter.write(sizes_area.getText());
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }

    public void startButtonClicked(ActionEvent actionEvent) {

    }
}
