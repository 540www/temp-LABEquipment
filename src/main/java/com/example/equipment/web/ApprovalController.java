package com.example.equipment.web;

import com.example.equipment.model.Approval;
import com.example.equipment.repository.ApprovalRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/approvals")
public class ApprovalController {

    private final ApprovalRepository approvalRepository;

    public ApprovalController(ApprovalRepository approvalRepository) {
        this.approvalRepository = approvalRepository;
    }

    @PostMapping("/submit")
    public Approval submit(@RequestParam String bizType, @RequestParam Long bizId) {
        Approval a = new Approval();
        a.setBizType(bizType);
        a.setBizId(bizId);
        a.setNode("提交");
        a.setResult("PENDING");
        a.setCreatedAt(LocalDateTime.now());
        return approvalRepository.save(a);
    }

    @PostMapping("/{id}/decide")
    public Approval decide(@PathVariable Long id, @RequestParam String result, @RequestParam(required = false) String comment) {
        Approval a = approvalRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("审批不存在"));
        a.setResult(result);
        a.setComment(comment);
        a.setActedAt(LocalDateTime.now());
        return approvalRepository.save(a);
    }

    @GetMapping
    public List<Approval> list() { return approvalRepository.findAll(); }
}


