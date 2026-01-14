package com.archive.archive_project_backend.service.writing;

import com.archive.archive_project_backend.constants.FileConstants;
import com.archive.archive_project_backend.constants.WritingConstants;
import com.archive.archive_project_backend.dto.req.AddWritingReqDto;
import com.archive.archive_project_backend.dto.req.FindWritingReqDto;
import com.archive.archive_project_backend.dto.req.PatchSeriesOrderReqDto;
import com.archive.archive_project_backend.dto.req.PatchWritingReqDto;
import com.archive.archive_project_backend.dto.res.FindWritingResDto;
import com.archive.archive_project_backend.dto.res.WritingInteractionStateResDto;
import com.archive.archive_project_backend.entity.*;
import com.archive.archive_project_backend.exception.FastRequestException;
import com.archive.archive_project_backend.exception.add.AddException;
import com.archive.archive_project_backend.exception.add.AddTagException;
import com.archive.archive_project_backend.exception.add.AddWritingException;
import com.archive.archive_project_backend.exception.BadRequestException;
import com.archive.archive_project_backend.exception.FindWritingException;
import com.archive.archive_project_backend.model.WritingIndexModel;
import com.archive.archive_project_backend.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class WritingService {
    private final WritingMapper writingMapper;
    private final CategoryMapper categoryMapper;
    private final SeriesMapper seriesMapper;
    private final UserMapper userMapper;
    private final TagMapper tagMapper;
    private final ObjectMapper objectMapper;
    private final WritingContentImageMigrator writingContentImageMigrator;

    //List<String> tag만으로 글과 태그를 연결
    @Transactional(rollbackFor = Exception.class)
    private void connectWritingTag(Writing writing, List<String> tag){
        //태그 추가
        tagMapper.insertTags(Tag.asStrings(tag));

        //id 얻어오기
        List<Tag> tags = tagMapper.getTagsByTagNames(Tag.asStrings(tag));

        //이를 연결함
        int successCount = tagMapper.insertWritingTags(writing.getWritingUuid(), tags);

        if(successCount < tags.size()){
            throw new AddTagException();
        }
    }


    //단건 글 추가
    @Transactional(rollbackFor = Exception.class)
    public void addWriting(AddWritingReqDto dto, String authorUuid) throws IOException {

        //식별자 생성 및 entity 받아옴
        String uuid = UUID.randomUUID().toString();
        User author = userMapper.getUserByUuid(authorUuid);

        Category category = categoryMapper.getCategoryById(dto.getCategoryId());
        //category 없음 -> error 반환
        if(category == null) {
            throw new BadRequestException("존재하지 않는 카테고리입니다.");
        }

        Series series = null;
        if(dto.getSeriesUuid() != null){
            series = seriesMapper.getSeriesByUuid(dto.getSeriesUuid());
            if(series == null){
                throw new BadRequestException("존재하지 않는 시리즈입니다.");
            }
        }

        //태그 중복 제거
        dto.setTag(new ArrayList<>(new LinkedHashSet<>(dto.getTag())));

        //이미지 주소를 옮기기
        writingContentImageMigrator.migrateTmpImages(
                dto.getContent(),
                FileConstants.WRITING + "/" + uuid);

        //entity 생성
        Writing writing = dto.toEntity(uuid,author, category, series, objectMapper);

        //추가
        int successCount = writingMapper.insertWriting(writing);
        if(successCount < 1){
            throw new AddWritingException();
        }

        //태그 연결
        connectWritingTag(writing, dto.getTag());

        //series면 다은 order 가져오기
        if(dto.getSeriesUuid() != null) {
            writing.setSeriesOrder(seriesMapper.getNextSeriesOrder(dto.getSeriesUuid()));
        }

        //메타데이터 증가
        userMapper.updateTotalWritingDelta(authorUuid, 1);
    }

    //단건 글 조회
    //조회 시 댓글을 제외한 정보들을 반환해줘야함
    @Transactional(rollbackFor = Exception.class)
    public FindWritingResDto findWritingByUuid(FindWritingReqDto reqDto){
        //가드
        if(reqDto.getWritingUuid() == null){
            throw new BadRequestException("글 찾기의 요청이 이상합니다.");
        }

        //글 받아오기
        Writing writing = writingMapper.selectWritingByUuid(reqDto.getWritingUuid());

        if(writing == null){
            throw new FindWritingException("해당 uuid의 글이 없습니다.");
        }

        //태그 추가
        List<Tag> tags = tagMapper.selectTagsByWritingUuid(writing.getWritingUuid());
        writing.setTag(tags);

        return FindWritingResDto.from(writing, objectMapper);
    }

    //시리즈 글 index 조회
    public List<WritingIndexModel> getWritingIndexBySeriesUuid(String seriesUuid){
        if(seriesMapper.getSeriesByUuid(seriesUuid) == null){
            throw new BadRequestException("존재하지 않는 시리즈입니다.");
        }

        List<Writing> writings = writingMapper.selectWritingBySeriesUuid(seriesUuid);
        writings.sort(Comparator.comparingInt(Writing::getSeriesOrder));
        return writings.stream().map(WritingIndexModel::from).toList();
    }

    //좋아요 / 북마크 여부 반환
    @Transactional
    public WritingInteractionStateResDto getWritingInteractionState(String writingUuid, String userUuid){
        //비로그인 처리
        if(userUuid == null){
            return new WritingInteractionStateResDto(false, false);
        }

        if(!writingMapper.existsWritingByUuid(writingUuid)){
            throw new BadRequestException("해당 글은 존재하지 않습니다.");
        }

        return new WritingInteractionStateResDto(
                writingMapper.getGreatedByUuids(writingUuid, userUuid),
                writingMapper.getBookmarkedByUuids(writingUuid, userUuid)
        );
    }

    //좋아요 켜기
    @Transactional(rollbackFor = Exception.class)
    public void greatedWriting(String writingUuid, String userUuid){
        if(!writingMapper.existsWritingByUuid(writingUuid)){
            throw new BadRequestException("해당 글은 존재하지 않습니다.");
        }

        int successCount = writingMapper.insertGreated(writingUuid, userUuid);
        if(successCount < 1){
            throw new FastRequestException();
        }

        successCount = writingMapper.increaseGreat(writingUuid);
        if(successCount < 1){
            throw new FastRequestException();
        }

        String authorUuid = writingMapper.getAuthorUuidByUuid(writingUuid);
        successCount = userMapper.updateTotalGreatDelta(authorUuid, 1);
        if(successCount < 1){
            throw new FastRequestException();
        }
    }

    //좋아요 끄기
    @Transactional(rollbackFor = Exception.class)
    public void ungreatedWriting(String writingUuid, String userUuid){
        if(!writingMapper.existsWritingByUuid(writingUuid)){
            throw new BadRequestException("해당 글은 존재하지 않습니다.");
        }

        int successCount = writingMapper.deleteGreated(writingUuid, userUuid);
        if(successCount < 1){
            throw new FastRequestException();
        }

        successCount = writingMapper.decreaseGreat(writingUuid);
        if(successCount < 1){
            throw new FastRequestException();
        }

        String authorUuid = writingMapper.getAuthorUuidByUuid(writingUuid);
        successCount = userMapper.updateTotalGreatDelta(authorUuid, -1);
        if(successCount < 1){
            throw new FastRequestException();
        }
    }

    //북마크 켜기
    public void bookmarkedWriting(String writingUuid, String userUuid){
        if(!writingMapper.existsWritingByUuid(writingUuid)){
            throw new BadRequestException("해당 글은 존재하지 않습니다.");
        }

        int successCount = writingMapper.insertBookmarked(writingUuid, userUuid);
        if(successCount < 1){
            throw new FastRequestException();
        }
    }

    //북마크 끄기
    public void unbookmarkedWriting(String writingUuid, String userUuid){
        if(!writingMapper.existsWritingByUuid(writingUuid)){
            throw new BadRequestException("해당 글은 존재하지 않습니다.");
        }

        int successCount = writingMapper.deleteBookmarked(writingUuid, userUuid);
        if(successCount < 1){
            throw new FastRequestException();
        }
    }

    //조회수 증가
    @Transactional(rollbackFor = Exception.class)
    public void increaseView(String writingUuid){
        if(!writingMapper.existsWritingByUuid(writingUuid)){
            throw new BadRequestException("해당 글은 존재하지 않습니다.");
        }

        int successCount = writingMapper.increaseView(writingUuid);
        if(successCount < 1){
            throw new FastRequestException();
        }

        String authorUuid = writingMapper.getAuthorUuidByUuid(writingUuid);
        successCount = userMapper.updateTotalViewDelta(authorUuid, 1);
        if(successCount < 1){
            throw new FastRequestException();
        }
    }

    //글 삭제
    @Transactional(rollbackFor = Exception.class)
    public void deleteWriting(String writingUuid, String userUuid){
        Writing writing = writingMapper.selectWritingByUuid(writingUuid);

        if(!userUuid.equals(writing.getAuthor().getUserUuid())){
            throw new BadRequestException("작성자가 아닙니다.");
        }

        //태그 연결 제거
        int successCount = tagMapper.deleteWritingTag(writingUuid);
        if(successCount < writing.getTag().size()){
            throw new BadRequestException("해당 글은 존재하지 않습니다. (Tag)");
        }

        String authorUuid = writing.getAuthor().getUserUuid();
        //연결 메타데이터 감소
        successCount = userMapper.updateTotalWritingDelta(authorUuid, -1) +
                        userMapper.updateTotalViewDelta(authorUuid, -writing.getView()) +
                        userMapper.updateTotalCommentDelta(authorUuid, -writing.getCommentCount()) +
                        userMapper.updateTotalGreatDelta(authorUuid, -writing.getGreat());

        if(successCount < 4){
            throw new BadRequestException("해당 글은 존재하지 않습니다. (Meta)");
        }

        //좋아요 이력 제거
        writingMapper.deleteAllGreat(writingUuid);

        //댓글 전부 제거
        writingMapper.deleteAllComment(writingUuid);

        //북마크 이력 제거
        writingMapper.deleteAllBookmarked(writingUuid);

        //완전 제거
        successCount = writingMapper.deleteWritingByUuid(writingUuid);
        if(successCount < 1){
            throw new BadRequestException("해당 글은 존재하지 않습니다. (Del)");
        }
    }

    //글 수정
    @Transactional(rollbackFor = Exception.class)
    public void patchWriting(String writingUuid, String userUuid, PatchWritingReqDto dto) throws IOException {
        Writing writing = writingMapper.selectWritingByUuid(writingUuid);

        //가드
        if(writing == null){
            throw new BadRequestException("존재하지 않는 글입니다.");
        }
        if(!writing.getAuthor().getUserUuid().equals(userUuid)){
            throw new BadRequestException("작성자가 아닙니다.");
        }

        //수정

        //카테고리
        if(dto.getCategoryId() != null){
            writingMapper.updateCategory(writingUuid, dto.getCategoryId());
        }

        //태그
        if(dto.getTag() != null){
            tagMapper.deleteWritingTag(writingUuid);
            connectWritingTag(writing, dto.getTag());
        }

        //시리즈
        if(dto.getSeriesUuid() != null){
            String seriesUuid = dto.getSeriesUuid();

            //기존 시리즈의 order 재정렬
            if(writing.getSeries() != null){
                int reorderStart = writing.getSeriesOrder() + 1;
                writingMapper.reorderSeriesOrder(writing.getSeries().getSeriesUuid(), reorderStart);
            }

            // -> null
            if(seriesUuid.equals(WritingConstants.NOT_SERIES)){
                writingMapper.updateSeries(writingUuid, null, null);
            }
            // -> new series
            else{
                if(seriesMapper.getSeriesByUuid(seriesUuid) == null){
                    throw new BadRequestException("존재하지 않는 시리즈입니다.");
                }

                //order 결정
                Integer nextOrder = seriesMapper.getNextSeriesOrder(seriesUuid);
                if(nextOrder == null) nextOrder = 1;
                writingMapper.updateSeries(writingUuid, seriesUuid, nextOrder);

            }
        }

        //content
        if(dto.getContent() != null){
            writingContentImageMigrator.migrateTmpImages(
                    dto.getContent(),
                    FileConstants.WRITING + "/" + writingUuid);
            String contentJson;
            try {
                contentJson = objectMapper.writeValueAsString(dto.getContent());
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("content를 JSON 문자열로 변환 실패", e);
            }
            writingMapper.updateContent(writingUuid, contentJson);
        }

        //title
        if(dto.getTitle() != null){
            writingMapper.updateTitle(writingUuid, dto.getTitle());
        }

        //수정일
        writingMapper.updateTime(writingUuid);
    }

    //글 오더 수정
    @Transactional(rollbackFor = Exception.class)
    public void patchSeriesOrder(String seriesUuid, List<PatchSeriesOrderReqDto> dtos){
        List<Writing> writingList = writingMapper.selectWritingsByStrings(dtos.stream().map(PatchSeriesOrderReqDto::getWritingUuid).toList());

        //가드
        if(writingList.size() != dtos.size()){
            throw new BadRequestException("글의 uuid 중 일치하지 않는 것이 있습니다.");
        }
        for(Writing w : writingList){
            if(!w.getSeries().getSeriesUuid().equals(seriesUuid)){
                throw new BadRequestException("글 중 시리즈와 일치하지 않는 것이 있습니다.");
            }
        }

        //정렬
        dtos.sort(Comparator.comparingInt(PatchSeriesOrderReqDto::getIndex));
        List<Writing> newWritingList = new ArrayList<>();
        for(PatchSeriesOrderReqDto dto : dtos){
            Writing target = writingList.stream().filter(writing -> writing.getWritingUuid().equals(dto.getWritingUuid())).findFirst().get();
            newWritingList.add(target);
        }

        int successCount = writingMapper.updateSeriesOrderByList(newWritingList);
        if(successCount != newWritingList.size()){
            throw new AddException("시리즈 순서 설정 중 에러가 발생했습니다");
        }
    }
}
