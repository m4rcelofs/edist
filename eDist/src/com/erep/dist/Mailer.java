package com.erep.dist;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import static javax.mail.Transport.send;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mailer {

	public class MailAddress {
		public String	name;
		public String	address;

		public MailAddress() {}

		public MailAddress(String name, String address) {
			this.name = name;
			this.address = address;
		}
	}

	/**
	 * @param nameEmails
	 *            [name][email]
	 */
	public void mail(String subject, String message, MailAddress... emails)
			throws IOException {

		try {
			Properties props = new Properties();
			Session session = Session.getDefaultInstance(props, null);

			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("RoBoT.erep@gmail.com"));

			for (MailAddress email : emails)
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
						email.address, email.name));

			msg.setSubject(subject);
			msg.setContent(message, "text/html");
			send(msg);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

}
