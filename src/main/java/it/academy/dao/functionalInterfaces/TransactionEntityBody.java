package it.academy.dao.functionalInterfaces;

@FunctionalInterface
public interface TransactionEntityBody<T> {

    T execute() throws Exception;
}
