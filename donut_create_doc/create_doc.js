import React, { useState } from "react";
import createDoc from '@/app/create_doc.module.css';

export default function CreateDoc({ isActive, setIsActive }) {
  // const [isActive, setIsActive] = useState(true); // 문서 생성 팝업 active 상태
  const [title, setTitle] = useState(''); // 제목 입력 상태
  const [description, setDescription] = useState(''); // 설명 입력 상태
  const [activeIndex, setActiveIndex] = useState(0); // 공개 활성화 초기화 상태 

  const handleTitleChange = (event) => {
    setTitle(event.target.value); // 제목 입력값으로 title 업데이트
  };

  const handleDescriptionChange = (event) => {
    setDescription(event.target.value); // 설명 입력값으로 description 업데이트
  };

  const handleClick = (index) => {    
    if (activeIndex === index) return;// 클릭한 버튼이 이미 활성화 상태면 
    setActiveIndex(index); // 클릭한 버튼 활성화 
  };

  const handleCancelClick = () => {
    setIsActive(false); // 팝업의 active 상태를 false로 설정하여 active 클래스를 제거합니다.
  };

   // 제목과 설명이 모두 입력된 경우, 다음 버튼에 active 클래스 추가
   const isNextActive = title.trim() !== '' && description.trim() !== '';

  return (
    <>
      <div className={`${createDoc.pop_bg} ${isActive ? createDoc.active : ''}`}/>
  
      <div className={`${createDoc.pop_doc} ${isActive ? createDoc.active : ''}`}>
        <p className={createDoc.pop_tit}>핵심 노트 만들기</p>

        {/* 노트 제목 */}
        <p className={`${createDoc.pop_item} pt-3 pb-1`}>노트 제목</p>
        <div>
          <input className={createDoc.pop_input} placeholder="노트 제목을 입력해주세요." maxLength={50} value={title} onChange={handleTitleChange}/>
          <span className={createDoc.tit_count}>{title.length > 50 ? 50 : title.length}/50</span>
        </div>

        {/* 노트 설명 */}
        <p className={`${createDoc.pop_item} pt-3 pb-1`}>노트 설명</p>
        <div className={createDoc.pop_descBox}>
          <textarea className={createDoc.pop_textarea} placeholder="노트 설명을 간략하게 입력해주세요." value={description} maxLength={200} onChange={handleDescriptionChange}/>
          <span className={createDoc.tit_count}>{description.length > 200 ? 200 : description.length}/200</span>
        </div>

        {/* 공개 여부 */}
        <p className={`${createDoc.pop_item} pt-3 pb-1`}>공개 여부</p>
        <ul className={createDoc.show_doc}>
          <li className={activeIndex === 0 ? createDoc.active : ''}
        onClick={() => handleClick(0)}>공개</li>
          <li className={activeIndex === 1 ? createDoc.active : ''}
          onClick={() => handleClick(1)}>비공개</li>
        </ul>

        {/* 취소 / 다음 버튼 */}
        <ul className={createDoc.choice_btn_list}>
          <li onClick={handleCancelClick}>취소</li>
          <li className={isNextActive ? createDoc.active : ''}>다음</li>
        </ul>        
      </div>
    </>
  );
}
