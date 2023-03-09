package com.fullstack.service;

import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.util.ArrayBuilders.BooleanBuilder;
import com.fullstack.dto.GuestbookDTO;
import com.fullstack.dto.PageRequestDTO;
import com.fullstack.dto.PageResultDTO;
import com.fullstack.entity.Guestbook;
import com.fullstack.entity.QGuestbook;
import com.fullstack.repository.GuestbookRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

//@Service 어노테이션은 해당 구현 객체를 빈객체로 사용하겠다고 선언한거임
//실행중에 자동의 해당 인스턴스를 생성해서 주입시켜줍니다. 이걸 Bean 이라고 합니다.
@Service
@Log4j2
//final 속성의 객체를 주입하는 어노테이션..RequiredArgsConstructor
@RequiredArgsConstructor
public class GuestBookServiceImpl implements GuestbookService {

	private final GuestbookRepository guestbookRepository;

	@Override
	public Long register(GuestbookDTO dto) {
		
		
		log.info("+++++++++++++++ DTO 신규 등록 +++++++++++++++");
		log.info(dto);
		
		//DTO --> Entity 로 변환되는 부모 메서드 호출합니다.
		Guestbook entity = dtoToEntity(dto);
		
		log.info("변환된 엔티티 정보 ---->" + entity);
		
		guestbookRepository.save(entity);
		return entity.getAgo();
		
	}
	
	
	@Override
	public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO) {
		Pageable pageable = requestDTO.getPageable(Sort.by("ago").descending());
		
		//repository 를 통해 pageable 파라미터로 Page Entity 리스트를 얻어냅니다.

		//조회조건이 없는 상태의 list 페이지의 목록을 리턴하는 메서드
		//Page<Guestbook> result = guestbookRepository.findAll(pageable);

		//조회 조건을 추가하여 list 페이지의 목록을 리턴하는 구문..
		BooleanBuilder booleanBuilder = getFind(requestDTO);
		Page<Guestbook> result = guestbookRepository.findAll(booleanBuilder, pageable);

		//Entity 를 DTO 로 변환 하도록 Function 객체 생성..
		Function<Guestbook, GuestbookDTO> fn = (entity -> entityToDto(entity));
		
		//PageResultDTO 객체 생성..
		return new PageResultDTO<>(result, fn);
	}

    @Override
    public GuestbookDTO read(Long ago) {
		Optional<Guestbook> result = guestbookRepository.findById(ago);
		return result.isPresent()? entityToDto(result.get()):null;
	}

	@Override
	public GuestbookDTO getModify(Long ago) {
		Optional<Guestbook> result = guestbookRepository.findById(ago);
		return result.isPresent()? entityToDto(result.get()):null;
	}
	@Override
	public Long modify(GuestbookDTO dto) {
		log.info(dto);
		//DTO --> Entity 로 변환되는 부모 메서드 호출합니다.
		Guestbook entity = dtoToEntity(dto);
		guestbookRepository.save(entity);
		return entity.getAgo();
	}
	@Override
	public GuestbookDTO getRemove(Long ago) {
		Optional<Guestbook> result = guestbookRepository.findById(ago);
		return result.isPresent()? entityToDto(result.get()):null;
	}
	@Override
	public Long remove(GuestbookDTO	dto) {
		log.info(dto);
        //DTO --> Entity 로 변환되는 부모 메서드 호출합니다.
        Guestbook entity = dtoToEntity(dto);
        guestbookRepository.delete(entity);
        return entity.getAgo();
	}

	//검색 조건을 추가하여, 검색에 매칭되는 Entity 를 구성해서 getListPage()로 보낸다
	//QueryDSL 을 이용할 예정이라, 리턴 타입은 javax.persistence.Page 객체로
	//리턴시키기 위해 BooleanBuilder 객체로 리턴할 예정..
	//이렇게 리턴된 BooleanBuilder 를 findAll(BooleanBuilder, Pageable)
	//메서드를 통해 Page 객체를 얻어내서 list.html 까지 전달 시킨다.
	private BooleanBuilder getFind(PageRequestDTO pageRequestDTO) {
		//사용자가 요청한 검색 키워드 알아내기

		String type = pageRequestDTO.getType();//c,w,t 거나 모두 이거나..
		//QS 도메인 생성..
		QGuestbook qGuestbook = QGuestbook.guestbook;
		String keyword = pageRequestDTO.getKeyword();

		BooleanBuilder booleanBuilder = new BooleanBuilder();
		//쿼리DSL 의 장점 : Entity 필드를 조회 조건으로 이용할 수 있다.

		//검색 조건을 생성하는데, 기본적으로 ago 를 기준으로 먼저 검색조건을 생성합니다.
		BooleanExpression booleanExpression = qGuestbook.ago.gt(0L);
		//생성된 조회건을 booleanBuilder 에 추가합니다.
		//이유는 repository.find....() 에 들어갈 파라미터는 BooleanBuilder 객체이기
		//때문입니다.
		booleanBuilder.and(booleanExpression);

		//만약 검색 조건이 아무것도 없다면, 일반 조회조건 즉, ago > 0 인 애들을
		//돌려주도록 합니다.
		if(type == null || type.equals("") || type.isEmpty() || type.length() == 0) {
			return booleanBuilder;
		}

		//아래서 사용될 조회 조건을 담는 BooleanBuilder 또하나 생성
		BooleanBuilder findbuilder = new BooleanBuilder();

		//어떤 필드(QDmain) 에서 keyword 를 찾아야 할지 요청 타입(type) 을 검색해 봅니다.
		//만약 아무런 type 조건이 없을 경우, 그냥 글번호 를 기준으로 넘겨주도록 합니다.
		if (type.contains("t")) {
			//모든 조건을 or 로 묶어서 추가합니다.
			findbuilder.or(qGuestbook.title.contains(keyword));
		}
		if(type.contains("w")) {
			//모든 조건을 or 로 묶어서 추가합니다.
            findbuilder.or(qGuestbook.writer.contains(keyword));
		}
		if(type.contains("c")) {
            //모든 조건을 or 로 묶어서 추가합니다.
            findbuilder.or(qGuestbook.content.contains(keyword));
        }
		return booleanBuilder;
	}
}
