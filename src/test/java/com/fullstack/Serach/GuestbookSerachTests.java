package com.fullstack.Serach;

import com.fullstack.dto.GuestbookDTO;
import com.fullstack.dto.PageRequestDTO;
import com.fullstack.dto.PageResultDTO;
import com.fullstack.entity.Guestbook;
import com.fullstack.service.GuestbookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GuestbookSerachTests {
    @Autowired
    private GuestbookService service;
    //검색 기능의 단위 테스트
    @Test
    public void findKwordTest() {
        //PageRequest 객체 생성..
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .type("c")
                .keyword("신규")
                .build();

        PageResultDTO<GuestbookDTO, Guestbook> resultDTO =
                service.getList(pageRequestDTO);

        System.out.println("+++++++++++ 조건 검색 결과 +++++++++++");
        System.out.println("total : " + resultDTO.getTotalPage());

        for(GuestbookDTO guestbookDTO : resultDTO.getDtoList()) {
            System.out.println(guestbookDTO);
        }
    }
}
