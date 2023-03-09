package com.fullstack.controller;

import com.fullstack.dto.GuestbookDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fullstack.dto.PageRequestDTO;
import com.fullstack.service.GuestbookService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/guestbook")
@Log4j2
//서비스 구현체를 DI 시키기 위해서 프레임워크에 @argmentsConst..선언
@RequiredArgsConstructor
public class GuestbookController {

   //서비스 구현체 선언합니다.
   private final GuestbookService service;
   
   //context로 요청이 올때 요청 처리. ex>guestbook/ 이러게 요청온 경우 리다이렉션 시킴
   @GetMapping("/")
   public String index() {
      return "redirect:/guestbook/list";
   }

   /*
    * 아래 list의 파라미터로 RequestDTO를 선언하게 되면,
    * 프레임워크가 기본적으로 사용자의 요청 파라미터를 dto에 맵핑하여 줍니다.
    * ex>page number등..해서 파라미터로 정의한 RequestDTO를 주고,
    * service 계층에서 처리된 목록 리스트를 View단으로 전달하기 위해 Model 파라미터도 선언합니다.
    */
   @GetMapping("/list")
   public void list(PageRequestDTO pageRequestDTO, Model model) {
      log.info("리스트 페이지 요청이 처리됨.");

      model.addAttribute("list",service.getList(pageRequestDTO)) ;
   }

   @GetMapping("/register")
   public void getRegister(){
      log.info("등록 요청됨");

   }
   @PostMapping("/register")
   public String register(GuestbookDTO dto, RedirectAttributes redirectAttributes) {
      log.info("등록 요청됨");
             Long number = service.register(dto);

      log.info("엔티티의 신규 글 번호 : " + number);

      //신규 글 작성이 완료된 후 list redirect 시키는데,
      //list 페이지에서는 글작성후 인지 그냥 글목록을 보는지 모름..
      //따라서, list 페이지에선 여기서 넘겨주는 key 값을 판별후
      //동적으로 alert() 를 생성해서 사용자에게 고지할 예정입니다.
      redirectAttributes.addFlashAttribute("msg",number);
      return "redirect:/guestbook/list";
   }
   @GetMapping("/read")
   public void read(Long ago, @ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Model model) {
      log.info("조회 요청됨");

      GuestbookDTO dto = service.read(ago);

      model.addAttribute("dto",dto);

   }
   @GetMapping("/modify")
   public void getmodify(Long ago, @ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Model model) {
      log.info("수정 요청됨");

      GuestbookDTO dto = service.getModify(ago);

      model.addAttribute("dto",dto);
   }
   @PostMapping("/modify")
   public String modify(GuestbookDTO dto,@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO,
                      RedirectAttributes redirectAttributes, Model model) {
      Long number = service.modify(dto);
      redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
      redirectAttributes.addAttribute("ago", dto.getAgo());
      redirectAttributes.addFlashAttribute("msg",number);
      model.addAttribute("dto",dto);
      return "redirect:/guestbook/list";

   }
   @GetMapping("/remove")
   public void getRemove(GuestbookDTO dto,Long ago, @ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Model model) {
      service.getRemove(ago);

      model.addAttribute("dto",dto);

   }
   @PostMapping("/remove")
   public String remove(GuestbookDTO dto,@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO,
                      RedirectAttributes redirectAttributes, Model model) {
      model.addAttribute("dto",dto);

      return "redirect:/guestbook/list";
   }

      //신규 글 작성이 완료된 후 list redirect 시키는데,
      //list 페이지에서는 글작성후 인지 그냥 글목록을 보는지 모름..
      //따라서, list 페이지에선 여기서 넘겨주는 key 값을 판별후
      //동적으로 alert() 를 생성해서



      //신규 글 작성이 완료된 후 list redirect 시키는데,
      //list 페이지에서는 글작성후 인지 그냥 글목록을 보는지 모름..
      //따라서, list 페이지에선 여기서 넘겨주는 key 값을 판별후
      //동적으로 alert() 를 생성해서 사용자에게 고지할 �
//   //View 페이지명을 리턴하는 메서드 정의
//   @GetMapping({"/","/list"})
//   public String list() {
//      log.info("방명록 테스트 리스트 페이지 호출됨");
//      
//      return "/guestbook/list";
//   }
}
