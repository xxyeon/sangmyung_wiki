package smw.capstone.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smw.capstone.common.exception.BusinessException;
import smw.capstone.common.exception.CustomErrorCode;
import smw.capstone.common.provider.EmailProvider;
import smw.capstone.entity.Member;
import smw.capstone.entity.SigninCode;
import smw.capstone.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository mr;
    private final EmailProvider emailProvider;
    Member member = new Member();

    @Transactional
    public void register(Member member) {
        mr.save(member);
    }

    @Transactional
    public void delete(Member member) {
        mr.remove(member);
    }

    @Transactional
    public Member login(String id, String pw) {
        Member requestmember = mr.findByUsername(id);

        if (requestmember == null) {
            throw new BusinessException(CustomErrorCode.NOT_EXIST_MEMBER);
        } else if (!requestmember.getPassword().equals(pw)) {
            throw new BusinessException(CustomErrorCode.NOT_LOGIN);
        } else {
            return requestmember;
        }
    }

    @Transactional
    public void update(Member member) {
        mr.update(member);
    }

    public Member findByUsername(String username) {
        return mr.findByUsername(username);
    }

    @Transactional
    public void sendNumber(String email) {
        //인증코드 생성 함수
        String code = emailProvider.randomCode();
        //함수 호출될때 들어온 email로 code 발송
        emailProvider.sendCertificationMail(email, code);
        //email, code를 SigininCode Table에 저장
        SigninCode target = new SigninCode();
        target.setEmail(email);
        target.setCertification_Code(code);
        mr.certificate_process(target);

    }

    public ResponseEntity<?> certificate(String email, String code) {
        String answer = mr.findCode(email);
        if (!answer.equals(code)){
            throw new BusinessException(CustomErrorCode.NOT_MATCHED_CODE);
        }else{
            return ResponseEntity.ok().body("인증되었습니다.");
        }
    }
}
