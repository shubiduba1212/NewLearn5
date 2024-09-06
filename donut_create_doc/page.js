"use client";
import React, { useState } from 'react';
import CreateDoc from '@/app/create_doc';


export default function Home() {
  const [content, setContent] = useState(false);
  const handleClick = () => {
    setContent(!content);
  };

  const handleCancelClick = () => {
    setContent(false); // 취소 버튼 클릭 시 CreateDoc 비활성화
  };

  return (
    <div className="flex items-center justify-center w-screen h-screen">
      <span className="flex items-center justify-center h-h-100 text-white px-10 cursor-pointer bg-main rounded-[10px] text-font-basic" onClick={handleClick}>문서 생성 버튼</span>
      {content && <CreateDoc isActive={content} setIsActive={setContent} />}
    </div>
  );
}
