package Controllers;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SignUpController {
	@FXML
	private Button closeBtn;
	@FXML
	private Button loginBtn;
	@FXML
	private Button signUpBtn;
	@FXML
	private TextField nameInput;
	@FXML
	private TextField emailInput;
	@FXML
	private PasswordField passwordInput;
	
	static MessageDigest md;
	
	@FXML
	private void handleMouseAction(MouseEvent e) throws IOException, NoSuchAlgorithmException {
		String name = nameInput.getText();
		String email = emailInput.getText();
		String password = passwordInput.getText();
		if (e.getSource() == closeBtn) {
			Stage stage = (Stage) closeBtn.getScene().getWindow();
			stage.close();
		}
		if (e.getSource() == loginBtn) {
			moveToLogin(e);
		}
		if (e.getSource() == signUpBtn) {
			if (name == "" || email == "" || password == "") {
				System.out.println("Error!");
			} else {
				if (emailPatt(email)) {
					String encryptedPWD = passwordEncrypt(password);
					Database db = new Database();
					db.createNewEntry(name, email, encryptedPWD);
					moveToLogin(e);
				}
			}
		}
	}
	
	public void moveToLogin(MouseEvent e) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/LoginPage.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}

	public static String passwordEncrypt(String password) throws NoSuchAlgorithmException {
		md = MessageDigest.getInstance("MD5");
		byte[] pass = password.getBytes();
		md.reset();
		byte[] digested = md.digest(pass);
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<digested.length;i++) {
			sb.append(Integer.toHexString(0xff & digested[i]));
		}
		return sb.toString();
	}

	private boolean emailPatt(String email) {
		String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
		Pattern patt = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
		Matcher matc = patt.matcher(email);
		return matc.find();
	}
}
