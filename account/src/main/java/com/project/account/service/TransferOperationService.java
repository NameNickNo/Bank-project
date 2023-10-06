package com.project.account.service;

import com.project.account.dto.TransferDTO;

public interface TransferOperationService {
    void createTransfer(TransferDTO transferDTO);
}
