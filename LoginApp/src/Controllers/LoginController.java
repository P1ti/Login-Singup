package Controllers;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LoginController {
	@FXML
	private Button loginBtn;
	@FXML
	private Button signUpBtn;
	@FXML
	private Button closeBtn;
	@FXML
	private Button signupBtn;
	@FXML
	private TextField emailInput;
	@FXML
	private PasswordField passwordInput;
	@FXML
	private Label response;
	
	static MessageDigest md;
	
	@FXML
	private void handleMouseAction(MouseEvent e) throws IOException, NoSuchAlgorithmException, SQLException {
		String email = emailInput.getText();
		String password = passwordInput.getText();
		if (e.getSource() == closeBtn) {
			Stage stage = (Stage) closeBtn.getScene().getWindow();
			stage.close();
		}
		if (e.getSource() == loginBtn) {
			if (email == "" || password == "") {
				System.out.println("Error!");
			} else {
				if (validateEmail(email)) {
					String encryptedPWD = encryptPass(password);
					Database db = new Database();
					List<User> user = db.getUser(email, encryptedPWD);
					String pwdFromDB = user.get(0).getPwd();
					verifyIfPwdMatch(encryptedPWD, pwdFromDB);
				}
			}
		} 
		if (e.getSource() == signupBtn) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Resources/SignUpPage.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
		}
	}

	private void verifyIfPwdMatch(String encryptedPWD, String pwdFromDB) {
		if (encryptedPWD.equals(pwdFromDB)) {
			response.setText("Logged In");
		} else {
			response.setText("Try again");
		}
	}

	private static String encryptPass(String password) throws NoSuchAlgorithmException {
		md = MessageDigest.getInstance("MD5");		
		byte[] pwd = password.getBytes();
		md.reset();
		byte[] digested = md.digest(pwd);
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<digested.length; i++) {
			sb.append(Integer.toHexString(0xff & digested[i]));
		}
		return sb.toString();
	}

	private boolean validateEmail(String email) {
		String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
		Pattern patt = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
		Matcher matc = patt.matcher(email);
		return matc.find();
	}
}
