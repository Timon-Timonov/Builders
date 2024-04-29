package it.academy.util.functionalInterfaces;

import it.academy.service.dto.Page;

@FunctionalInterface
public interface TransactionPageBody<T> {

    Page<T> execute() throws Exception;
}
