package com.venkat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.hubspot.jinjava.Jinjava;

@RestController
public class SimpleMailController {
	@Autowired
	private JavaMailSender sender;

	@RequestMapping("/sendMail")
	public String sendMail() throws IOException {
		Map<String, String> students = new HashMap<>();
                students.put("201601", "Wang Tai Hammer");
                students.put("201602", " ");
                students.put("201603", "Cui Dajian");
List<String> tasks = new ArrayList<>();
tasks.add("Alchemy");
tasks.add(" ");
                tasks .add(" ");
                tasks.add("charge");
                tasks.add("shame");

Map<String, Object> context = new HashMap<>();
context.put("name", "Dark Master");
context.put("students", students);
context.put("tasks", tasks);
context.put("HelloWorldPath", "http://localhost:8080/sendMail");
		
		Jinjava jinjava = new Jinjava();
		String template = Resources.toString(Resources.getResource("person.html"), Charsets.UTF_8);
		String renderedTemplate = jinjava.render(template, context);
		//System.err.println(renderedTemplate);
		
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		try {
			helper.setTo("venkivazza@gmail.com");
			helper.setText("Greetings :)");
			helper.setSubject("Mail From Spring Boot");
			message.setContent(renderedTemplate,"text/html");
		} catch (MessagingException e) {
			e.printStackTrace();
			return "Error while sending mail ..";
		}
		sender.send(message);
		return "Mail Sent Success!";
	}

	@RequestMapping("/sendMailAtt")
	public String sendMailAttachment() throws MessagingException {
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message,true);
		try {
			helper.setTo("demo@gmail.com");
			helper.setText("Greetings :)\n Please find the attached docuemnt for your reference.");
			helper.setSubject("Mail From Spring Boot");
			ClassPathResource file = new ClassPathResource("document.PNG");
			helper.addAttachment("document.PNG", file);
		} catch (MessagingException e) {
			e.printStackTrace();
			return "Error while sending mail ..";
		}
		sender.send(message);
		return "Mail Sent Success!";
	}

}
