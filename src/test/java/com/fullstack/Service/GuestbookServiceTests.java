package com.fullstack.Service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fullstack.dto.GuestbookDTO;
import com.fullstack.dto.PageRequestDTO;
import com.fullstack.dto.PageResultDTO;
import com.fullstack.entity.Guestbook;
import com.fullstack.service.GuestbookService;

@SpringBootTest
public class GuestbookServiceTests {
	@Autowired
	//private GuestbookRepository guestbookRepository;
	private GuestbookService guestbookService;
	
	//신규글 등록 테스트 작업 메서드
//	@Test
//	public void registerTest() {
//		
//		//DTO를 생성해서 Service 개출에 넘깁니다.
//		GuestbookDTO guestbookDTO = GuestbookDTO.builder()
//				.title("이건 신규 등록한 글 제목")
//				.content("이건 신규 등록한 글 내용입니다.")
//				.writer("쌤1")
//				.build();
//		System.out.println(guestbookService.register(guestbookDTO));
//	}
//	@Test
//	public void listTest() {
		//PageRequestDTO 는 직접생성해서 던져줍니다.
//		PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
//				.page(1)
//				.size(10)
//				.build();
//		
//		//PageResultDTO 객체를 생성해서 조회된 리스트 확인 합니다.
//		PageResultDTO<GuestbookDTO, Guestbook> resultDTO =
//				guestbookService.getList(pageRequestDTO);
//		
//		for(GuestbookDTO guestbookDTO : resultDTO.getDtoList()) {
//			System.out.println(guestbookDTO);
//		}
//		System.out.println("----------------------------");
		
		
//	}
	
	//글목록에서 페이징 처리된 내용 알아보기..
	@Test
	public void testPaging() {
		PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
				.page(11)
				.size(10)
				.build();
		PageResultDTO<GuestbookDTO, Guestbook> resultDTO =
				guestbookService.getList(pageRequestDTO);
		
		System.out.println("이전 표시 : " + resultDTO.isPrev());
		System.out.println("Next 표시 " + resultDTO.isNext());
		System.out.println("실제 총 페이지 수" + resultDTO.getTotalPage());
		
	}
	
	
	
	
	
	
	
}