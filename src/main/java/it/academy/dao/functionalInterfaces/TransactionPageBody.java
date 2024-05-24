package it.academy.dao.functionalInterfaces;

import it.academy.dto.Page;

@FunctionalInterface
public interface TransactionPageBody<T> {

    Page<T> execute() throws Exception;
}
