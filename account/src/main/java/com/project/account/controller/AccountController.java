package com.project.account.controller;

import com.project.account.dto.AccountCreateDTO;
import com.project.account.dto.AccountDTO;
import com.project.account.dto.AccountWithOperationDTO;
import com.project.account.exception.AccountNotCreateException;
import com.project.account.exception.AccountNotFoundException;
import com.project.account.model.Account;
import com.project.account.service.AccountService;
import com.project.account.service.MessageProducer;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;
    private final ModelMapper modelMapper;
    private final MessageProducer messageProducer;

    @Autowired
    public AccountController(AccountService accountService, ModelMapper modelMapper, MessageProducer messageProducer) {
        this.accountService = accountService;
        this.modelMapper = modelMapper;
        this.messageProducer = messageProducer;
    }

    @GetMapping
    public ResponseEntity<List<AccountDTO>> findAll() {
        List<Account> accounts = accountService.findAll();
        List<AccountDTO> accountDTOS = accounts.stream().map(account -> modelMapper.map(account, AccountDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(accountDTOS);
    }

    @GetMapping("/{number}")
    public ResponseEntity<AccountWithOperationDTO> getOperations(@PathVariable("number") long number) {
        Account account = accountService.findByNumberWithOperation(number)
                .orElseThrow(() -> new AccountNotFoundException(number));

        AccountWithOperationDTO accountDTO = modelMapper.map(account, AccountWithOperationDTO.class);
        messageProducer.produce(accountDTO);

        return ResponseEntity.ok(accountDTO);
    }

    @PostMapping("/create")
    public ResponseEntity<AccountCreateDTO> createAccount(@RequestBody @Valid AccountCreateDTO accountDTO,
                                                    BindingResult bindingResult) {
        validateAttributes(bindingResult);

        accountService.createAccount(modelMapper.map(accountDTO, Account.class));
        return new ResponseEntity(accountDTO, HttpStatus.CREATED);
    }

    private void validateAttributes(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null)
                throw new AccountNotCreateException("Attribute '" + fieldError.getField() + "' " + fieldError.getDefaultMessage());
            else
                throw new AccountNotCreateException("Account not created!");
        }
    }
}
