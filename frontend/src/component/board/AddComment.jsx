import React, { useState } from "react";
import { authInstance } from "../../util/api";
import boardStyles from "./Board.module.css";

const AddComment = ({ boardId }) => {
  const [comment, setComment] = useState({
    board_id: boardId,
    content: ""
  });

  const { board_id, content } = comment;

  const onChange = (event) => {
    const { value, name } = event.target; //event.target에서 name과 value만 가져오기
    setComment({
      ...comment,
      [name]: value,
    });
  };

  const handleAddComment = async () => {
        await authInstance.post("/comment",comment).then((res)=> {
            alert("성공");
        })
  };


  return (
    <div>
      <input className={`${boardStyles.addCommentInput} ${boardStyles.addCommentInput2}`}
        type="text"
        name="content"
        value={content}
        onChange={onChange}
             placeholder={"댓글을 작성해 주세요."}
      />
      <button className={`${boardStyles.addCommentBtn}`} onClick={handleAddComment}>댓글 작성</button>
    </div>
  );
};

export default AddComment;
