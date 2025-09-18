package vn.iotstar.utils;

import java.util.Properties;
import java.util.Random;

import org.springframework.stereotype.Component;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import vn.iotstar.entities.UserEntity;

@Component
public class EmailUtils {
	
	// Tạo mã code ngẫu nhiên 6 chữ số
	public String getRandom() {
		Random rnd = new Random();
		int number = rnd.nextInt(999999);
		return String.format("%06d", number);
	}
	
	// Gửi email xác thực tài khoản
	public boolean sendEmail(UserEntity user) {
		boolean test = false;

		String toEmail = user.getEmail();
		String fromEmail = "nguyentrilam0304@gmail.com";
		String password = "tkos soaj hmmi exyn";

		try {
			// Thiết lập cấu hình SMTP cho Gmail
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", "587");

			// Tạo session xác thực
			Session session = Session.getInstance(props, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(fromEmail, password);
				}
			});

			// Soạn nội dung email
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromEmail));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
			message.setSubject("Xác thực tài khoản người dùng");

			String code = user.getCode(); // mã xác nhận được gán trước khi gọi sendEmail
			String htmlContent = "<h3>Xin chào " + user.getFullname() + ",</h3>"
				+ "<p>Mã xác thực tài khoản của bạn là: <strong>" + code + "</strong></p>"
				+ "<p>Vui lòng không chia sẻ mã này với người khác.</p>";

			message.setContent(htmlContent, "text/html; charset=UTF-8");

			// Gửi email
			Transport.send(message);
			test = true;
			
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		return test;
	}
}
