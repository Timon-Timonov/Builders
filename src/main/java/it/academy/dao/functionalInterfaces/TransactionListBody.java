package it.academy.dao.functionalInterfaces;

import java.util.List;

@FunctionalInterface
public interface TransactionListBody<T> {

    List<T> execute() throws Exception;
}
