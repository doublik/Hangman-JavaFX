package Hangman;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ResourceBundle;

public class TitleController implements Initializable {

    @FXML private AnchorPane paneSelect, paneLogin;
    @FXML private Button bGameStart, bSetting;
    @FXML private Label errMsg;
    @FXML private TextField txtID;
    @FXML private PasswordField txtPW, txtConfirmPW;
    @FXML private Button bLogin, bJoin;

    public static boolean isLogined = false;
    public static String LoginedID;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (isLogined) {
            paneLogin.setVisible(false);
            paneSelect.setVisible(true);
        } else {
            // 텍스트필드 최대 입력 글자수 제한
            int MAX_LENGTH = 18;
            txtID.lengthProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    if (newValue.intValue() > oldValue.intValue()) {
                        if (txtID.getText().length() > MAX_LENGTH) {
                            txtID.setText(txtID.getText().substring(0,MAX_LENGTH));
                        }
                    }
                }
            });
            txtPW.lengthProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    if (newValue.intValue() > oldValue.intValue()) {
                        if (txtPW.getText().length() > MAX_LENGTH) {
                            txtPW.setText(txtPW.getText().substring(0,MAX_LENGTH));
                        }
                    }
                }
            });
            txtConfirmPW.lengthProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    if (newValue.intValue() > oldValue.intValue()) {
                        if (txtConfirmPW.getText().length() > MAX_LENGTH) {
                            txtConfirmPW.setText(txtConfirmPW.getText().substring(0,MAX_LENGTH));
                        }
                    }
                }
            });
        }
    }

    // 게임시작 버튼 클릭
    @FXML
    private void bGS_Action(ActionEvent event) throws IOException {
        Stage stage = (Stage) bGameStart.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("fxml/GameScreen.fxml"));
        Scene scene = new Scene(root,800,600);
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    // 세팅 버튼 클릭
    @FXML
    private void bS_Action(ActionEvent event) throws IOException {
        Stage stage = (Stage) bGameStart.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("fxml/SettingScreen.fxml"));
        Scene scene = new Scene(root,800,600);
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    // 로그인
    @FXML
    private void Do_Login(ActionEvent event) throws IOException {
        // 둘 다 공백일 경우 아이디 공백 메시지를 우선 적용.
        if (txtID.getText().trim().isEmpty()) {
            setErrMsg("아이디를 적어주세요.");
        } else if (txtPW.getText().trim().isEmpty()) {
            setErrMsg("비밀번호를 적어주세요.");
        } else {
            String path = "./data/" + txtID.getText() + ".db";
            try {
                BufferedReader reader = new BufferedReader(new FileReader(path));
                String encryptPW = sha256(txtPW.getText());
                String readPW = reader.readLine();

                if (!readPW.equals("$PW" + encryptPW)) {
                    throw new Exception("WrongPassword");
                }

                String readSetting = reader.readLine();
                String[] parameters = readSetting.split("&");
                for (int i=0; i<parameters.length; i++) {
                    String name = parameters[i].split("=")[0];
                    if (name.equals("Background")) {
                        String value = parameters[i].split("=")[1];
                        try {
                            GameController.bgNum = Integer.valueOf(value);
                            if (GameController.bgNum < 1 || GameController.bgNum > 3) { // 잘못된 숫자 입력 방지
                                GameController.bgNum = 1;
                            }
                        } catch (Exception e) {
                            setErrMsg("설정값 오류");
                        }
                    } else if (name.equals("HumanGender")) {
                        String value = parameters[i].split("=")[1];
                        if (value.equals("man"))
                            GameController.isMan = true;
                        else if (value.equals("woman"))
                            GameController.isMan = false;
                    }
                }
                reader.close();
                LoginedID = txtID.getText();
                isLogined = true;
                paneLogin.setVisible(false);
                paneSelect.setVisible(true);
            } catch (FileNotFoundException e) {
                setErrMsg("아이디가 존재하지 않습니다.");
            } catch (IOException e) { // 파일 입출력 오류
                setErrMsg("문제가 발생하였습니다.");
            } catch (Exception e) {
                if (e.getMessage().equals("WrongPassword")) {
                    setErrMsg("비밀번호가 틀렸습니다.");
                }
            }
        }
    }

    // 회원가입
    @FXML
    private void Do_Join(ActionEvent event) throws IOException {
        if (txtConfirmPW.visibleProperty().getValue() == true) {
            if (txtID.getText().trim().isEmpty()) {
                setErrMsg("아이디를 적어주세요.");
            } else if (txtPW.getText().trim().isEmpty()) {
                setErrMsg("비밀번호를 적어주세요.");
            } else if (txtConfirmPW.getText().trim().isEmpty()) {
                setErrMsg("비밀번호를 다시 한번 적어주세요.");
            } else {
                String path = "./data/" + txtID.getText() + ".db";
                try {
                    if (Files.exists(Paths.get(path))) {
                        throw new Exception("ExistID");
                    }
                    if (!txtPW.getText().equals(txtConfirmPW.getText())) {
                        throw new Exception("DifferentPW");
                    }

                    if (!Files.exists(Paths.get("./data"))) { // 디렉토리 존재 확인
                        Files.createDirectory(Paths.get("./data"));
                    }
                    BufferedWriter writer = new BufferedWriter(new FileWriter(path));
                    writer.write("$PW" + sha256(txtPW.getText()));
                    writer.newLine();
                    writer.write("Background=1&HumanGender=man"); // save default value]
                    writer.close();
                    txtConfirmPW.setVisible(false);
                } catch (IOException e) {
                    setErrMsg("문제가 발생하였습니다.");
                } catch (Exception e) {
                    if (e.getMessage().equals("ExistID")) {
                        setErrMsg("이미 존재하는 아이디입니다.");
                    } else if (e.getMessage().equals("DifferentPW")) {
                        setErrMsg("두 비밀번호가 일치하지 않습니다.");
                    }
                    e.printStackTrace();
                }
            }
        } else {
            txtConfirmPW.setVisible(true);
        }
    }

    // 로그인 에러 메세지 설정 후 보여주기.
    private void setErrMsg(String msg) {
        errMsg.setText(msg);
        errMsg.setVisible(true);
    }

    // SHA-256 ENCRYPT
    public static String sha256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
