import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Link, useNavigate } from 'react-router-dom';

import { authInstance } from '../../util/api'; 

const BoardList = () => {
  const navigate = useNavigate();
  const [boardList, setBoardList] = useState([]);

  const getBoardList = async () => {
    const resp = await (await authInstance.get('/board')); // 게시글 목록 데이터에 할당
    setBoardList(resp.data); // boardList 변수에 할당

    const pngn = resp.pagination;
    console.log(pngn);
  };

  const moveToWrite = () => {
    navigate('/write');
  };

  useEffect(() => {
    getBoardList(); // 게시글 목록 조회 함수 호출
  }, []);

  return (
    <div>
      <ul>
        {boardList.map((board) => (
          // map 함수로 데이터 출력
          <li key={board.idx}>
            <Link to={`/board/${board.idx}`}>{board}</Link>
          </li>
        ))}
      </ul>
      <div>
        <button onClick={moveToWrite}>글쓰기</button>
      </div>
    </div>
  );
};

export default BoardList;