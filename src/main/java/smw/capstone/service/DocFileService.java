package smw.capstone.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smw.capstone.DTO.request.ReqUpdateDocDTO;
import smw.capstone.common.exception.BusinessException;
import smw.capstone.common.exception.CustomErrorCode;
import smw.capstone.entity.DocFile;
import smw.capstone.entity.Documents;
import smw.capstone.entity.Files;
import smw.capstone.entity.Member;
import smw.capstone.repository.DocFileRepository;
import smw.capstone.repository.DocRepository;
import smw.capstone.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DocFileService {

    public final DocFileRepository docFileRepository;
    public final DocRepository docRepository;
    public final FileService fileService;
    public final MemberRepository memberRepository;

    @Transactional
    public void updateDocFile(ReqUpdateDocDTO reqUpdateDocDTO, List<String> updateFiles, Member member) {

        List<Files> findMemberFiles = fileService.finByMember(member); //사용자가 등록한 파일 조회
        List<String> memberFileName = new ArrayList<>();
        for (Files file : findMemberFiles) {
            memberFileName.add(file.getName());
        }

        for (String updateFile : updateFiles) {
            if (!memberFileName.contains(updateFile)) {
                throw new BusinessException(CustomErrorCode.NOT_MEMBER_FILE);
            }

        }
        Documents updateDoc = docRepository.findByIdAndMember(reqUpdateDocDTO.getDocId(), member).orElseThrow(() -> new BusinessException(CustomErrorCode.NOT_EXIST_MEMBER_DOC)); //null예외처리
        List<DocFile> docFileList = updateDoc.getDocFileList(); //회원이 등록한 문서와 파일
        List<String> fileNameList = new ArrayList<>();
        if (docFileList != null){
            fileNameList = getFileNameList(docFileList); //DocFile에서 해당 파일 이름 추출

        } else {
            fileNameList = null;
        }
        //fileNameList가 null이면 모두 삭제
        //아니면 존재하는 것 빼고 추가하기
        //문서에 파일 삭제
        if (fileNameList != null) {
            if (updateFiles != null) {
                for (String fileName : fileNameList) {
                    if (!updateFiles.contains(fileName)) {
                        //문서에서 파일 삭제 filename 삭제
                        docFileRepository.deleteByDocumentAndFile(updateDoc, fileService.finByMemberAndFileName(member, fileName));
                    }
                }
                for (String updateFile : updateFiles) {
                    if (!fileNameList.contains(updateFile)) {
                        //문서에 파일 추가
                        DocFile newDocFile = buildDocFile(updateFile, updateDoc, member);
                        saveDocFile(newDocFile);
                    }
                }
            } else {
                //docfile모두 삭제
                deleteDocFileByDoc(updateDoc);
            }
        } else {
            //docfile이 하나도 없었으므로 updateFiles모두 추가
            List<DocFile> newDocFiles = new ArrayList<>();
            for (String updateFile : updateFiles) {
                DocFile newDocFile = buildDocFile(updateFile, updateDoc, member);
                newDocFiles.add(newDocFile);
            }
            docFileRepository.saveAll(newDocFiles);
        }

    }

    private DocFile buildDocFile(String updateFile, Documents updateDoc, Member temp) {
        return DocFile.builder()
                .document(updateDoc)
                .file(fileService.finByMemberAndFileName(temp, updateFile)).build();
    }

    private static List<String> getFileNameList(List<DocFile> docFileList) {
        List<String> fileNameList = new ArrayList<>();
        for (DocFile docFile : docFileList) {
            fileNameList.add(docFile.getFile().getName());
        }
        return fileNameList;
    }

    public void saveDocFile(DocFile docFile) {
        docFileRepository.save(docFile);
    }

    public void deleteDocFileByDoc(Documents documents) {
        docFileRepository.deleteAllByDocument(documents);
    }

    public List<DocFile> getDocFileByDocument(Documents documents) {
        return docFileRepository.findByDocument(documents);
    }

    public List<DocFile> findDocfileByDocId(Long docId) {
        return docFileRepository.findByDocumentId(docId);
    }


}

