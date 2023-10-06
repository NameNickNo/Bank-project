package com.project.account.controller;

import com.project.account.dto.DepositDTO;
import com.project.account.dto.TransferDTO;
import com.project.account.dto.WithdrawDTO;
import com.project.account.exception.BadRequestException;
import com.project.account.service.BalanceOperationService;
import com.project.account.service.TransferOperationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/balance")
public class BalanceOperationController {

    private final BalanceOperationService balanceOperationService;
    private final TransferOperationService transferOperationService;

    @Autowired
    public BalanceOperationController(BalanceOperationService balanceOperationService,
                                      TransferOperationService transferOperationService) {
        this.balanceOperationService = balanceOperationService;
        this.transferOperationService = transferOperationService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<HttpStatus> depositAmount(@RequestBody @Valid DepositDTO depositDTO, BindingResult bindingResult) {
        validateAttributes(bindingResult);

        balanceOperationService.createDepositOperation(depositDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<HttpStatus> withdrawAmount(@RequestBody @Valid WithdrawDTO withdrawDTO, BindingResult bindingResult) {
        validateAttributes(bindingResult);

        balanceOperationService.createWithdrawOperation(withdrawDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/transfer")
    public ResponseEntity<HttpStatus> transferAmount(@RequestBody @Valid TransferDTO transferDTO, BindingResult bindingResult) {
        validateAttributes(bindingResult);

        transferOperationService.createTransfer(transferDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private void validateAttributes(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null)
                throw new BadRequestException("Attribute '" + fieldError.getField() + "' " + fieldError.getDefaultMessage());
            else
                throw new BadRequestException("Request not sent!");
        }
    }
}
