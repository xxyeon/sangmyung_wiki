import React from 'react';
import { NavLink } from "react-router-dom";

function CreateAccountEmail() {
    return (
        <div style={{ textAlign: 'left' }}>
            <h2><b>계정 만들기</b></h2>
            <label htmlFor="email">이메일</label><br />
            <input type="email" id="email" name="email" /><br /><br />
            <p>이메일 허용 목록이 활성화 되어 있습니다. 이메일 허용 목록에 존재하는 메일만 사용할 수 있습니다.</p>
            <ul style={{ listStyleType: 'disc' }}>
                <li>naver.com</li>
                <li>gmail.com</li>
                <li>kakao.com</li>
            </ul>
            <p><b>가입후 탈퇴는 불가능합니다.</b></p>
            <div>
                <button type="submit">
                    <NavLink to="/createId">가입</NavLink>
                </button>
            </div>
        </div>
    );
}

export default CreateAccountEmail;