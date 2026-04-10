package pt.upskill.smart_city_co2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.base.url}")
    private String baseUrl;

    public void enviarCodigoRecuperacao(String toEmail, String codigo, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Recuperação de Password - Smart City CO₂");
        message.setText(String.format(
                "Olá %s,\n\n" +
                        "Recebemos um pedido para recuperar a password da sua conta.\n\n" +
                        "O seu código de verificação é: %s\n\n" +
                        "Este código é válido por 15 minutos.\n\n" +
                        "Se não solicitou esta alteração, ignore este email.\n\n" +
                        "Atenciosamente,\n" +
                        "Equipa Smart City CO₂",
                username, codigo
        ));

        mailSender.send(message);
    }
}