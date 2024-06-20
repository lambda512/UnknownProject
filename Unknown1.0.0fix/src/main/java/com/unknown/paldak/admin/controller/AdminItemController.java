package com.unknown.paldak.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.unknown.paldak.admin.common.domain.Criteria;
import com.unknown.paldak.admin.common.domain.PageDTO;
import com.unknown.paldak.admin.domain.AttachImageVO;
import com.unknown.paldak.admin.domain.ItemCateVO;
import com.unknown.paldak.admin.domain.ItemVO;
import com.unknown.paldak.admin.domain.ReviewReplyVO;
import com.unknown.paldak.admin.service.AdminAttachServiceImpl;
import com.unknown.paldak.admin.service.BaseService;
import com.unknown.paldak.admin.service.AdminItemCateServiceImpl;
import com.unknown.paldak.admin.service.AdminItemServiceImpl;
import com.unknown.paldak.admin.util.AdminFileUploadManager;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("admin/item/*")
@RequiredArgsConstructor
public class AdminItemController {

    private final BaseService<ItemVO> itemService;
    private final AdminFileUploadManager fileUploadManager;
    private final AdminAttachServiceImpl attachService;
    private final AdminItemCateServiceImpl itemCateService;
    private final AdminItemServiceImpl itemServiceUtil;

    @GetMapping("/list")
    public String list(@ModelAttribute Criteria cri, Model model) {
    	System.out.println("kkk");
        // 페이지 번호와 페이지당 항목 수 설정
        cri.setPageNum(cri.getPageNum() == null ? 1 : cri.getPageNum());
        cri.setAmount(cri.getAmount() == null ? 10 : cri.getAmount());

        System.out.println("Page Number: " + cri.getPageNum());
        System.out.println("Amount: " + cri.getAmount());

        // 데이터 가져오기
        List<ItemCateVO> cateList = itemCateService.getList();
        List<ItemVO> list = itemService.getList(cri);
        int total = itemService.getTotal(cri);

        // 모델에 데이터와 페이징 정보 추가
        model.addAttribute("items", list);
        model.addAttribute("categorys", cateList);
        model.addAttribute("pageMaker", new PageDTO(cri, total));

        return "admin/item";
    }

    @GetMapping("/descList")
    public String descList(@ModelAttribute Criteria cri, Model model) {
        cri.setPageNum(cri.getPageNum() == null ? 1 : cri.getPageNum());
        cri.setAmount(cri.getAmount() == null ? 10 : cri.getAmount());

        List<ItemCateVO> cateList = itemCateService.getList();
        List<ItemVO> list = itemService.getDescList(cri);
        int total = itemService.getTotal(cri);

        model.addAttribute("items", list);
        model.addAttribute("categorys", cateList);
        model.addAttribute("pageMaker", new PageDTO(cri, total));

        return "admin/item";
    }
    
    @PostMapping("/register")
    public String register(@RequestParam("uploadFile") MultipartFile[] uploadFile, Model model, AttachImageVO attachItemVO, ItemVO itemVO, RedirectAttributes rttr) {
        itemService.register(itemVO);
        long newId = itemVO.getItemId();
        if (!uploadFile[0].isEmpty()) {        
            Map<String, String> imageInfo = fileUploadManager.uploadFiles(uploadFile);
            String uuid = imageInfo.get("uuid");
            String fileName = imageInfo.get("originalFilename");
            String uploadPath = imageInfo.get("datePath");
            attachItemVO.setUuid(uuid);
            attachItemVO.setFileName(fileName);
            attachItemVO.setUploadPath(uploadPath);
            attachItemVO.setItemId(newId);
            int result = attachService.register(attachItemVO);

            if (result < 1) {
                return "error";
            }
        }

        boolean itemStateResult = itemServiceUtil.registerItemState(itemVO);
        if (!itemStateResult) {
            return "error";
        }
        rttr.addFlashAttribute("result", newId);
        return "redirect:descList";
    }

    @GetMapping(value = "/get/{itemId}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Map<String, Object>> get(@PathVariable("itemId") long itemId) {
        ItemVO item = itemService.get(itemId);
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("item", item);

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @GetMapping(value = "/itemState/{itemId}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<ItemVO> modifyItemState(@PathVariable("itemId") long itemId) {
        ItemVO itemVO = new ItemVO();
        itemVO.setItemId(itemId);
        itemVO.setItemState("품절");

        boolean result = itemServiceUtil.modifyItemState(itemVO);

        if (result) {
            return new ResponseEntity<>(itemVO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/checkItem/{itemId}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Map<String, Boolean>> checkDuplicateId(@PathVariable("itemId") long itemId) {
        boolean result = false;
        ItemVO itemVO = itemService.get(itemId);
        if (itemVO != null) {
            result = true;
        }

        Map<String, Boolean> response = new HashMap<>();
        response.put("result", result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/modify")
    public String modify(MultipartFile[] uploadFile, ItemVO itemVO, @ModelAttribute("cri") Criteria cri, AttachImageVO attachItemVO, ReviewReplyVO replyVO, @RequestParam("currentPath") String currentPath, RedirectAttributes rttr) {
        long currentItemId = itemVO.getItemId();
        if (!uploadFile[0].isEmpty()) {        
            Map<String, String> imageInfo = fileUploadManager.uploadFiles(uploadFile);
            String uuid = imageInfo.get("uuid");
            String fileName = imageInfo.get("originalFilename");
            String uploadPath = imageInfo.get("datePath");
            attachItemVO.setUuid(uuid);
            attachItemVO.setFileName(fileName);
            attachItemVO.setUploadPath(uploadPath);
            attachItemVO.setItemId(currentItemId);
            attachService.modify(attachItemVO);          
        }

        if (!uploadFile[0].isEmpty()) {
            String imageURL = fileUploadManager.uploadFiles(uploadFile).get("imageURLs");
            itemVO.setItemImageURL(imageURL);
        }

        if (itemService.modify(itemVO)) {
            rttr.addFlashAttribute("result", "success");
        }

        rttr.addAttribute("pageNum", cri.getPageNum());
        rttr.addAttribute("amount", cri.getAmount());
        return "redirect:" + currentPath;
    }

    @PostMapping("/remove")
    public String remove(@RequestParam("itemId") Long itemId, @ModelAttribute("cri") Criteria cri, @RequestParam("currentPath") String currentPath, RedirectAttributes rttr) {
        String itemImageURL = itemService.get(itemId).getItemImageURL();

        if (itemService.remove(itemId)) {
            rttr.addFlashAttribute("result", "success");
        }

        fileUploadManager.deleteFile(itemImageURL);
        rttr.addAttribute("pageNum", cri.getPageNum());
        rttr.addAttribute("amount", cri.getAmount());
        return "redirect:" + currentPath;
    }
}
