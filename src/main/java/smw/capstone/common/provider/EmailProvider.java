package smw.capstone.common.provider;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class EmailProvider {
    private final JavaMailSender javaMailSender;

    private final String SUBJECT = "[상명위키] 인증메일입니다.";

    public boolean sendCertificationMail(String email, String certificationNumber) {

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);

            String htmlContent = getCertificationMessage(certificationNumber);

            messageHelper.setTo(email);
            messageHelper.setSubject(SUBJECT);
            messageHelper.setText(htmlContent, true);

            javaMailSender.send(message);

        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    private String getCertificationMessage(String certificationNumber){
        String certificationMessage = "";
        certificationMessage += "<h1 style='text-align: center;'>[상명위키] 인증메일</h1>";
        certificationMessage += "<h3 style='text-align: center;'>인증코드 : <strong style='font-size: 32px; letter-spacing: 8px;'>"
                + certificationNumber + "</strong></h3>";

        return certificationMessage;
    }

    public String randomCode(){
        Random random = new Random();
        int codeInt = random.nextInt(900000) + 100000;

        String code = String.valueOf(codeInt);

        return code;
    }
}